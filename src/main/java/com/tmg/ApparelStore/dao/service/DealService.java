package com.tmg.ApparelStore.dao.service;


import java.util.UUID;


import com.tmg.ApparelStore.dto.DealDto;
import com.tmg.ApparelStore.model.Deal;

public interface DealService {
    public double getDiscountedPrice(UUID apparelId);
    public Deal save(DealDto dealDto,boolean flag);
    public DealDto oneDeal(UUID apparelId);
    void deleteDeal(UUID apparelId);
}
