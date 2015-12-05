package com.delirium.finapp.auditing;

import com.delirium.finapp.exceptions.FinappException;

/**
 * Created by Daniel Moran on 11/25/2015.
 */
public class AuditException extends FinappException {
    public AuditException(String msg) {
        super(msg);
    }
}
