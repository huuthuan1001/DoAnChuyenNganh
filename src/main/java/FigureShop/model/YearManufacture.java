
package FigureShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "YearManufactures")
public class YearManufacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Năm là bắt buộc")
    @Min(value = 2000, message = "Năm phải lớn hơn hoặc bằng 2000")
    @Max(value = 2024, message = "Năm phải bé hơn hoặc bằng năm hiện tại {2024}")

    private Integer year;
}