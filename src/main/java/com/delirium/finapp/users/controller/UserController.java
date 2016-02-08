package com.delirium.finapp.users.controller;

import com.delirium.finapp.config.JView;
import com.delirium.finapp.exceptions.UserCreationException;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @JsonView(JView.UserSummary.class)
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User findUser = userService.findUserByEmail(user.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UsernameNotFoundException e) {
            User createdUser = null;
            try {
                createdUser = userService.createUser(user);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.LOCATION, "users/" + createdUser.getId());
                return new ResponseEntity<>(createdUser, httpHeaders, HttpStatus.CREATED);
            } catch (UserCreationException e1) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> register(@ModelAttribute("username") String email, @ModelAttribute("password") String password) {
        User user = new User(email, password);
        return register(user);
    }

    @RequestMapping(value = "/facebookuser",
        params = {"accountId"},
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> facebookUser(@RequestBody User user,
                                             @RequestParam("accountId") Long accountId) {
        User loginUser = null;
        try {
            loginUser = userService.findUserByEmail(user.getEmail());
            userService.setCurrentUser(loginUser);
            return new ResponseEntity<>(loginUser, new HttpHeaders(), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            loginUser = userService.createFacebookUser(user, accountId);
            userService.setCurrentUser(loginUser);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.LOCATION, "users/" + loginUser.getId());
            return new ResponseEntity<>(loginUser, httpHeaders, HttpStatus.CREATED);
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
        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserCreationException e) {
            log.warn("Couldn't create user, probably password issues");
            return new ResponseEntity<>(updatedUser, HttpStatus.BAD_REQUEST);
        }
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
