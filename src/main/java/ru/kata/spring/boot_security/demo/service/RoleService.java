package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    public Set<Role> findByIdRoles(List<Long> roles) {
        return new HashSet<>(roleRepository.findAllById(roles));
    }
    public Set<Role> findByIdRoles(Long id) {
        List<Long> roles  = new LinkedList<>();
        roles.add(id);
        return findByIdRoles(roles);
    }

    @PostConstruct
    public void addDefaultRole() {
        roleRepository.save(new Role(1L,"ROLE_USER"));
        roleRepository.save(new Role(2L,"ROLE_ADMIN"));
    }
}
