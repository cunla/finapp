package com.delirium.finapp.exceptions;

import java.io.IOException;

/**
 * Created by style on 22/12/2015.
 */
public class FinappUrlException extends Throwable {
    private final String url;

    public FinappUrlException(String msg, String url, IOException e) {
        super(msg, e);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
