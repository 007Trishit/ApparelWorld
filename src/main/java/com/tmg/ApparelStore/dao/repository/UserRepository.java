package com.tmg.ApparelStore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmg.ApparelStore.model.Role;
import com.tmg.ApparelStore.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String s);
    List<User> findAllByRole(Role role);
}