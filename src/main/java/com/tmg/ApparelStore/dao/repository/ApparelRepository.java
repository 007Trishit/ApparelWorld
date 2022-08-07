package com.tmg.ApparelStore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmg.ApparelStore.model.Apparel;
import com.tmg.ApparelStore.model.Season;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApparelRepository extends JpaRepository<Apparel, UUID> {
    List<Apparel> findAllBySeason(Season season);
    Optional<Apparel> findById(UUID id);
    void deleteById(UUID id);
}