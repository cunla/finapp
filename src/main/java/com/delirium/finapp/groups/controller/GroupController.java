package com.delirium.finapp.groups.controller;

import com.delirium.finapp.exceptions.FinappException;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.users.service.UserService;
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
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/groups/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Group>> findAllGroups() {
        List<Group> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Group>> findUserGroups() {
        List<Group> groups = groupService.findAllForUser(userService.findCurrentUser());
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Group> findAccount(@PathVariable("id") Long id) {
        Group group = groupService.findById(id);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (group.getUsers() == null) {
            throw new FinappException("Data broken -- group with no users!");
        }
        if (!group.getUsers().contains(userService.findCurrentUser())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups", params = {
        "systemId"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> createAccount(@RequestBody Group group) {
        Group sameGroup = groupService.findByName(group.getName());
        if (sameGroup != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Group createdGroup = groupService.create(group);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION,
            "groups/" + createdGroup.getId());
        return new ResponseEntity<>(createdGroup, httpHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Group> updateAccount(@RequestBody Group group,
                                               @PathVariable("id") Long id) {
        Group existingGroup = groupService.findById(id);
        if (existingGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (existingGroup.getAdmin() != userService.findCurrentUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Group sameGroup = groupService.findByName(group.getName());
        if (sameGroup != null && sameGroup.getId() != existingGroup.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        group.setId(id);
        Group updatedGroup = groupService.update(group);
        return new ResponseEntity<>(updatedGroup, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") Long id) {
        Group group = groupService.findById(id);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (group.getAdmin() != userService.findCurrentUser()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        groupService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
