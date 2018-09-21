package com.example.hanifkf.lostandfound.Models;

import java.util.Date;

/**
 * Created by M Taufiq R on 19/03/2018.
 */

public class Comments {

    private String user_id;
    private String comment;
    private Date timestamp;

    public Comments() {
    }

    public Comments(String user_id, String comment, Date timestamp) {
        this.user_id = user_id;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
