package me.fevralev.socksapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FilesService {
    boolean cleanDataFile();

    boolean cleanReportFile();

    File getDataFile();

    File getReportFile();

    boolean saveToFile(String json);

    String readFromFile();

    String readReportFromFile();

    void uploadFile(MultipartFile file) throws IOException;

    boolean saveReportFile(String json);
}
