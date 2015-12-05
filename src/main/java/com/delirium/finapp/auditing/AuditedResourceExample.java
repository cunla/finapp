package com.delirium.finapp.auditing;

import com.delirium.finapp.auditing.annotations.Audited;
import com.delirium.finapp.auditing.annotations.AuditedObject;
import com.delirium.finapp.auditing.annotations.AuditedResult;
import com.delirium.finapp.auditing.annotations.AuditedSubject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Daniel Moran on 11/23/2015.
 */
@RestController
@RequestMapping("/sample")
public class AuditedResourceExample {

    @Audited
    @RequestMapping(value = "/audittest/{subject}",
        method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public
    @AuditedResult("x")
    ResponseEntity<List<String>> findSystems(
        @PathVariable("subject") @AuditedSubject("x") String subject,
        @RequestParam(value = "object", required = false) @AuditedObject("x") String object) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
