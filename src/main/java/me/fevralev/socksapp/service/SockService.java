package me.fevralev.socksapp.service;

import me.fevralev.socksapp.model.Sock;
import me.fevralev.socksapp.model.SockInput;
import me.fevralev.socksapp.validator.ColorValid;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;

public interface SockService {
    Map<Sock, Integer> getSocksMap();

    SockInput add(@Valid SockInput socksInput);

    int getSocks(@ColorValid String color, @Min(36) @Max(49) int size, @Min(0) @Max(100) int cottonMin, @Min(0)@Max(100) int cottonMax);

    SockInput delete(@Valid SockInput socksInput);

    String edit(@Valid SockInput socksInput);

    void saveToFile();

    void saveReportToFile();

    void readFromFile();

    void readReportFile();
}
