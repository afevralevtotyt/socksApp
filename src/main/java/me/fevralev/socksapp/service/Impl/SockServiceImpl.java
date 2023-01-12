package me.fevralev.socksapp.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.fevralev.socksapp.exception.*;
import me.fevralev.socksapp.model.*;
import me.fevralev.socksapp.service.FilesService;
import me.fevralev.socksapp.service.SockService;
import me.fevralev.socksapp.validator.ColorValid;
import me.fevralev.socksapp.model.Size;
import me.fevralev.socksapp.model.Color;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;
@Validated
@Service
public class SockServiceImpl implements SockService {

    final private FilesService filesService;

    private List<Operation> operations = new ArrayList<>();

    public SockServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    public Map<Sock, Integer> socksMap = new HashMap();

    @Override
    public Map<Sock, Integer> getSocksMap() {
        return socksMap;
    }

    @PostConstruct
    public void init() {
        try {
            readFromFile();
        } catch (FileReadException e) {
            e.printStackTrace();
        }
    }
    @PostConstruct
    public void initReport() {
        try {
            readReportFile();
        } catch (FileReadException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SockInput add(@Valid  SockInput socksInput) {
        Sock sock = new Sock(Color.colorOf(socksInput.getColor()), Size.valueOf(socksInput.getSize()), socksInput.getCottonPart());
        socksMap.put(sock, (socksMap.getOrDefault(sock, 0) + socksInput.getQuantity()));
        saveToFile();
        operations.add(new Operation("Приемка", socksInput.getQuantity(), sock));
        saveReportToFile();
        return socksInput;
    }

    @Override
    public int getSocks(@ColorValid String color, @Min(36) @Max(49) int size, @Min(0) @Max(100) int cottonMin, @Min(0)@Max(100) int cottonMax) {
        if (cottonMin > cottonMax) {
            throw new WrongCottonPartException("Введен неверный процент хлопка");
        }
        int sum = 0;
        for (Sock socks : socksMap.keySet()
        ) {
            if (socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax && socks.getColor().equals(Color.colorOf((color))) && socks.getSize() == Size.valueOf(size)) {
                sum += socksMap.get(socks);
            }
        }
        return sum;
    }

    @Override
    public SockInput delete(@Valid SockInput socksInput) {
        Sock socks = new Sock(Color.colorOf(socksInput.getColor()), Size.valueOf(socksInput.getSize()), socksInput.getCottonPart());
        if (socksMap.containsKey(socks)) {
            if (socksInput.getQuantity() == socksMap.get(socks)) {
                socksMap.remove(socks);
            } else if (socksInput.getQuantity() > socksMap.get(socks)) {
                throw new WrongQuantityException("Введено неверное количество товара");
            } else {
                socksMap.put(socks, socksMap.get(socks) - socksInput.getQuantity());
            }
            saveToFile();
            operations.add(new Operation("Списание", socksInput.getQuantity(), socks));
            saveReportToFile();
            return socksInput;
        }
        return null;
    }

    @Override
    public String edit(@Valid SockInput socksInput) {
        Sock sock = new Sock(Color.colorOf(socksInput.getColor()), Size.valueOf(socksInput.getSize()), socksInput.getCottonPart());
        if (socksMap.containsKey(sock)) {
            if (socksInput.getQuantity() > socksMap.get(sock)) {
                throw new WrongQuantityException("Нет товара в таком количестве");
            } else if (socksInput.getQuantity() == socksMap.get(sock)) {
                socksMap.remove(sock);
                operations.add(new Operation("Отпуск", socksInput.getQuantity(), sock));
                saveReportToFile();
                saveToFile();
                return "На складе больше нет такого товара";
            } else {
                socksMap.put(sock, (socksMap.get(sock) - socksInput.getQuantity()));
                operations.add(new Operation("Отпуск", socksInput.getQuantity(), sock));
                saveReportToFile();
                saveToFile();
                return "Удалось отпустить товар со склада";
            }
        } else {
            throw new WrongSocksException("Нет такого товара");
        }
    }

    @Override
    public void saveToFile() {
        List<SocksAccounting> fileList = new ArrayList<>();
        for (Sock socks : socksMap.keySet()
        ) {
            fileList.add(new SocksAccounting(socks, socksMap.get(socks)));
        }
        try {
            String json = new ObjectMapper().writeValueAsString(fileList);
            filesService.saveToFile(json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new FileWriteException("Ошибка записи в файл");
        }
    }

    @Override
    public void saveReportToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(operations);
            filesService.saveReportFile(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new FileWriteException("Ошибка записи отчета в файл");
        }
    }

    @Override
    public void readFromFile() {
        try {
            socksMap.clear();
            String json = filesService.readFromFile();
            List<SocksAccounting> accountingArrayList = new ObjectMapper().readValue(json,
                    new TypeReference<ArrayList<SocksAccounting>>() {
                    });
            for (SocksAccounting socks : accountingArrayList
            ) {
                socksMap.put(socks.getSocks(), socks.getQuantity());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new FileReadException("Ошибка чтения из файла");
        }
    }
    @Override
    public void readReportFile() {
        try {
            operations.clear();
            String json = filesService.readReportFromFile();
            operations = new ObjectMapper().readValue(json, new TypeReference<ArrayList<Operation>>(){
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new FileReadException("Ошибка чтения из файла");
        }
    }

}
