package com.tmg.ApparelStore.dao.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tmg.ApparelStore.dao.repository.PurchaseRepository;
import com.tmg.ApparelStore.dao.repository.RoleRepository;
import com.tmg.ApparelStore.dao.repository.UserRepository;
import com.tmg.ApparelStore.dto.UserDto;
import com.tmg.ApparelStore.model.Purchase;
import com.tmg.ApparelStore.model.Role;
import com.tmg.ApparelStore.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PurchaseRepository purchaseRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PurchaseRepository purchaseRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.purchaseRepository = purchaseRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User save(UserDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = new User(dto);
        Role role = Optional.ofNullable(roleRepository.findByName(ROLE_USER)).orElse(new Role(ROLE_USER));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User saveAdmin(UserDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = new User(dto);
        Role role = Optional.ofNullable(roleRepository.findByName(ROLE_ADMIN)).orElse(new Role(ROLE_ADMIN));
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public boolean isRegistered(UserDto dto) {
        return userRepository.findByEmail(dto.getEmail()) != null;
    }

    @Override
    public List<Purchase> getPurchases(String email) {
        return purchaseRepository.findAllByUser(userRepository.findByEmail(email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(Collections.singleton(user.getRole())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles)
    {
        return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


	@Override
	public String getRole(String email) {
		return userRepository.findByEmail(email).getRole().getName();
	}
}
