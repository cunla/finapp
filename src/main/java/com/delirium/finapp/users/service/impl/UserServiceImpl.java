package com.delirium.finapp.users.service.impl;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.repository.AccountRepository;
import com.delirium.finapp.infra.common.auth.domain.AbstractCurrentUser;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.repository.UserRepository;
import com.delirium.finapp.users.service.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service public class UserServiceImpl implements UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private AccountRepository accountRepository;

    @PersistenceContext(unitName = "finapp") @Qualifier("entityManagerFactory")
    private EntityManager entityManager;

    @Value("${finapp.admin.login}") private String adminLogin;

    @Value("${finapp.admin.password}") private String adminPaswword;

    @PostConstruct public void init() {
        User existingAdmin = userRepository.findOneByEmail(adminLogin);
        if (existingAdmin == null) {
            User newAdmin = new User();
            newAdmin.setEmail(adminLogin);
            newAdmin.setEncodedPassword(adminPaswword);
            newAdmin.setPermission("ADMIN");
            newAdmin.setName("admin");
            updateAuditFields(newAdmin);
            userRepository.save(newAdmin);
        }
    }

    @Override public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override public AbstractCurrentUser findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AbstractCurrentUser currentUser = (AbstractCurrentUser) auth.getPrincipal();
        return currentUser;
    }

    @Override public User findUser(Long id) {
        User user = userRepository.findOne(id);
        return user;
    }

    @Override @Transactional("transactionManager")
    public User createUser(User user, Long accountId) {
        updateAuditFields(user);
        user.setEncodedPassword(user.getPassword());
        user.setPermission("USER");

        entityManager.persist(user);
        entityManager.flush();
        Group group = accountRepository.findOne(accountId);
        user.setGroup(group);
        User createdUser = entityManager.merge(user);
        entityManager.flush();
        return createdUser;
    }


/*	@Override
    public User createUser(User user, Long accountId) {
		updateAuditFields(user);
		user.setEncodedPassword(user.getPassword());
		user.setPermission("USER");
		User createdUser = userRepository.save(user);
		return createdUser;
	}*/

    @Override public User updateUser(User user) {
        User existingUser = userRepository.findOne(user.getId());
        if (existingUser == null) {
            return null;
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setEncodedPassword(user.getPassword());
        }
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    @Override public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override public User findUserByLogin(String email) {
        User user = userRepository.findOneByEmail(email);
        return user;
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    @Override @Transactional("transactionManager")
    public User createFacebookUser(User user, Long accountId) {
        updateAuditFields(user);
        user.setEncodedPassword(accountId.toString());
        user.setPermission("USER");

        entityManager.persist(user);
        entityManager.flush();
        Group group = accountRepository.findOne(accountId);
        user.setGroup(group);
        User createdUser = entityManager.merge(user);
        entityManager.flush();
        return createdUser;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public String getAdminPaswword() {
        return adminPaswword;
    }

    public void setAdminPaswword(String adminPaswword) {
        this.adminPaswword = adminPaswword;
    }

    void updateAuditFields(User user) {
        user.setCreatedBy("Register");
        user.setCreatedDate(new DateTime());
    }

}
