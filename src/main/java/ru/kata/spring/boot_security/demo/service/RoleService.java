package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Role findByIdRoles(Long id) {
        return roleRepository.getOne(id);
    }

    @PostConstruct
    public void addDefaultRole() {
        roleRepository.save(new Role(1L,"ROLE_USER"));
        roleRepository.save(new Role(2L,"ROLE_ADMIN"));
    }
}
