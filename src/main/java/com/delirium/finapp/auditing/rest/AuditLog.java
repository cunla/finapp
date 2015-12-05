package com.delirium.finapp.auditing.rest;

import com.delirium.finapp.auditing.AuditEntry;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.min;

/**
 * Created by Daniel Moran on 11/29/2015.
 */
public class AuditLog {
    private List<AuditEntry> log;
    private Integer totalPages;
    private Integer currentPage;

    public AuditLog() {
        log = new LinkedList<>();
    }

    public AuditLog(Integer page, Integer pageSize, List<AuditEntry> entries) {
        this.currentPage = page;
        this.totalPages = entries.size() / pageSize + ((entries.size() % pageSize == 0) ? 0 : 1);
        if (this.currentPage <= this.totalPages) {
            this.log = entries.subList((page - 1) * pageSize, min(page * pageSize, entries.size()));
        }
    }

    public List<AuditEntry> getLog() {
        return log;
    }

    public void setLog(List<AuditEntry> log) {
        this.log = log;
    }

    public void addAuditEntry(AuditEntry entry) {
        this.log.add(entry);
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
