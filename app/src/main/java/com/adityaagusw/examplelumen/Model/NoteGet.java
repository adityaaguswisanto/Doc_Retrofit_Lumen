package com.adityaagusw.examplelumen.Model;

import java.util.ArrayList;

public class NoteGet<T> {

    private boolean value;
    private String message;
    private ArrayList<T> data;

    public NoteGet(boolean value, String message, ArrayList<T> data) {
        this.value = value;
        this.message = message;
        this.data = data;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
