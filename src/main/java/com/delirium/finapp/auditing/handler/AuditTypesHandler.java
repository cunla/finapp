package com.delirium.finapp.auditing.handler;

import com.delirium.finapp.auditing.AuditEntry;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;

/**
 * Created by style on 01/12/2015.
 */
public interface AuditTypesHandler {
    void writeAuditData(Date date, String username, ProceedingJoinPoint joinPoint,
                        Object resultValue);

    AuditEntry createAuditEntry(Date date, String username, ProceedingJoinPoint joinPoint,
                                Object resultValue);

    String getAuditText(String paramType, Object paramValue);
}
