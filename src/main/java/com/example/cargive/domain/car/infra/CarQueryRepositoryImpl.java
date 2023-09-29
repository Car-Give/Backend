package com.example.cargive.domain.car.infra;

import com.example.cargive.domain.car.controller.dto.response.CarResponse;
import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.car.infra.dto.CarQueryResponse;
import com.example.cargive.global.template.Status;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.cargive.domain.car.entity.QCar.*;
import static com.example.cargive.domain.tag.entity.QTag.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarQueryRepositoryImpl implements CarQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CarQueryResponse<CarResponse> findCarByMemberId(Long memberId) {
        List<Car> carList = jpaQueryFactory
                .selectFrom(car)
                .join(car.tagList, tag).fetchJoin()
                .where(car.member.id.eq(memberId), tag.status.eq(Status.NORMAL), car.status.eq(Status.NORMAL))
                .orderBy(car.isFavorite.desc(), car.createAt.desc())
                .fetch();

        List<CarResponse> responseData = carList.stream()
                .map(carData -> new CarResponse(
                        carData.getId(), carData.getType(), carData.getNumber(), carData.getRecentCheck(),
                        carData.getMileage(), carData.getImageUrl(), carData.isFavorite(), carData.getTagList()))
                .collect(Collectors.toList());

        return new CarQueryResponse<>(responseData);
    }
}