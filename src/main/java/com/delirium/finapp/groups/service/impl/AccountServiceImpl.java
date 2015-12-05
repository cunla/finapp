package com.delirium.finapp.groups.service.impl;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.repository.AccountRepository;
import com.delirium.finapp.groups.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @PersistenceContext(unitName = "finapp")
    @Qualifier("entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<Group> findAll() {
        List<Group> groups = accountRepository.findAll();
        return groups;
    }

    @Override
    public Group findById(Long id) {
        Group group = accountRepository.findOne(id);
        return group;
    }

    @Override
    public Group findByName(String name) {
        Group group = accountRepository.findOneByName(name);
        return group;
    }

    @Override
    @Transactional
    public Group create(Group group, Long systemId) {
        entityManager.persist(group);
        entityManager.flush();
        Group newGroup = entityManager.merge(group);
        entityManager.flush();
        return newGroup;
    }

    @Override
    public Group update(Group group) {
        Group existingGroup = accountRepository.findOne(group.getId());
        existingGroup.setName(group.getName());
        existingGroup.setLabel(group.getLabel());
        Group updatedGroup = accountRepository.save(existingGroup);
        return updatedGroup;
    }

    @Override
    public void delete(Long id) {
        accountRepository.delete(id);

    }

}
