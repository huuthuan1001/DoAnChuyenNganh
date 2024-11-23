package FigureShop.repository;
import FigureShop.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long>{
    Optional<Revenue> findByDate(LocalDate date);
    List<Revenue> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
