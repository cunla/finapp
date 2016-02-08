package com.delirium.finapp.users.service;

import com.delirium.finapp.exceptions.UserCreationException;
import com.delirium.finapp.groups.domain.GroupRepository;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.domain.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @PersistenceContext(unitName = "finapp")
    @Qualifier("entityManagerFactory")
    private EntityManager entityManager;

    @Value("${finapp.admin.login}")
    private String adminLogin;

    @Value("${finapp.admin.password}")
    private String adminPaswword;

    @PostConstruct
    public void init() {
        User existingAdmin = userRepository.findOneByEmail(adminLogin);
        if (existingAdmin == null) {
            try {
                User newAdmin = new User();
                newAdmin.setEmail(adminLogin);

                newAdmin.setEncodedPassword(adminPaswword);

                newAdmin.setPermission("ADMIN");
                newAdmin.setName("admin");
                newAdmin.setCreatedDate(new DateTime());
                newAdmin.setCreatedBy("system");
                userRepository.save(newAdmin);
            } catch (UserCreationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setCurrentUser(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        user.setLastLogin(DateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public List<User> findUsers(String query) {
        List<User> users = userRepository.findAllContaining(query);
        return users;
    }

    @Override
    public User findCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principalObj = auth.getPrincipal();
        if (principalObj instanceof User) {
            return (User) principalObj;
        }
        String principal = principalObj.toString();
        User currentUser = userRepository.findOneByEmail(principal);
        return (User) currentUser;
    }

    @Override
    public User findUser(Long id) {
        User user = userRepository.findOne(id);
        return user;
    }

    @Override
    @Transactional("transactionManager")
    public User createUser(User user) throws UserCreationException {
        updateAuditFields(user);
        user.setEncodedPassword(user.getPassword());
        user.setPermission("USER");

        entityManager.persist(user);
        entityManager.flush();
//        Group group = accountRepository.findOne(accountId);
//        user.setGroup(group);
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

    @Override
    public User updateUser(User user) throws UserCreationException {
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
        if (user.getCountry() != null) {
            existingUser.setCountry(user.getCountry());
        }
        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public User loadUserByUsername(String email) {
        User user = userRepository.findOneByEmail(email);
        if (null == user) {
            throw new UsernameNotFoundException(email);
        }
        return user;
    }

    public User findUserByEmail(String email) {
        return loadUserByUsername(email);
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    @Override
    @Transactional("transactionManager")
    public User createFacebookUser(User user, Long accountId) {
        updateAuditFields(user);
        user.setEncodedPasswordNoException(accountId.toString());
        user.setPermission("USER");

        entityManager.persist(user);
        entityManager.flush();
//        Group group = accountRepository.findOne(accountId);
//        user.setGroup(group);
        User createdUser = entityManager.merge(user);
        entityManager.flush();
        return createdUser;
    }

    @Override
    public void updateLastGroupId(User user, Long groupId) {
        if (user.getLastGroupId() != groupId) {
            user.setLastGroupId(groupId);
            userRepository.save(user);
        }
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
