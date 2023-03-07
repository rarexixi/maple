package org.xi.maple.common.models;

import java.io.Serializable;

public class ResponseError implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
