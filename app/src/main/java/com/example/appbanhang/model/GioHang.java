package com.example.appbanhang.model;

public class  GioHang {
    int idsp;
    String tenSp;
    long giaSp;
    String hinhAnh;
    int soluong;

    public GioHang() {
    }

    public int getIdsp() {
        return idsp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public long getGiaSp() {
        return giaSp;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public void setGiaSp(long giaSp) {
        this.giaSp = giaSp;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
