package com.example.cargive.domain.car.entity;

import com.example.cargive.domain.car.infra.CarQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long>, CarQueryRepository {
}