package com.delirium.finapp.finance.protocol;

import com.delirium.finapp.finance.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by style on 18/01/2016.
 */
public class TransactionsPage extends PageImpl<Transaction> {

    private Double sumAll;
    private Integer missingData;

    public TransactionsPage(List<Transaction> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public TransactionsPage(Page<Transaction> page, Pageable pageable) {
        this(page.getContent(), pageable, page.getTotalElements());
    }

    public Double getSumAll() {
        return sumAll;
    }

    public void setSumAll(Double sumAll) {
        this.sumAll = sumAll;
    }

    public Integer getMissingData() {
        return missingData;
    }

    public void setMissingData(Integer missingData) {
        this.missingData = missingData;
    }
}
