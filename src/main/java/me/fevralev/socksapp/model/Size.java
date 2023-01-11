package me.fevralev.socksapp.model;

public enum Size {
    S36(36), S37(37), S38(38), S39(39), M40(40), M41(41), M42(42), M43(43), L44(44), L45(45), L46(46), XL47(47), XL48(48), XL49(49);
    private final int SIZE;

    Size(int SIZE) {
        this.SIZE = SIZE;
    }

    public Size getSIZE(int SIZE) {
        return this;
    }

}
