package com.delirium.finapp.groups.service;

import com.delirium.finapp.groups.domain.Group;
import com.delirium.finapp.groups.domain.GroupRepository;
import com.delirium.finapp.users.domain.User;
import com.delirium.finapp.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;


    @PersistenceContext(unitName = "finapp")
    @Qualifier("entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<Group> findAll() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }

    @Override
    public List<Group> findAllForUser(User user) {
        List<Group> groups = groupRepository.findAll();
        List<Group> res = new LinkedList<>();
        for(Group g:groups){
            if(g.hasUser(user)){
                res.add(g);
            }
        }
        return res;
    }

    @Override
    public Group findById(Long id) {
        Group group = groupRepository.findOne(id);
        return group;
    }

    @Override
    public Group findByName(String name) {
        Group group = groupRepository.findOneByName(name);
        return group;
    }

    @Override
    @Transactional("transactionManager")
    public Group create(Group group) {
        User user = userService.findCurrentUser();
        group.setAdmin(user);
        group.addUser(user);
        entityManager.persist(group);
        entityManager.flush();
        Group newGroup = entityManager.merge(group);
        entityManager.flush();
        return newGroup;
    }

    @Override
    public Group update(Group group) {
        Group existingGroup = groupRepository.findOne(group.getId());
        existingGroup.setName(group.getName());
        existingGroup.setLabel(group.getLabel());
        existingGroup.setAdmin(group.getAdmin());
        Group updatedGroup = groupRepository.save(existingGroup);
        return updatedGroup;
    }

    @Override
    public void delete(Long id) {
        groupRepository.delete(id);

    }

}
