package com.tmg.ApparelStore.dao.service;

import org.springframework.stereotype.Service;

import com.tmg.ApparelStore.dao.repository.ApparelRepository;
import com.tmg.ApparelStore.dao.repository.DealRepository;
import com.tmg.ApparelStore.dto.DealDto;
import com.tmg.ApparelStore.model.Apparel;
import com.tmg.ApparelStore.model.Deal;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final ApparelRepository apparelRepository;

    public DealServiceImpl(DealRepository dealRepository, ApparelRepository apparelRepository) {
        this.dealRepository = dealRepository;
        this.apparelRepository = apparelRepository;
    }

    @Override
    public double getDiscountedPrice(UUID apparelId) {

        double percentage = Optional.ofNullable(dealRepository.findByApparel_Id(apparelId))
                .map(deal -> {
                    if (deal.getValidUpto().isBefore(LocalDateTime.now())) {
                        dealRepository.delete(deal);
                        return 0.0;
                    } else if (deal.getValidFrom().isAfter(LocalDateTime.now())) {
                        return 0.0;
                    }
                    return deal.getPercentage();
                }).orElse(0.0);
        Apparel apparel = apparelRepository.getById(apparelId);
        return apparel.getPrice() * (1 - percentage / 100);
    }

    @Override
    public Deal save(DealDto dealDto,boolean flag) {
        if(dealDto.getValidFrom().isEmpty())
            dealDto.setValidFrom(LocalDateTime.now().toString());
        Deal deal;
        if(flag) {
        	deal= new Deal(apparelRepository.getById(dealDto.getApparelId()),
                    dealDto.getPercentage(),
                    LocalDateTime.parse(dealDto.getValidFrom()),
                    LocalDateTime.parse(dealDto.getValidUpto()) );
        }
        else {
        	deal = dealRepository.findByApparel_Id(dealDto.getApparelId());
        	deal.setPercentage(dealDto.getPercentage());
        	deal.setValidFrom(LocalDateTime.parse(dealDto.getValidFrom()));
        	deal.setValidUpto(LocalDateTime.parse(dealDto.getValidUpto()));
        }
        	
        return dealRepository.save(deal);
    }

	@Override
	public DealDto oneDeal(UUID apparelId) {
		Deal deal = (dealRepository.findByApparel_Id(apparelId));
		return new DealDto(apparelId,deal.getPercentage(),deal.getValidFrom().toString(),deal.getValidUpto().toString());
	}

	@Override
	public void deleteDeal(UUID apparelId) {
		dealRepository.findByApparel_Id(apparelId).setApparel(null);
		dealRepository.delete(dealRepository.findByApparel_Id(apparelId));
	}

}
