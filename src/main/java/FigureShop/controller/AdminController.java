package FigureShop.controller;

import FigureShop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private  OrderService orderService;
    @GetMapping("/manager")
    public String showAdminManagerPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();

        for (SimpleGrantedAuthority authority : authorities) {
            System.out.println("Role: " + authority.getAuthority());
        }

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
            System.out.println("Truy cập với quyền quản trị");
            return "admin/manager";
        } else {
            System.out.println("Access Denied. User roles: " + authorities.toString());
            return "redirect:/access-denied";
        }
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return "redirect:/admin/orders/list"; // Chuyển hướng về trang danh sách đơn hàng sau khi cập nhật
    }
}
