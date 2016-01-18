package com.delirium.finapp.auditing.cache;

import com.delirium.finapp.auditing.annotations.Audited;
import com.delirium.finapp.auditing.annotations.AuditedObject;
import com.delirium.finapp.auditing.annotations.AuditedResult;
import com.delirium.finapp.auditing.annotations.AuditedSubject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Moran on 11/23/2015.
 */
public class AuditedMethodDescriptor {
    private static final Logger log = LoggerFactory.getLogger(AuditedMethodDescriptor.class);
    private String name;
    //Map param index -> paramType
    private Map<Integer, String> objects;
    // subject param index ->  type
    private Pair<Integer, String> subject;
    //result type
    private String resultType;

    public AuditedMethodDescriptor(Method method) {
        this.name = getActionName(method);
        this.resultType = getResultType(method);
        log.debug("Creating descriptor for {}", this.name);
        this.objects = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getName();
            AuditedSubject subjectAnnotation = parameter.getAnnotation(AuditedSubject.class);
            if (subjectAnnotation != null) {
                log.debug("Descriptor {}: Added param {} as subject with type {}", this.name,
                    paramName, subjectAnnotation.value());
                this.subject = new ImmutablePair<>(i, subjectAnnotation.value());
            } else {
                AuditedObject objectAnnotation = parameter.getAnnotation(AuditedObject.class);
                if (null != objectAnnotation) {
                    log.debug("Descriptor {}: Added param {} as subject with type {}", this.name,
                        paramName, objectAnnotation.value());
                    this.objects.put(i, objectAnnotation.value());
                } else { //No Audited annotation on param
                    log.debug("Descriptor {}: Ignored param {}", this.name, paramName);
                }
            }
        }
    }

    private static String getActionName(Method method) {
        Audited auditAnnotation = method.getAnnotation(Audited.class);
        String action = auditAnnotation.action();
        if ("".equals(action)) {
            action = method.getName();
        }
        return action;
    }

    private static String getResultType(Method method) {
        AuditedResult resultAnnotation = method.getAnnotation(AuditedResult.class);
        return (null == resultAnnotation) ? null : resultAnnotation.value();
    }

    public Integer getSubjectIndex() {
        return subject.getKey();
    }

    public String getSubjectType() {
        return subject.getValue();
    }

    public String getResultType() {
        return resultType;
    }

    public Map<Integer, String> getObjects() {
        return objects;
    }

    public String getAction() {
        return name;
    }

    public boolean hasSubject() {
        return subject != null;
    }

    public boolean hasResult() {
        return null != resultType;
    }
}
