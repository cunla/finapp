package com.delirium.finapp.users.controller;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/users", params = {
        "query"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<User>> findUsers(@RequestParam("query") String query) {
        List<User> users = userService.findUsers(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{groupId}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Group> findGroup(@PathVariable("groupId") Long groupId) {
        Group group = groupService.findById(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!group.hasUser(userService.findCurrentUser())) {
            return new ResponseEntity<Group>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/current-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> findCurrentUser() {
        User currentUser = userService.findCurrentUser();
        if (null == currentUser) {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> findUser(@PathVariable("id") Long id) {
        User user = userService.findUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User findUser = userService.findUserByEmail(user.getEmail());
        if (null != findUser) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User sameLoginUser = null;
        try {
            sameLoginUser = userService.findUserByEmail(user.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UsernameNotFoundException e) {
            User createdUser = userService.createUser(user);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, "users/" + createdUser.getId());
            return new ResponseEntity<>(createdUser, httpHeaders, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/facebookuser", params = {
        "accountId"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> facebookUser(@RequestBody User user,
                                             @RequestParam("accountId") Long accountId) {
        try {
            User findUser = userService.findUserByEmail(user.getEmail());
            userService.setCurrentUser(findUser);
            return new ResponseEntity<>(findUser, new HttpHeaders(), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            User createdUser = userService.createFacebookUser(user, accountId);
            userService.setCurrentUser(user);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, "users/" + createdUser.getId());
            return new ResponseEntity<>(createdUser, httpHeaders, HttpStatus.CREATED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        if (user.getId() != null) {
            if (!user.getId().equals(id)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            User existingUser = userService.findUser(user.getId());
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }
        User loggedUser = userService.findCurrentUser();
        if (!loggedUser.getId().equals(user.getId())
            && !loggedUser.getAuthorities().contains("ADMIN")
            ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        User user = userService.findUser(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/users/login/{login}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<HttpStatus> deleteUserByLogin(@PathVariable("login") String login) {
        User user = userService.findUserByEmail(login);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
