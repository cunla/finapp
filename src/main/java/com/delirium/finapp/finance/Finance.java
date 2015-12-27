package com.delirium.finapp.finance;

import com.delirium.finapp.finance.domain.Transaction;
import com.delirium.finapp.finance.domain.TransactionRepository;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by morand3 on 12/23/2015.
 */
@Controller
public class Finance {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepo;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findGroupTransactions(@PathVariable("group") Long groupId) {
        User user = userService.findCurrentUser();
        Group group = groupService.findById(groupId);
        if (group.hasUser(user)) {
            Page<Transaction> res = transactionRepo.findAllForGroup(group, new PageRequest(1, 20));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
