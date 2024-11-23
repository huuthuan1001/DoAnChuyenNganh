package FigureShop.controller;

import FigureShop.model.CartItem;
import FigureShop.model.Order;
import FigureShop.model.User;
import FigureShop.service.CartService;
import FigureShop.service.OrderService;
import FigureShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            User currentUser = user.get();
            model.addAttribute("email", currentUser.getEmail());
            model.addAttribute("phone", currentUser.getPhone());
        }
        return "/cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(String customerName,String address, String phoneNumber, String eMail, String note, String payment) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName,address,phoneNumber,eMail, note,payment, cartItems);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

    @GetMapping("/list")
    public String showOrderList(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/orders-list";
    }

    @GetMapping("/myorder")
    public String showMyOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Order> orders = orderService.getOrdersByUsername(username);



        System.out.println("Username: " + username);
        model.addAttribute("orders", orders);
        return "order/my-orders";
    }


}