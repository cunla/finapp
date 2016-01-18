package com.delirium.finapp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Daniel Moran on 11/12/2015.
 */
public class ExceptionInfo {
    public final String url;
    public final String ex;
    public String[] stackTrace;

    public ExceptionInfo(String url, Exception ex) {
        this.url = url;
        this.ex = ex.getLocalizedMessage();
        this.stackTrace = ExceptionUtils.getStackTrace(ex).split("\n\t");
    }

    public ExceptionInfo(HttpServletRequest req, Exception e) {
        this(req.getRequestURI(), e);
    }

    @Override
    public String toString() {
        return "ExceptionInfo{" +
            "url='" + url + '\'' +
            ", ex='" + ex + '\'' +
            ", stackTrace=" + StringUtils.join(stackTrace, ",") +
            '}';
    }
}
