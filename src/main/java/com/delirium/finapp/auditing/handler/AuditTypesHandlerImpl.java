package com.delirium.finapp.auditing.handler;

import com.delirium.finapp.auditing.AuditEntry;
import com.delirium.finapp.auditing.AuditRepository;
import com.delirium.finapp.auditing.cache.AuditedMethodDescriptor;
import com.delirium.finapp.auditing.cache.AuditingCache;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Moran on 11/25/2015.
 */
public class AuditTypesHandlerImpl implements AuditTypesHandler {
    @Autowired
    private AuditRepository auditRepository;

    public AuditTypesHandlerImpl() {
    }

    @Override
    public void writeAuditData(Date date, String username, ProceedingJoinPoint joinPoint, Object resultValue) {
        AuditEntry entry = createAuditEntry(date, username, joinPoint, resultValue);
        auditRepository.save(entry);
        auditRepository.flush();
    }

    @Override
    public AuditEntry createAuditEntry(Date date, String username, ProceedingJoinPoint joinPoint,
                                       Object resultValue) {
        AuditedMethodDescriptor descriptor = getAuditMethodDescriptor(joinPoint);
        String actionName = descriptor.getAction();
        Object[] args = joinPoint.getArgs();
        String subject = getSubject(descriptor, args);
        String resultText = getResultText(descriptor, resultValue);
        String objects = getObjectsText(descriptor, args);
        AuditEntry auditEntry = new AuditEntry(date, username, actionName, subject, resultText,
            objects);
        return auditEntry;
    }

    private String getObjectsText(AuditedMethodDescriptor descriptor, Object[] args) {
        List<String> objectText = new LinkedList<>();
        Map<Integer, String> objectsType = descriptor.getObjects();
        for (Map.Entry<Integer, String> entry : objectsType.entrySet()) {
            String objectType = entry.getValue();
            Object objectValue = args[entry.getKey()];
            objectText.add(getAuditText(objectType, objectValue));
        }
        return StringUtils.join(objectText, ", ");
    }

    private String getResultText(AuditedMethodDescriptor descriptor, Object resultValue) {
        if (null == resultValue || !descriptor.hasResult()) {
            return null;
        }
        return getAuditText(descriptor.getResultType(), resultValue);
    }

    private String getSubject(AuditedMethodDescriptor descriptor, Object[] args) {
        String subject = null;
        if (descriptor.hasSubject()) {
            Object subjectValue = args[descriptor.getSubjectIndex()];
            subject = getAuditText(descriptor.getSubjectType(), subjectValue);
        }
        return subject;
    }

    private AuditedMethodDescriptor getAuditMethodDescriptor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuditedMethodDescriptor methodDescriptor = AuditingCache.instance()
            .getAuditDescriptor(method);
        return methodDescriptor;
    }

    @Override
    public String getAuditText(String paramType, Object paramValue) {
        return paramType + paramValue;

        //throw new AuditException("Couldn't get text for audit type {" + paramType + "}");
    }

}
