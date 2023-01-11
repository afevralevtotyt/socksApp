package me.fevralev.socksapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.fevralev.socksapp.model.*;
import me.fevralev.socksapp.service.SockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
public class SocksController {
    private final SockService service;

    public SocksController(SockService service) {
        this.service = service;
    }

    @Tag(name = "Регистрирует отпуск носков со склада")
    @Operation(description = "Введите данные в формате JSON")
    @PutMapping(value = "socks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалось произвести отпуск носков со склада", content = @Content(
                    mediaType = "text/plain")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<String> editSocks(@RequestBody SockInput socksInput) {
        String editResult = service.edit(socksInput);
        if (editResult.isEmpty()) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().body(editResult);
    }

    @Tag(name = "Регистрирует приход товара на склад")
    @Operation(description = "Введите данные в формате JSON: color(белый, черный, красный, синий, желтый), " +
            "size(36-49), cottonPart(0-100), quantity(количество пар)")
    @PostMapping(value = "socks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалось добавить приход", content = @Content(
                    mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<SockInput> addSocks(@RequestBody SockInput socksInput) {
        SockInput addedSocks = service.add(socksInput);
        if (addedSocks == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().body(addedSocks);
    }

    @Tag(name = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.")
    @Operation(description = "Введите параметры запроса: color(белый, черный, красный, синий, желтый), " +
            "size(36-49), cottonMax(0-100), cottonMin(0-100)")
    @GetMapping(value = "socks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен", content = @Content(
                    mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Данного товара на складе нет", content = @Content(
                    mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<Integer> getSocks(@RequestParam String color, @RequestParam int size,
                                        @RequestParam(required = false, defaultValue = "0") int cottonMin,  @RequestParam(required = false, defaultValue = "100") int cottonMax) {
        int sum = service.getSocks(color, size, (int) cottonMin, (int) cottonMax);
        if (sum==0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(sum);
    }

    @Tag(name = "Регистрирует списание испорченных (бракованных) носков")
    @Operation(description = "Введите данные в формате JSON: color(белый, черный, красный, синий, желтый), " +
            "size(36-49), cottonPart(0-100), quantity(количество пар)")
    @DeleteMapping(value = "socks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, товар списан со склада", content = @Content(
                    mediaType = "application/json")),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    })
    public ResponseEntity<SockInput> deleteSocks(@RequestBody SockInput socksInput) {
        SockInput addedSocks = service.delete(socksInput);
        if (addedSocks == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().body(addedSocks);
    }

}