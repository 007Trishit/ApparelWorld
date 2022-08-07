package com.tmg.ApparelStore.dao.service;

import org.springframework.stereotype.Service;

import com.tmg.ApparelStore.dao.repository.ApparelRepository;
import com.tmg.ApparelStore.dao.repository.UserRepository;
import com.tmg.ApparelStore.dto.ApparelDto;
import com.tmg.ApparelStore.model.Apparel;
import com.tmg.ApparelStore.model.Preference;
import com.tmg.ApparelStore.model.Sex;
import com.tmg.ApparelStore.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApparelServiceImpl implements ApparelService{
    private final ApparelRepository repository;
    private final UserRepository userRepository;

    public ApparelServiceImpl(ApparelRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    @Override
    public Apparel save(ApparelDto dto, boolean flag) {
        Apparel apparel;
        if(flag) apparel=new Apparel(dto);
        else {
        	apparel=repository.findById(dto.getId()).get();
        	apparel.setBrandName(dto.getBrandName());
        	apparel.setGenericName(dto.getGenericName());
        	apparel.setGender(dto.getGender());
        	apparel.setPrice(dto.getPrice());
        	apparel.setSeason(dto.getSeason());
        	if(!dto.getImage().isEmpty()) 
        		apparel.setImage(dto.getImage());
        	System.out.println(apparel);
        }
        return repository.save(apparel);
    }

    @Override
    public List<Apparel> listApparel(String email) {
        User user = userRepository.findByEmail(email);
        System.out.println(user);
        return repository.findAll()
                .stream()
                .filter(apparel -> apparel.getGender() == Sex.OTHERS || apparel
                .getGender() == user.getSex())
                .filter(apparel -> {
                    if(user.getPreference() == Preference.NEW_ARRIVAL)
                    {
                        return Duration.between(apparel.getCreatedAt().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() < 2;
                    }

                    return apparel.getSeason().getMonths().contains(LocalDate.now().getMonthValue());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Apparel> listApparel() {
        return repository.findAll();
    }


	@Override
	public ApparelDto oneApparel(UUID id) {
		return new ApparelDto(repository.findById(id).get());
	}


	@Override
	public void deleteApparel(UUID id) {
		repository.delete(repository.findById(id).get());
	}
}
