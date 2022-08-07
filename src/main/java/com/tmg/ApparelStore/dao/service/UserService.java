package com.tmg.ApparelStore.dao.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tmg.ApparelStore.dto.UserDto;
import com.tmg.ApparelStore.model.Purchase;
import com.tmg.ApparelStore.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User save(UserDto dto);
    public User saveAdmin(UserDto dto);
    public boolean isRegistered(UserDto dto);
    public List<Purchase> getPurchases(String email);
    public String getRole(String email);
}
