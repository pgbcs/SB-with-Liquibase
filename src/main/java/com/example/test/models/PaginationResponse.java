package com.example.test.models;

import java.util.List;

public class PaginationResponse<T> extends BaseResponse{
    private int page_number;
    private int page_size;
    private long count_items;
    private int count_pages;
    public PaginationResponse(int page_number, int page_size, int count_pages, long count_items, int s, String m, T d){
        super(s, m, d);
        this.page_number = page_number;
        this.page_size = page_size;
        this.count_pages = count_pages;
        this.count_items = count_items;
    }

    public long getCount_items() {
        return count_items;
    }

    public int getCount_pages() {
        return count_pages;
    }

    public int getPage_number() {
        return page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public static <T> PaginationResponse<T> success(T data, int page_number, int page_size, int count_pages, long count_items, String msg){
        return new PaginationResponse<T>(page_number, page_size, count_pages, count_items, 200,msg, data);
    }


    public static <T> PaginationResponse<T> paginationError(int status, String msg){
        return new PaginationResponse<T>(0, 0, 0,0,status, msg, null);
    }
}
