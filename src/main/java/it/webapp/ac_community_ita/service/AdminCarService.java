package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.CarDto;
import it.webapp.ac_community_ita.entity.Car;
import it.webapp.ac_community_ita.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminCarService {

    private final CarRepository carRepository;

    public AdminCarService(CarRepository carRepository) {
        this.carRepository =  carRepository;
    }


    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car findById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auto non trovata con id: " + id));
    }


    public Car create(CarDto dto) {

        if (carRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Esiste già una vettura con il nome " + dto.getName());
        }
        Car car = new Car();
        car.setName(dto.getName());
        car.setCarClass(dto.getCarClass());
        car.setCreatedAt(LocalDateTime.now());
        return carRepository.save(car);
    }

    public Car update(Long id, CarDto dto) {
        Car car = findById(id);
        car.setName(dto.getName());
        car.setCarClass(dto.getCarClass());
        return carRepository.save(car);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}