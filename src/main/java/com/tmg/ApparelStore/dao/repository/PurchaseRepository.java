package com.tmg.ApparelStore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmg.ApparelStore.model.Purchase;
import com.tmg.ApparelStore.model.User;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser(User user);
}