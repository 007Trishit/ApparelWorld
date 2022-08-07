package com.tmg.ApparelStore.dao.service;

import org.springframework.stereotype.Service;

import com.tmg.ApparelStore.dao.repository.ApparelRepository;
import com.tmg.ApparelStore.dao.repository.PurchaseRepository;
import com.tmg.ApparelStore.dao.repository.UserRepository;
import com.tmg.ApparelStore.model.Purchase;

import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final ApparelRepository apparelRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(ApparelRepository apparelRepository, UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.apparelRepository = apparelRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase purchase(UUID apparelId, String username) {
        Purchase purchase = new Purchase();
        purchase.setApparel(apparelRepository.getById(apparelId));
        purchase.setUser(userRepository.findByEmail(username));
        return purchaseRepository.save(purchase);
    }
}
