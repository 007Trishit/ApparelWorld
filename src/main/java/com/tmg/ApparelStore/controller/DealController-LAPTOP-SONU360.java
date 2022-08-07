package com.tmg.ApparelStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmg.ApparelStore.dao.service.DealService;
import com.tmg.ApparelStore.dto.DealDto;
import com.tmg.ApparelStore.model.Deal;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/admin/deal")
public class DealController {
	@Autowired
    private DealService dealService;
	
    @ModelAttribute("deal")
    DealDto dealDto()
    {
        return new DealDto();
    }

    @PostMapping("")
    String createDeal(@ModelAttribute("deal") DealDto dto)
    {
        if(dto.getValidFrom() == null || dto.getValidFrom().isEmpty())
        {
            dto.setValidFrom(LocalDateTime.now().toString());
        }

        Deal deal = dealService.save(dto,true);
        if(deal == null)
        {
            return "redirect:/admin?dealError";
        }
        return "redirect:/admin?dealSuccess";
    }

    @GetMapping("/edit/{id}")
    String editDeal(@PathVariable("id") String id, Model model)
    {
        
        model.addAttribute("deal", dealService.oneDeal(UUID.fromString(id)));
        return "updateDeal";
    }
    
    @PostMapping("/process-edit")
    String updateDeal(@ModelAttribute("deal") DealDto dto)
    {
    	dealService.save(dto, false);
        return "redirect:/admin?dealSuccess";
    }
    @GetMapping("/delete/{id}")
    String deleteDeal(@PathVariable("id") String id)
    {
        
        dealService.deleteDeal(UUID.fromString(id));
        return "redirect:/admin?dealSuccess";
    }
}
