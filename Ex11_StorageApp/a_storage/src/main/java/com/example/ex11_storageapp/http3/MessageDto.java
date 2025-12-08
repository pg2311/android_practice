package com.example.ex11_storageapp.http3;

public class MessageDto {
    private int id;

    private String message;

    public MessageDto(int id, String message) {
        this.id = id;
        this.message = message;
    }
    public MessageDto(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
