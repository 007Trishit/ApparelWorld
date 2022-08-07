package com.tmg.ApparelStore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmg.ApparelStore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}