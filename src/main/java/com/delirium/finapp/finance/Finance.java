package com.delirium.finapp.finance;

import com.delirium.finapp.finance.domain.*;
import com.delirium.finapp.finance.protocol.CategoryReport;
import com.delirium.finapp.finance.protocol.Period;
import com.delirium.finapp.finance.protocol.TransPojo;
import com.delirium.finapp.finance.protocol.TransactionsPage;
import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.service.GroupService;
import com.delirium.finapp.tools.PlacesService;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @RequestMapping(value = "/groups/{group}/export",
        method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public ResponseEntity<String> exportCsv(
        @PathVariable("group") Long groupId
    ) {
        Group group = authorized(groupId);
        if (null != group) {
            List<Transaction> transactions = transactionRepo.findAllForGroup(group);
            StringBuffer res = new StringBuffer(Transaction.csvTitle());
            for (Transaction t : transactions) {
                res.append(t.asCsv());
            }
            return new ResponseEntity<>(res.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/transactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findGroupTransactions(
        @PathVariable("group") Long groupId,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "pageSize", defaultValue = "30") Integer pageSize
    ) {
        Group group = authorized(groupId);
        if (null != group) {
            Pageable pageable = new PageRequest(page, pageSize);
            Page<Transaction> pageRes = transactionRepo.findAllForGroup(group, pageable);
            TransactionsPage res = new TransactionsPage(pageRes, pageable);
            res.setSumAll(transactionRepo.sumAllForGroup(group));
            res.setMissingData(transactionRepo.countTransactionsWithMissingData(group));
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/transactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
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
//        placesService.saveNearByLocations(t.getLocation().latitude, t.getLocation().longitude);
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
        t.setServices(categoryRepository, accountRepository, placesService);
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
        if (null != t.getAccountId() && exisitingTransaction.getAccountId() != t.getAccountId()) {
            t.setAccount(accountRepository.findOne(t.getAccountId()));
        }
        if (null != t.getCategoryId() && exisitingTransaction.getCategoryId() != t.getCategoryId()) {
            t.setCategory(categoryRepository.findOne(t.getCategoryId()));
        }
        exisitingTransaction.updateTransation(t);
        transactionRepo.save(exisitingTransaction);
        return new ResponseEntity<>(exisitingTransaction, HttpStatus.OK);
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

    @RequestMapping(value = "/groups/{group}/categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
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
        Category category = null;
        if (null == c.getId()) {
            category = new Category(group, c.getColor(), c.getIcon(), c.getName());
        } else {
            category = categoryRepository.findOne(c.getId());
            if (null == category) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            category.updateCategory(c);
        }
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

    @RequestMapping(value = "/groups/{group}/accounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Account> newAccount(@PathVariable("group") Long groupId,
                                              @RequestBody Account acc) {
        if (null == acc || null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Account account = null;
        if (null == acc.getId()) {
            account = new Account(group, acc.getColor(), acc.getIcon(), acc.getName(), acc.getStartingBalance());
        } else {
            account = accountRepository.findOne(acc.getId());
            if (null == account) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            account.updateAccount(acc);
        }
        accountRepository.save(account);
        accountRepository.flush();
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{group}/accounts/{accountId}/validate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Account> validateAccount(@PathVariable("group") Long groupId,
                                                   @PathVariable("accountId") Long accountId) {
        if (null == accountId || null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Account account = null;
        if (null == accountId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            account = accountRepository.findOne(accountId);
            account.setLastValidated(new Date());
        }
        accountRepository.save(account);
        accountRepository.flush();
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/{group}/accounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Account>> getAccounts(@PathVariable("group") Long groupId) {
        if (null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        List<Account> accounts = accountRepository.accountsForGroup(group);
        for (Account acc : accounts) {
            acc.setTransactionsRepo(transactionRepo);
        }
        Account noAcc = new Account();
        noAcc.setName("No account");
        noAcc.setBalance(transactionRepo.balanceWithoutAccount(group), transactionRepo.transactionsWithoutAcount(group));
        accounts.add(noAcc);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


    @RequestMapping(value = "/groups/{group}/categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CategoryReport> getCategories(@PathVariable("group") Long groupId,
                                                        @RequestBody Period period) {
        if (null == groupId) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Group group = authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        List<Category> categories = categoryRepository.categoryForGroup(group);
//        DateTime start = (null != period.getStart()) ? new DateTime(period.getStart()) : new DateTime(0);
//        DateTime end = (null != period.getEnd()) ? new DateTime(period.getEnd()) : new DateTime(new Date(Long.MAX_VALUE));
        Date start = (null != period.getStart()) ? period.getStart() : new Date(0);
        Date end = (null != period.getEnd()) ? period.getEnd() : new Date(Long.MAX_VALUE);
        for (Category category : categories) {
            category.setCategoryReport(transactionRepo, start, end);
        }
        Category noCat = new Category();
        noCat.setName("No category");
        noCat.setTotal(transactionRepo.totalWithoutCategory(group, start, end));
        categories.add(noCat);
        CategoryReport res = new CategoryReport(period, categories);
        res.setTransactionsWithoutCategory(transactionRepo.transactionsWithoutCategory(group, start, end));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
