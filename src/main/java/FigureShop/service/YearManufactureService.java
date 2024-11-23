
package FigureShop.service;

import FigureShop.model.YearManufacture;
import FigureShop.repository.YearManufactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class YearManufactureService {

    private final YearManufactureRepository yearManufactureRepository;

    public List<YearManufacture> getAllYearManufacture() {
        return yearManufactureRepository.findAll();
    }

    public Optional<YearManufacture> getYearManufactureById(Long id) {
        return yearManufactureRepository.findById(id);
    }

    public void addYearManufacture(YearManufacture yearManufacture) {
        yearManufactureRepository.save(yearManufacture);
    }

    public void updateYearManufacture(YearManufacture yearManufacture) {
        YearManufacture existingYear = yearManufactureRepository.findById(yearManufacture.getId())
                .orElseThrow(() -> new IllegalStateException("YearManufacture with ID " + yearManufacture.getId() + " does not exist."));
        existingYear.setYear(yearManufacture.getYear());
        yearManufactureRepository.save(existingYear);
    }

    public void deleteYearManufactureById(Long id) {
        if (!yearManufactureRepository.existsById(id)) {
            throw new IllegalStateException("YearManufacture with ID " + id + " does not exist.");
        }
        yearManufactureRepository.deleteById(id);
    }
}