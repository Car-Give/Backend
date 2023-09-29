package com.example.cargive.car.fixture;

import com.example.cargive.domain.car.entity.Car;
import com.example.cargive.domain.member.entity.Member;
import com.example.cargive.global.template.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum CarFixture {
    CAR_1("TestCar1", "1", LocalDate.now(), 1L, "Test Image1", false, Status.NORMAL),
    CAR_2("TestCar2", "2", LocalDate.now().plusDays(1), 2L, "Test Image2", false, Status.NORMAL),
    CAR_3("TestCar3", "3", LocalDate.now().plusDays(2), 3L, "Test Image3", true, Status.NORMAL),
    CAR_4("TestCar4", "4", LocalDate.now().plusDays(3), 4L, "Test Image4", false, Status.NORMAL),
    CAR_5("TestCar5", "5", LocalDate.now().plusDays(4), 5L, "Test Image5", false, Status.NORMAL)
    ;
    private final String type;
    private final String number;
    private final LocalDate recentCheck;
    private final Long mileage;
    private final String imageUrl;
    private final boolean isFavorite;
    private final Status status;

    public Car createEntity() {
        return new Car(type, number, recentCheck, mileage, imageUrl, null);
    }

    public Car createEntityWithMember(Member member) {
        return new Car(type, number, recentCheck, mileage, imageUrl, member);
    }
}
