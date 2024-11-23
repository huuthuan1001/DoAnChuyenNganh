
package FigureShop.controller;

import FigureShop.model.YearManufacture;
import FigureShop.service.YearManufactureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class YearManufactureController {

    @Autowired
    private final YearManufactureService yearManufactureService;

    @GetMapping("/years/add")
    public String showAddForm(Model model) {
        model.addAttribute("yearManufacture", new YearManufacture());
        return "years/add-year";
    }

    @PostMapping("/years/add")
    public String addYearManufacture(@Valid YearManufacture yearManufacture, BindingResult result) {
        if (result.hasErrors()) {
            return "years/add-year";
        }
        yearManufactureService.addYearManufacture(yearManufacture);
        return "redirect:/years";
    }

    @GetMapping("/years")
    public String listYearManufacture(Model model) {
        List<YearManufacture> years = yearManufactureService.getAllYearManufacture();
        model.addAttribute("years", years);
        return "years/years-list";
    }

    @GetMapping("/years/edit/{id}")
    public String showUpdateYearForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
        if (authorities.contains((new SimpleGrantedAuthority("ADMIN")))){
            YearManufacture yearManufacture = yearManufactureService.getYearManufactureById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Year Id:" + id));
            model.addAttribute("yearManufacture", yearManufacture);
            return "years/update-year";
        }else {
            return "redirect:/access-denied";
        }

    }

    @PostMapping("/years/update/{id}")
    public String updateYearManufacture(@PathVariable("id") Long id, @Valid YearManufacture yearManufacture,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            yearManufacture.setId(id);
            return "years/update-year";
        }
        yearManufactureService.updateYearManufacture(yearManufacture);
        model.addAttribute("years", yearManufactureService.getAllYearManufacture());
        return "redirect:/years";
    }

    @GetMapping("/years/delete/{id}")
    public String deleteYearManufacture(@PathVariable("id") Long id, Model model) {
        YearManufacture yearManufacture = yearManufactureService.getYearManufactureById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid year Id:" + id));
        yearManufactureService.deleteYearManufactureById(id);
        model.addAttribute("years", yearManufactureService.getAllYearManufacture());
        return "redirect:/years";
    }
}