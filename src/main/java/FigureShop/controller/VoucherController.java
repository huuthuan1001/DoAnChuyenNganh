package FigureShop.controller;

import FigureShop.model.Voucher;
import FigureShop.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String showAllVouchers(Model model) {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        model.addAttribute("vouchers", vouchers);
        return "voucher/list";
    }

    @GetMapping("/add")
    public String showAddVoucherForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "voucher/add";
    }

    @PostMapping("/add")
    public String addVoucher(@ModelAttribute Voucher voucher) {
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

    @GetMapping("/edit/{id}")
    public String showEditVoucherForm(@PathVariable Long id, Model model) {
        Optional<Voucher> voucherOpt = voucherService.getVoucherById(id);
        if (voucherOpt.isPresent()) {
            model.addAttribute("voucher", voucherOpt.get());
            return "voucher/edit";
        } else {
            return "redirect:/voucher";
        }
    }

    @PostMapping("/edit")
    public String editVoucher(@ModelAttribute Voucher voucher) {
        voucherService.saveVoucher(voucher);
        return "redirect:/voucher";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucherById(id);
        return "redirect:/voucher";
    }
}
