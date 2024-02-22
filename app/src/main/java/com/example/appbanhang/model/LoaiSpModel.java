package com.example.appbanhang.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;

public class LoaiSpModel {
    boolean success;
    String message;
    List<sanpham> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<sanpham> getResult() {
        return result;
    }

    public void setResult(List<sanpham> result) {
        this.result = result;
    }

    public String getRawJson() {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(this);
        return gson.toJson(jsonElement);
    }
}
