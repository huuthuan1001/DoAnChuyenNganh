package FigureShop.controller;

import FigureShop.model.Voucher;
import FigureShop.repository.VoucherRepository;
import FigureShop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherRepository voucherRepository;


    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        //tinh tong tiền
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "/cart/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int
            quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    //update lại số lượng sản phẩm trong giỏ hàng
    @PostMapping("/update")
    public String updateCart(@RequestParam Long productId, @RequestParam String action) {
        if ("increase".equals(action)) {
            cartService.increaseQuantity(productId);
        } else if ("decrease".equals(action)) {
            cartService.decreaseQuantity(productId);
        }
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }


   @PostMapping("/apply-voucher")
   public String applyVoucher(@RequestParam("voucherCode") String voucherCode, Model model) {
       Voucher voucher = voucherRepository.findByCode(voucherCode);

       if (voucher != null) {
           if (voucher.getQuantity() <= 0) {
               model.addAttribute("error", "Số lượng voucher đã hết");
           } else {
               cartService.applyVoucher(voucher);

               model.addAttribute("appliedVoucher", voucher);

               double totalPriceAfterDiscount = cartService.getTotalPrice();
               model.addAttribute("totalPrice", totalPriceAfterDiscount);
           }
       } else {
           model.addAttribute("error", "Mã voucher không hợp lệ");
       }
       return "redirect:/cart";
   }


}