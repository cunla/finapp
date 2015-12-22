package com.delirium.finapp.exceptions;

import java.io.IOException;
import java.net.URL;

/**
 * Created by style on 22/12/2015.
 */
public class FinappUrlException extends Throwable {
    private final URL url;

    public FinappUrlException(String msg, URL url, IOException e) {
        super(msg, e);
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}
