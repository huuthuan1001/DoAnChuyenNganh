package FigureShop.controller;

import FigureShop.model.Revenue;
import FigureShop.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/revenues")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/daily")
    public String getDailyRevenue(Model model) {
        LocalDate today = LocalDate.now();
        Revenue revenue = revenueService.getRevenueByDate(today);
        model.addAttribute("revenue", revenue);
        return "revenues/daily-revenue";
    }

    @GetMapping("/monthly")
    public String getMonthlyRevenue(Model model) {
        YearMonth currentMonth = YearMonth.now();
        List<Revenue> revenues = revenueService.getRevenuesByMonth(currentMonth);
        model.addAttribute("revenues", revenues);
        return "revenues/monthly-revenue";
    }
}
