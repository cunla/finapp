package com.delirium.finapp.finance;

import com.delirium.finapp.exceptions.FinappException;
import com.delirium.finapp.finance.domain.*;
import com.delirium.finapp.groups.domain.Group;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by style on 16/01/2016.
 */
@Controller
public class FinanceTools {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String HEADER_ACCOUNT = "account".toUpperCase();
    private static final String HEADER_CATEGORY = "category".toUpperCase();
    private static final String HEADER_LOCATION = "location".toUpperCase();
    private static final String HEADER_DATE = "date".toUpperCase();
    private static final String HEADER_AMOUNT = "amount".toUpperCase();
    private static final String HEADER_TITLE = "title".toUpperCase();


    private static final Logger log = LoggerFactory.getLogger(FinanceTools.class);
    @Autowired
    private Finance finance;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private LocationRepository locationRepository;

    /**
     * Import data to group using CSV
     *
     * @param groupId - the group to import to
     * @param csv     - date, amount, title, location, account, category
     * @return number of transactions created
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/groups/{group}/uploadCsv",
        method = RequestMethod.POST,
        consumes = MediaType.TEXT_PLAIN_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Page<Transaction>> findGroupTransactions(@PathVariable("group") Long groupId,
                                                                   @RequestBody String csv) {
        Group group = finance.authorized(groupId);
        if (null == group) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        readData(group, csv);
        return new ResponseEntity<Page<Transaction>>(HttpStatus.OK);
    }

    private void readData(Group group, String csv) {
        String[] lines = csv.split("\n");

        Map<String, Integer> headers = getHeaders(lines[0]);
        Map<String, Account> accounts = new HashMap<>();
        Map<String, Category> categories = new HashMap<>();
        Map<String, Location> locations = new HashMap<>();
        Set<Transaction> transactions = new HashSet<>();
        for (int i = 1; i < lines.length; ++i) {
            try {
                String[] fields = lines[i].split(",");
                if (fields.length < headers.size()) {
                    throw new FinappException("Line doesn't have all fields");
                }
                Account account = genAccount(group, accounts, fields[headers.get(HEADER_ACCOUNT)]);
                Category category = genCategory(group, categories, fields[headers.get(HEADER_CATEGORY)]);
                Location location = genLocation(locations, fields[headers.get(HEADER_LOCATION)]);
                LocalDate date = LocalDate.parse(fields[headers.get(HEADER_DATE)]);
                Date date1 = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                String title = fields[headers.get(HEADER_TITLE)];
                Double amount = Double.valueOf(fields[headers.get(HEADER_AMOUNT)]);
                Transaction t = new Transaction(group, account, category, location, date1, title, amount);
                transactions.add(t);
            } catch (Exception e) {
                log.warn("Couldn't parse line {} : {}", i, lines[i]);
            }
        }
        accountRepository.save(accounts.values());
        categoryRepository.save(categories.values());
        locationRepository.save(locations.values());
        transactionRepository.save(transactions);
        log.debug("Imported {} transactions, {} accounts, {} categories, {} locations",
            transactions.size(), accounts.size(), categories.size(), locations.size());
    }

    private Location genLocation(Map<String, Location> locations, String name) {
        Location res = locations.get(name);
        if (null == res) {
            res = new Location(name);
            locations.put(name, res);
        }
        return res;
    }

    private Category genCategory(Group group, Map<String, Category> categories, String name) {
        Category res = categories.get(name);
        if (null == res) {
            List<Category> list = categoryRepository.findByName(group, name);
            if (null == list || list.isEmpty()) {
                res = new Category(group, name);
            } else {
                res = list.get(0);
            }
            categories.put(name, res);
        }
        return res;
    }

    private Account genAccount(Group group, Map<String, Account> accounts, String accountName) {
        Account res = accounts.get(accountName);
        if (null == res) {
            List<Account> list = accountRepository.findByName(group, accountName);
            if (null == list || list.isEmpty()) {
                res = new Account(group, accountName);
            } else {
                res = list.get(0);
            }
            accounts.put(accountName, res);
        }
        return res;
    }

    private Map<String, Integer> getHeaders(String headersLine) {
        String[] headersRow = headersLine.trim().split(",");
        Map<String, Integer> headers = new HashMap<>();
        for (int i = 0; i < headersRow.length; ++i) {
            headers.put(headersRow[i].toUpperCase(), i);
        }
        return headers;
    }
}
