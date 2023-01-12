package me.fevralev.socksapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.fevralev.socksapp.exception.FileDownloadException;
import me.fevralev.socksapp.exception.FileUploadException;
import me.fevralev.socksapp.service.FilesService;
import me.fevralev.socksapp.service.SockService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("files")
public class FilesController {
    FilesService filesService;
    SockService sockService;

    public FilesController(FilesService filesService, SockService sockService) {
        this.filesService = filesService;
        this.sockService = sockService;
    }

    @Tag(name = "Выгрузить данные о товарах из файла JSON")
    @Operation(description = "Выберите файл с данными")
    @PostMapping(value = "/uploadSocks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Товар выгружен в приложение", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка выгрузки файла"
            )
    })
    public ResponseEntity<String> uploadRecipes(@RequestParam MultipartFile file) {

        try {
            filesService.uploadFile(file);
            sockService.readFromFile();
        } catch (IOException e) {
            throw new FileUploadException("Ошибка выгрузки файла");
        }

        return ResponseEntity.ok().build();
    }

    @Tag(name = "Загрузить данные о товарах в формате JSON")
    @Operation(description = "Нажмите Download file")
    @GetMapping(value = "/downloadSocks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл для загрузки готов", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет контента"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка скачивания файла"
            )
    })
    public ResponseEntity<Object> downloadSocks() {
        File file = filesService.getDataFile();
        if (file.exists()) {
            try {
                InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentLength(file.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socksData.json\"")
                        .body(inputStreamResource);
            } catch (FileNotFoundException e) {
                throw new FileDownloadException("Ошибка скачивания файла");
            }
        }
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "Загрузить данные об операциях с товаром в формате из JSON")
    @Operation(description = "Нажмите Download file")
    @GetMapping(value = "/downloadReport")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл для загрузки готов", content = @Content(
                    mediaType = "application/json"
            )),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет контента"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка скачивания файла"
            )
    })
    public ResponseEntity<Object> downloadReport() {
        File file = filesService.getReportFile();
        if (file.exists()) {
            try {
                InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentLength(file.length())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socksReport.json\"")
                        .body(inputStreamResource);
            } catch (FileNotFoundException e) {
                throw new FileDownloadException("Ошибка скачивания файла");
            }
        }
        return ResponseEntity.noContent().build();
    }
}
