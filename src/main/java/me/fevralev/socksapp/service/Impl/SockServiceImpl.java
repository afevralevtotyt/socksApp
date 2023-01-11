package me.fevralev.socksapp.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.fevralev.socksapp.exception.*;
import me.fevralev.socksapp.model.*;
import me.fevralev.socksapp.service.FilesService;
import me.fevralev.socksapp.service.SockService;
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
    private ArrayList<Operation> operations = new ArrayList<>();

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
        Sock sock = new Sock(findColorByName(socksInput.getColor()), findSizeByNumber(socksInput.getSize()), socksInput.getCottonPart());
        socksMap.put(sock, (socksMap.getOrDefault(sock, 0) + socksInput.getQuantity()));
        saveToFile();
        operations.add(new Operation("Приемка", socksInput.getQuantity(), sock));
        saveReportToFile();
        return socksInput;
    }

    @Override
    public int getSocks(@me.fevralev.socksapp.validator.Color String color, @Min(36) @Max(49) int size, @Min(0) @Max(100) int cottonMin, @Min(0)@Max(100) int cottonMax) {
        if (cottonMin > cottonMax) {
            throw new WrongCottonPartException("Введен неверный процент хлопка");
        }
        int sum = 0;
        for (Sock socks : socksMap.keySet()
        ) {
            if (socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax && socks.getColor() == findColorByName(color) && socks.getSize() == findSizeByNumber(size)) {
                sum += socksMap.get(socks);
            }
        }
        return sum;
    }

    @Override
    public SockInput delete(@Valid SockInput socksInput) {
        Sock socks = new Sock(findColorByName(socksInput.getColor()), findSizeByNumber(socksInput.getSize()), socksInput.getCottonPart());
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
        Sock sock = new Sock(findColorByName(socksInput.getColor()), findSizeByNumber(socksInput.getSize()), socksInput.getCottonPart());
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

    public static Size findSizeByNumber(int size) throws WrongSizeException {
        switch (size) {
            case 36:
                return Size.S36;
            case 37:
                return Size.S37;
            case 38:
                return Size.S38;
            case 39:
                return Size.S39;
            case 40:
                return Size.M40;
            case 41:
                return Size.M41;
            case 42:
                return Size.M42;
            case 43:
                return Size.M43;
            case 44:
                return Size.L44;
            case 45:
                return Size.L45;
            case 46:
                return Size.L46;
            case 47:
                return Size.XL47;
            case 48:
                return Size.XL48;
            case 49:
                return Size.XL49;
            default:
                throw new WrongSizeException("Введен неверный размер, введите число 36-49");
        }
    }

    public static Color findColorByName(String color) throws WrongColorException {
        switch (color) {
            case "белый":
                return Color.WHITE;
            case "синий":
                return Color.BLUE;
            case "черный":
                return Color.BLACK;
            case "красный":
                return Color.RED;
            case "зеленый":
                return Color.GREEN;
            case "желтый":
                return Color.YELLOW;
            default:
                throw new WrongColorException("Введен неверный цвет");
        }
    }
}
