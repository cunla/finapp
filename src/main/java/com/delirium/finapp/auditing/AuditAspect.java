package com.delirium.finapp.auditing;

import com.delirium.finapp.auditing.handler.AuditTypesHandler;
import com.delirium.finapp.users.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Daniel Moran on 8/30/2015.
 */

@Aspect
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    //    @Autowired
//    private AuditRepository auditRepository;
    @Autowired
    private AuditTypesHandler auditTypesHandler;

    @Autowired
    private UserService userService;

    @Around("@annotation(com.delirium.finapp.auditing.annotations.Audited)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Date date = new Date();
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            writeAuditToDb(date, joinPoint, result);
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            writeAuditToDb(date, joinPoint, "Exception" + e.getMessage());
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }

    @Transactional(value = "auditTransactionManager")
    private void writeAuditToDb(Date date, ProceedingJoinPoint joinPoint, Object result) {
        UserDetails user = userService.findCurrentUser();
        String username = user.getUsername();
        auditTypesHandler.writeAuditData(date, username, joinPoint, result);
    }

}
