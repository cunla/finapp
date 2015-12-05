package com.delirium.finapp.groups.controller;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/accounts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Group>> findAccounts() {
        List<Group> groups = accountService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Group> findAccount(@PathVariable("id") Long id) {
        Group group = accountService.findById(id);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/accounts", params = {
        "systemId"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> createAccount(@RequestBody Group group,
                                               @RequestParam("systemId") Long systemId) {
        Group sameGroup = accountService.findByName(group.getName());
        if (sameGroup != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Group createdGroup = accountService.create(group, systemId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION,
            "groups/system/" + systemId + "/account/" + createdGroup.getId());
        return new ResponseEntity<>(createdGroup, httpHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Group> updateAccount(@RequestBody Group group,
                                               @PathVariable("id") Long id) {
        Group existingGroup = accountService.findById(id);
        if (existingGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Group sameGroup = accountService.findByName(group.getName());
        if (sameGroup != null && sameGroup.getId() != existingGroup.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        group.setId(id);
        Group updatedGroup = accountService.update(group);
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") Long id) {
        Group group = accountService.findById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        accountService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
