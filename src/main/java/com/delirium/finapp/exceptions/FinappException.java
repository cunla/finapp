package com.delirium.finapp.exceptions;

/**
 * Created by Daniel Moran on 1/14/2015.
 */
public class FinappException extends RuntimeException {
    public FinappException(String msg) {
        super(msg);
    }

    public FinappException(Exception e) {
        super(e);
    }
}
