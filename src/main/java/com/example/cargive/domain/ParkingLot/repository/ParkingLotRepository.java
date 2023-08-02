package com.example.cargive.domain.ParkingLot.repository;

import com.example.cargive.domain.ParkingLot.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

}
