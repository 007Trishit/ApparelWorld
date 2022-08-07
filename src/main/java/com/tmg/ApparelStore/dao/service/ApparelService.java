package com.tmg.ApparelStore.dao.service;

import java.util.List;
import java.util.UUID;

import com.tmg.ApparelStore.dto.ApparelDto;
import com.tmg.ApparelStore.model.Apparel;

public interface ApparelService {
    public Apparel save(ApparelDto dto, boolean flag);
    public List<Apparel> listApparel(String email);
    public List<Apparel> listApparel();
    public ApparelDto  oneApparel(UUID id);
    void deleteApparel(UUID id);
}
