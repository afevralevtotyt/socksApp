package me.fevralev.socksapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.fevralev.socksapp.validator.Color;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@RequiredArgsConstructor
public class SockInput {
@Color
private String color;
@Min(value = 36, message = "Неверное размер, введите значение 36-49")
@Max(value = 49, message = "Неверное содержание хлопка")
    private int size;
@Min(value = 0, message = "Неверное содержание хлопка")
@Max(value = 100, message = "Неверное содержание хлопка")
    private int cottonPart;
@Min(value = 1, message = "Неверное количество товара")
    private int quantity;
}
