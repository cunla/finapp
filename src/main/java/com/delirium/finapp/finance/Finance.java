package com.delirium.finapp.finance;

import com.delirium.finapp.finance.domain.*;
import com.delirium.finapp.finance.protocol.TransPojo;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.tools.PlacesService;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlacesService placesService;
    @Autowired
    private CategoryRepository categoryRepository;

    public Group authorized(Long groupId) {
        User user = userService.findCurrentUser();
        Group group = groupService.findById(groupId);
        if (group.hasUser(user)) {
            userService.updateLastGroupId(user, groupId);
            return group;
        }
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/transactions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findGroupTransactions(@PathVariable("group") Long groupId) {
        Group group = authorized(groupId);
        if (null != group) {
            Page<Transaction> res = transactionRepo.findAllForGroup(group, null);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/transactions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> newTransation(@PathVariable("group") Long groupId,
                                                     @RequestBody TransPojo t) {
        if (null == t || null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        Transaction transaction = new Transaction(group, t.getAmount(), t.getLocation().longitude,
            t.getLocation().latitude, t.getDate());
        placesService.saveNearByLocations(t.getLocation().latitude, t.getLocation().longitude);
        transactionRepo.save(transaction);
        transactionRepo.flush();
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transactions/{transactionId}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> getTransation(@PathVariable("transactionId") Long tId) {
        if (null == tId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction t = transactionRepo.findOne(tId);
        if (null == t) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (null == authorized(t.getGroupId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        t.setServices(categoryRepository, accountRepository, null);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transactions/{transactionId}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> updateTransation(@PathVariable("transactionId") Long tId,
                                                        @RequestBody Transaction t) {
        if (null == t || t.getId() != tId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction exisitingTransaction = transactionRepo.findOne(tId);
        if (null == authorized(exisitingTransaction.getGroupId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        exisitingTransaction.updateTransation(t);
        transactionRepo.save(exisitingTransaction);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transactions/{transactionId}", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> deleteTransation(@PathVariable("transactionId") Long tId) {
        if (null == tId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Transaction t = transactionRepo.findOne(tId);
        if (null == authorized(t.getGroupId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        transactionRepo.delete(tId);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{group}/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Category> newCategory(@PathVariable("group") Long groupId,
                                                @RequestBody Category c) {
        if (null == c || null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        Category category = new Category(group, c.getColor(), c.getName());
        categoryRepository.save(category);
        categoryRepository.flush();
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{group}/settings", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GroupSettings> getGroupSettings(@PathVariable("group") Long groupId) {
        if (null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        GroupSettings settings = new GroupSettings(group, accountRepository, categoryRepository);
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

}
