package com.adityaagusw.examplelumen.Model;

import java.util.ArrayList;

public class NotePagination<T> {

    private int current_page;
    private ArrayList<T> data;

    public NotePagination(int current_page, ArrayList<T> data) {
        this.current_page = current_page;
        this.data = data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
