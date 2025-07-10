package com.example.test.models;

public class BaseResponse<T> {
    private int status;
    private String message;
    private T data;
    public BaseResponse(int s, String m, T d){
        this.status = s;
        this.message = m;
        this.data = d;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage(){
        return message;
    }

    public T getData(){
        return data;
    }

    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(200, "OK", data);
    }
    public static <T> BaseResponse<T> success(T data, String message){
        return new BaseResponse<T>(200, message, data);
    }
    public static <T> BaseResponse<T> error(int status, String message){
        return new BaseResponse<T>(400, message, null);
    }
}