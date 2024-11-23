package FigureShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "revenues", uniqueConstraints = @UniqueConstraint(columnNames = {"date"}))
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate date;
    private Double totalRevenue;
    private int numberOfSales;

    @OneToMany(mappedBy = "revenue")
    private List<Order> orders;

    public Revenue(LocalDate date, double totalRevenue, int numberOfSales) {
        this.date = date;
        this.totalRevenue = totalRevenue;
        this.numberOfSales = numberOfSales;
    }
}
