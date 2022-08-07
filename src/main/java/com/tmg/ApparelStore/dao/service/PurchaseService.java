package com.tmg.ApparelStore.dao.service;

import java.util.UUID;

import com.tmg.ApparelStore.model.Purchase;

public interface PurchaseService {
    public Purchase purchase(UUID id, String username);
}
