package me.fevralev.socksapp.model;

public enum Color {
    WHITE("белый"), BLACK("черный"), BLUE("синий"), RED("красный"), YELLOW("желтый"), GREEN("зеленый");
    private String text;

    Color(String text) {
        this.text = text;
    }

    public String getColor() {
        return text;
    }
    }
