package com.hdpsolution.quanlychitieu.model;

public class ThuChi {
    private int id;
    private String note;
    private String danhmuc;
    private String number;
    private String date;
    private int type;
    private int typeSpiner;


    public ThuChi() {
    }

    public ThuChi(String note, String danhmuc, String number, String date, int type, int typeSpiner) {
        this.note = note;
        this.danhmuc = danhmuc;
        this.number = number;
        this.date = date;
        this.type = type;
        this.typeSpiner = typeSpiner;
    }

    public ThuChi(int id, String note, String danhmuc, String number, String date, int type, int typeSpiner) {
        this.id = id;
        this.note = note;
        this.danhmuc = danhmuc;
        this.number = number;
        this.date = date;
        this.type = type;
        this.typeSpiner = typeSpiner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDanhmuc() {
        return danhmuc;
    }

    public void setDanhmuc(String danhmuc) {
        this.danhmuc = danhmuc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeSpiner() {
        return typeSpiner;
    }

    public void setTypeSpiner(int typeSpiner) {
        this.typeSpiner = typeSpiner;
    }
}
