package com.delirium.finapp.exceptions;

/**
 * Created by Daniel Moran on 11/30/2015.
 */
public class FinappLoadingException extends FinappException {
    public FinappLoadingException(Exception e) {
        super(e);
    }

    public FinappLoadingException(String msg) {
        super(msg);
    }
}
