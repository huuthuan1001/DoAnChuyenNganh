package FigureShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "name is required")
    @Size(min = 1, message = "Tên phải có số ký tự >1")
    private String name;
    private String imUrl;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Giá phải lớn hơn 0")
    private double price;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "year_id")
    private YearManufacture yearManufacture;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImageList;
}