package com.indiaawayday.salonivithalani.indiaawayday.scanner;

import java.io.Serializable;

/**
 * Created by bhupendrakumar on 7/17/16.
 */
public class Result implements Serializable {

    private String text;
    private int type;

    protected Result(int type, String text) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
