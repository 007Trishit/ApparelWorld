package com.tmg.ApparelStore.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tmg.ApparelStore.dao.service.ApparelService;
import com.tmg.ApparelStore.dto.ApparelDto;
import com.tmg.ApparelStore.model.Apparel;

@Controller
@RequestMapping("/admin/apparel")
public class ApparelController {

    private ApparelService apparelService;

    public ApparelController(ApparelService apparelService) {
        this.apparelService = apparelService;
    }

    @PostMapping("")
    String createApparel(@ModelAttribute("apparel") ApparelDto dto, @RequestParam("dispImage") MultipartFile file)
    {
    	try {
        
        if(file.isEmpty()) {
        	System.out.println("File is empty");
        }
        else {
        	dto.setImage(file.getOriginalFilename());
        	File saveFile= new ClassPathResource("static/img").getFile();
        	Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        Apparel apparel = apparelService.save(dto, true);
        if(apparel == null)
            return "redirect:/admin?apparelError";
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
        
        return "redirect:/admin?apparelSuccess";
    }
    
    @GetMapping("/edit/{id}")
    String editDeal(@PathVariable("id") String id, Model model)
    {
        
        model.addAttribute("apparel", apparelService.oneApparel(UUID.fromString(id)));
        return "updateApparel";
    }
    
    @PostMapping("/process-edit")
    String updateDeal(@ModelAttribute("apparel") ApparelDto dto, @RequestParam("dispImage") MultipartFile file)
    {
    	try {
            
            if(file.isEmpty()) {
            	System.out.println("File is empty");
            }
            else {
            	dto.setImage(file.getOriginalFilename());
            	File saveFile= new ClassPathResource("static/img").getFile();
            	Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
            	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            Apparel apparel = apparelService.save(dto, false);
            if(apparel == null)
                return "redirect:/admin?apparelError";
        	}
        	catch(Exception e) {
        		System.out.println(e.getMessage());
        	}
    	
        return "redirect:/admin?dealSuccess";
    }
}
