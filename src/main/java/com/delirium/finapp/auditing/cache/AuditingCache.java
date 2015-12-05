package com.delirium.finapp.auditing.cache;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Daniel Moran on 11/25/2015.
 */
public class AuditingCache {
    private static AuditingCache ins;
    private Map<Method, AuditedMethodDescriptor> cache;

    private AuditingCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public static AuditingCache instance() {
        if (null == ins) {
            ins = new AuditingCache();
        }
        return ins;
    }

    public AuditedMethodDescriptor getAuditDescriptor(Method method) {
        AuditedMethodDescriptor auditedMethodDescriptor = cache.get(method);
        if (null == auditedMethodDescriptor) {
            auditedMethodDescriptor = new AuditedMethodDescriptor(method);
            cache.put(method, auditedMethodDescriptor);
        }
        return auditedMethodDescriptor;
    }
}
