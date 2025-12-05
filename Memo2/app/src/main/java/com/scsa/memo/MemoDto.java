package com.scsa.memo;

import java.io.Serializable;
import java.util.Date;

public class MemoDto implements Serializable {
    private String title;
    private String body;
    private long regDate;

    public MemoDto() {
    }

    public MemoDto(String t, String b) {
        title = t;
        body = b;
        regDate = (new Date()).getTime();
    }

//    TODO:
//    public MemoDto(String t, String b, String dateString) {
//        title = t;
//        body = b;
//        regDate = dateString;
//    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public long getRegDate() {
        return regDate;
    }

    public void setTitle(String t) {
        title = t;
    }

    public void setBody(String b) {
        body = b;
    }

    public void setRegDate(long r) {
        regDate = r;
    }
}
