package com.delirium.finapp.finance;

import com.delirium.finapp.finance.domain.Account;
import com.delirium.finapp.finance.domain.AccountRepository;
import com.delirium.finapp.finance.domain.Category;
import com.delirium.finapp.finance.domain.CategoryRepository;
import com.delirium.finapp.groups.domain.Group;

import java.util.List;

/**
 * Created by style on 02/01/2016.
 */
public class GroupSettings {
    private List<Account> accounts;
    private List<Category> categories;

    public GroupSettings(Group group, AccountRepository accRepo, CategoryRepository catRepo){
        this.accounts = accRepo.accountsForGroup(group);
        this.categories = catRepo.categoryForGroup(group);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
