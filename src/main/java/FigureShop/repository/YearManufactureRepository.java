package FigureShop.repository;

import FigureShop.model.YearManufacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface YearManufactureRepository extends JpaRepository<YearManufacture, Long> {

}
