package com.tmg.ApparelStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tmg.ApparelStore.dao.service.ApparelService;
import com.tmg.ApparelStore.dao.service.DealService;
import com.tmg.ApparelStore.dao.service.UserService;
import com.tmg.ApparelStore.dto.ApparelDto;
import com.tmg.ApparelStore.metric.Metric;
import com.tmg.ApparelStore.model.Apparel;
import com.tmg.ApparelStore.model.Purchase;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Controller
public class ShopController {
	@Autowired
    private ApparelService apparelService;
	@Autowired
    private DealService dealService;
	@Autowired
    private UserService userService;

    @GetMapping("/shop")
    public String home(@RequestParam(required = false) String q, Authentication auth, Model model, HttpSession session) {
        Metric metric = (Metric) session.getAttribute("metric");
        User user = (User) auth.getPrincipal();

        if (metric == null) {
            metric = new Metric();
            userService.getPurchases(user.getUsername()).stream().map(Purchase::getApparel)
            .mapToDouble(Apparel::getPrice).forEach(metric::update);
            session.setAttribute("metric", metric);
        }

        if (q == null || q.isEmpty()) {

            List<ApparelDto> apparelList = apparelService.listApparel(user.getUsername())
            		.stream().map(ApparelDto::new)
                    .peek(apparelDto -> apparelDto.setDiscountedPrice(dealService
                    		.getDiscountedPrice(apparelDto.getId())))
                    .collect(Collectors.toList());
            model.addAttribute("apparelList", sortAccordingToMetric(apparelList, metric));
        } else {
            List<ApparelDto> apparelList = apparelService.listApparel()
                    .stream()
                    .filter(apparel -> apparel.getBrandName().toLowerCase(Locale.ROOT)
            		.startsWith(q.toLowerCase(Locale.ROOT)) || apparel
            		.getGenericName().toLowerCase(Locale.ROOT)
            		.startsWith(q.toLowerCase(Locale.ROOT)))
                    .map(ApparelDto::new)
                    .peek(apparelDto -> apparelDto.setDiscountedPrice(dealService.getDiscountedPrice(apparelDto.getId())))
                    .collect(Collectors.toList());
            model.addAttribute("apparelList", sortAccordingToMetric(apparelList, metric));
        }
        return "shop";
    }

    private List<ApparelDto> sortAccordingToMetric(List<ApparelDto> apparels, Metric metric) {

        apparels.sort((a, b) -> {
            double left = metric.getMean() - metric.getStandardDeviation();
            double right = metric.getMean() + metric.getStandardDeviation();
            if (a.getPrice() >= left && a.getPrice() <= right && b.getPrice() >= left && b.getPrice() <= right) {
                return Double.compare(a.getPrice(), b.getPrice());
            } else if (a.getPrice() >= left && a.getPrice() <= right) {
                return -1;
            } else if (b.getPrice() >= left && b.getPrice() <= right) {
                return 1;
            } else {
                return Double.compare(Math.abs(a.getPrice() - metric.getMean()), Math.abs(b.getPrice() - metric.getMean()));
            }
        });

        return apparels;
    }
}
