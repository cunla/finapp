package com.delirium.finapp.finance.protocol;

import com.delirium.finapp.finance.domain.Category;

import java.util.List;

/**
 * Created by morand3 on 1/14/2016.
 */
public class CategoryReport {
    private List<Category> categories;
    private Period period;
    private Integer transactionsWithoutCategory;

    public CategoryReport() {
    }

    public CategoryReport(Period period, List<Category> categories) {
        this.categories = categories;
        this.period = period;
    }

    public Integer getTransactionsWithoutCategory() {
        return transactionsWithoutCategory;
    }

    public void setTransactionsWithoutCategory(Integer transactionsWithoutCategory) {
        this.transactionsWithoutCategory = transactionsWithoutCategory;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
