package FigureShop.service;

import FigureShop.model.CartItem;
import FigureShop.model.Product;
import FigureShop.model.Voucher;
import FigureShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    private Voucher appliedVoucher; // Lưu voucher
    private boolean voucherApplied = false; // Đánh dấu áp dụng voucher

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(new CartItem(product, quantity));
        }

        resetVoucherStatus();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));

        resetVoucherStatus();
    }

    public void clearCart() {
        cartItems.clear();

        resetVoucherStatus();
    }

    public void increaseQuantity(Long productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
        resetVoucherStatus();
    }

    public void decreaseQuantity(Long productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                }
                break;
            }
        }
        resetVoucherStatus();
    }

    public double getTotalPrice() {
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        // Áp dụng giảm giá
        if (voucherApplied && appliedVoucher != null) {
            totalPrice -= (totalPrice * appliedVoucher.getDiscount() / 100);
        }

        return totalPrice;
    }

    public void applyVoucher(Voucher voucher) {
        this.appliedVoucher = voucher;
        this.voucherApplied = true; //
    }

    public void removeVoucher() {
        this.appliedVoucher = null;
        this.voucherApplied = false;
    }

    public Voucher getAppliedVoucher() {
        return appliedVoucher;
    }

    private void resetVoucherStatus() {
        this.voucherApplied = false;
    }
}
