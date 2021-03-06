package com.delirium.finapp.finance.domain;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.images.FinImage;
import com.delirium.finapp.tools.PlacesService;
import com.delirium.finapp.users.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by morand3 on 12/23/2015.
 */
@Entity
@Table(name = "F_TRANSACTIONS")
public class Transaction {
    private static final Logger log = LoggerFactory.getLogger(Transaction.class);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Transient
    private AccountRepository accountRepository;
    @Transient
    private CategoryRepository categoryRepository;
    @Transient
    private PlacesService placesService;
    @Id
    @Column(name = "TRANSACTION_ID")
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonIgnore
    private Group group;
    @Column
    private String title;
    @Column
    private String target;
    @Column
    private Double amount;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @JsonIgnore
    private User createdBy;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @JsonIgnore
    private Category category;
    @Column
    private String comment;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn
    private Location location;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @JsonIgnore
    private Account account;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    @JsonIgnore
    private FinImage image;
    @JsonProperty("categoryId")
    @Transient
    private Long categoryId;
    @JsonProperty("accountId")
    @Transient
    private Long accountId;

    public Transaction() {
    }

    public Transaction(Group group, User user, Double amount, Location location, Date date) {
        this.group = group;
        this.createdBy = user;
        this.amount = amount;
        this.date = date;
        this.title = "TBD";
        this.location = location;
    }

    public Transaction(Group group, User user, Account account, Category category, Location location, Date date, String title, Double amount) {
        this.group = group;
        this.createdBy = user;
        this.account = account;
        this.category = category;
        this.location = location;
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    public static String csvTitle() {
        return "group,amount,date,title,target,category,account,location\n";
    }

    public Long getId() {
        return id;
    }

    public String getCategoryColor() {
        return (null == category) ? "black" : category.getColor();
    }

    public void setCategoryColor(String color) {
    }

    public String getCategoryIcon() {
        return (null == category) ? "ion-record" : category.getIcon();
    }

    public Long getCategoryId() {
        return (null == category) ? this.categoryId : category.getId();
    }

    public void setCategoryId(Long id) {
        this.categoryId = id;
    }

    public String getCategoryName() {
        return (null == category) ? null : category.getName();
    }

    public void setCategoryName(String name) {
    }

    public String getAccountName() {
        return (null == account) ? null : account.getName();
    }

    public Long getAccountId() {
        return (null == account) ? this.accountId : account.getId();
    }

    public void setAccountId(Long id) {
        this.accountId = id;
    }

    public Long getGroupId() {
        return (null == group) ? null : group.getId();
    }

    public void setGroup(Group group) {
        if (null == this.group || !this.group.equals(group)) {
            this.group = group;
        }
    }

    public void setServices(CategoryRepository categoryRepository,
                            AccountRepository accountRepository,
                            PlacesService placesService) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.placesService = placesService;
    }

    @JsonProperty("groupCategories")
    public List<Category> getGroupCategories() {
        return (null == categoryRepository) ? null : categoryRepository.categoryForGroup(this.group);
    }

    public void setGroupCategories(List<Category> categories) {
    }

    @JsonProperty("groupAccounts")
    public List<Account> getGroupAccounts() {
        return (null == accountRepository) ? null : accountRepository.accountsForGroup(this.group);
    }

    public void setGroupAccounts(List<Account> accounts) {
    }

    @JsonProperty
    public String getCreatedBy() {
        return (null == createdBy) ? null : createdBy.getName();
    }

    @JsonProperty("places")
    public void setPlaces(List<Location> places) {
        return;
    }

    @JsonProperty("places")
    public List<Location> getNearByPlaces() {
        return (null == placesService) ? null :
            placesService.getNearbyPlaces(location.getLatitude(), location.getLongitude());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Double getAmount() {
        return (amount == null) ? 0 : amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocationTitle() {
        return (null == this.location) ? "" : this.location.getName();
    }

    public Location getLocation() {
        if (null != placesService) {
//            log.debug("Returning location {}", this.location);
            return this.location;
        }
        return null;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getAccount() {
        return (null == account) ? null : account.getId();
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public FinImage getImage() {
        return image;
    }

    public void setImage(FinImage image) {
        if (null == this.image || !this.image.equals(image)) {
            this.image = image;
        }
    }

    public void updateTransation(Transaction t) {
        if (t.getTitle() != null) {
            this.setTitle(t.getTitle());
        }
        if (t.getTarget() != null) {
            this.setTarget(t.getTarget());
        }
        if (t.getAmount() != null) {
            this.setAmount(t.getAmount());
        }
        if (t.getDate() != null) {
            this.setDate(t.getDate());
        }
        if (t.getCategory() != null) {
            this.setCategory(t.getCategory());
        }
        if (t.getComment() != null) {
            this.setComment(t.getComment());
        }
        if (t.getLocation() != null) {
            this.setLocation(t.getLocation());
        }
        if (t.account != null) {
            this.account = t.account;
        }
        if (t.getImage() != null) {
            this.setImage(t.getImage());
        }
    }

    public String asCsv() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getGroupId());
        buf.append(",").append(this.amount);
        buf.append(",").append(dateFormat.format(this.date));
        buf.append(",").append(this.title);
        buf.append(",").append(this.target);
        buf.append(",").append(this.getCategoryName());
        buf.append(",").append(this.getAccountName());
        buf.append(",").append(this.location.getName());
        buf.append("\n");
        return buf.toString();
    }
}
