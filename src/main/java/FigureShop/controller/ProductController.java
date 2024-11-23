package FigureShop.controller;

import FigureShop.model.Product;
import FigureShop.service.CategoryService;
import FigureShop.service.ProductService;
import FigureShop.service.YearManufactureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private YearManufactureService yearManufactureService;

    // Display a list of all products
    @GetMapping
    public String showProductList(@RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "category", required = false) String category,
                                  @RequestParam(name = "priceRange", required = false) String priceRange,
                                  Model model) {
        List<Product> products = productService.getAllProducts();

        if (name != null && !name.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (category != null && !category.isEmpty() && !category.equals("Tất cả")) {
            products = products.stream()
                    .filter(product -> product.getCategory().getName().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }
        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "0-100":
                    products = products.stream()
                            .filter(product -> product.getPrice() <= 100)
                            .collect(Collectors.toList());
                    break;
                case "100-200":
                    products = products.stream()
                            .filter(product -> product.getPrice() > 100 && product.getPrice() <= 200)
                            .collect(Collectors.toList());
                    break;
                case "200-500":
                    products = products.stream()
                            .filter(product -> product.getPrice() > 200 && product.getPrice() <= 500)
                            .collect(Collectors.toList());
                    break;
                case "500-1000":
                    products = products.stream()
                            .filter(product -> product.getPrice() > 500 && product.getPrice() <= 1000)
                            .collect(Collectors.toList());
                    break;
                case "1000+":
                    products = products.stream()
                            .filter(product -> product.getPrice() > 1000)
                            .collect(Collectors.toList());
                    break;
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/products-list";
    }

    // Display add product form
    @GetMapping("/add")
    public String showAddForm(Model model) {
            model.addAttribute("product", new Product());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("years", yearManufactureService.getAllYearManufacture());
            return "/products/add-product";
        }


    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("imageUrl") MultipartFile imageUrl) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        if (!imageUrl.isEmpty()) {
            try {
                File uploadDir = new File("src/main/resources/static/images/");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String image = imageUrl.getOriginalFilename();
                Path imagePath = Paths.get("src/main/resources/static/images/" + image);
                Files.write(imagePath, imageUrl.getBytes());
                product.setImUrl("/images/" + image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        productService.addProduct(product);
        return "redirect:/products";
    }

    // Display edit product form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("years", yearManufactureService.getAllYearManufacture());
        return "/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, @RequestParam("imageUrl") MultipartFile imageUrl, BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/products/update-product";
        }

        // Lấy sản phẩm hiện tại từ database
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        // Nếu người dùng không tải lên hình ảnh mới, giữ lại đường dẫn hình ảnh cũ
        if (!imageUrl.isEmpty()) {
            try {
                File uploadDir = new File("src/main/resources/static/images/");
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String image = imageUrl.getOriginalFilename();
                Path imagePath = Paths.get("src/main/resources/static/images/" + image);
                Files.write(imagePath, imageUrl.getBytes());
                product.setImUrl("/images/" + image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Giữ lại đường dẫn hình ảnh cũ
            product.setImUrl(existingProduct.getImUrl());
        }

        productService.updateProduct(product);
        return "redirect:/products";
    }


    // Handle delete product request
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    // Handle search request
    @GetMapping("/search")
    public String searchProducts(@RequestParam("name") Optional<String> name,
                                 @RequestParam("category") Optional<Long> categoryId,
                                 @RequestParam("minPrice") Optional<Double> minPrice,
                                 @RequestParam("maxPrice") Optional<Double> maxPrice,
                                 Model model) {
        List<Product> products = productService.searchProducts(name.orElse(""), categoryId.orElse(null), minPrice.orElse(null), maxPrice.orElse(null));
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/products-list";
    }
    @GetMapping("/searchByName")
    @ResponseBody
    public List<String> searchProductNames(@RequestParam("term") String term) {
        return productService.searchProductNames(term);
    }

}
