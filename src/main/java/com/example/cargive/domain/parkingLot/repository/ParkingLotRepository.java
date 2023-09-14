package com.example.cargive.domain.parkingLot.repository;

import com.example.cargive.domain.parkingLot.entity.ParkingLot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
//    @Query("select p " +
//            "from parkingLot p " +
//            "join p.favorite f " +
//            "where f.id = :favoriteId " +
//            "and p.id < :parkingLotId " +
//            "order by p.id desc " +
//            "limit 100")
//    Slice<ParkingLot> findParkingLotsByFavoriteId(@Param("favoriteId") Long favoriteId,
//                                                  @Param("parkingLotId") Long parkingLotId,
//                                                  Pageable pageable);
}
