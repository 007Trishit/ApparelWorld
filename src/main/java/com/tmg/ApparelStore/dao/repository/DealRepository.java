package com.tmg.ApparelStore.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmg.ApparelStore.model.Deal;

import java.util.UUID;

public interface DealRepository extends JpaRepository<Deal, Long> {
    Deal findByApparel_Id(UUID id);
    void deleteByApparel_Id(UUID id);
}