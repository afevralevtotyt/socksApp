package me.fevralev.socksapp.model;

import me.fevralev.socksapp.exception.WrongColorException;


public enum Color {
    WHITE("белый"),
    BLACK("черный"),
    BLUE("синий"),
    RED("красный"),
    YELLOW("желтый"),
    GREEN("зеленый");

    private String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public static Color colorOf(String inputColor) {
        for (Color value : Color.values()) {
            if (value.color.equals(inputColor)) {
                return value;
            }
        }
        return null;
    }

}