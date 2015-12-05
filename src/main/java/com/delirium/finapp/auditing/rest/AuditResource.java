package com.delirium.finapp.auditing.rest;

import com.delirium.finapp.auditing.AuditEntry;
import com.delirium.finapp.auditing.AuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel Moran on 11/29/2015.
 */
@RestController
@RequestMapping("/app")
public class AuditResource {
    private final Logger log = LoggerFactory.getLogger(AuditResource.class);

    @Inject
    AuditRepository auditRepository;

    @RequestMapping(value = "/audit/log", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditLog> findSystems(
        @RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "query", required = false) String text) {
        page = (null != page && page >= 0) ? page : 1;
        pageSize = (null != pageSize && pageSize > 0) ? pageSize : 1000;

        List<AuditEntry> log = auditRepository.findAll();
        List<AuditEntry> query = queryForText(log, text);
        AuditLog auditLog = new AuditLog(page, pageSize, query);
        return new ResponseEntity<AuditLog>(auditLog, HttpStatus.OK);
    }

    private List<AuditEntry> queryForText(List<AuditEntry> log, String query) {
        if (null == query) {
            return log;
        }
        List<AuditEntry> res = new LinkedList<>();
        for (AuditEntry entry : log) {
            if (entry.toString().contains(query)) {
                res.add(entry);
            }
        }
        return res;
    }
}
