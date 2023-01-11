package me.fevralev.socksapp.service.Impl;

import me.fevralev.socksapp.exception.FileUploadException;
import me.fevralev.socksapp.exception.FileWriteException;
import me.fevralev.socksapp.service.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("${name.of.data.file}")
    private String dataFileName;
    @Value("${name.of.report.file}")
    private String reportFileName;
    @Value("${kByte}")
    private int KBYTE;

    @Override
    public boolean cleanDataFile() {
        try {
            Path path = Path.of(dataFilePath, dataFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cleanReportFile() {
        try {
            Path path = Path.of(dataFilePath, reportFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public File getDataFile() {
        return new File(dataFilePath + "/" + dataFileName);
    }
    @Override
    public File getReportFile() {
        return new File(dataFilePath + "/" + reportFileName);
    }

    @Override
    public boolean saveToFile(String json) {
        try {
            cleanDataFile();
            Files.writeString(Path.of(dataFilePath, dataFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileWriteException("Ошибка записи в файл");
        }
    }

    @Override
    public String readFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileWriteException("Ошибка чтения из файла");
        }
    }
    @Override
    public String readReportFromFile() {
        try {
            return Files.readString(Path.of(dataFilePath, reportFileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileWriteException("Ошибка чтения из файла");
        }
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        Path filePath = Path.of(dataFilePath, dataFileName);
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, KBYTE);
             BufferedOutputStream bos = new BufferedOutputStream(os, KBYTE);
        ) {
            bis.transferTo(bos);
        }
        catch (IOException e) {
            throw new FileUploadException("Ошибка выгрузки файла");
        }
    }
    @Override
    public boolean saveReportFile(String json) {
        try{
            cleanReportFile();
            Files.writeString(Path.of(dataFilePath, reportFileName), json);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            throw new FileWriteException("Ошибка записи в файл отчета");
        }


    }
}
