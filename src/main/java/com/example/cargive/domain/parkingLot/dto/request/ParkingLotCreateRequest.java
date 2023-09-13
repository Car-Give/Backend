package com.example.cargive.domain.ParkingLot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingLotCreateRequest {

    @NotNull(message = "즐겨찾기 아이디는 필수입니다.")
    private Long favoriteId;

    @NotBlank(message = "주차장 이름은 필수입니다.")
    private String parkingLotName;

    @NotBlank(message = "주차장 번호는 필수입니다.")
    private String phoneNumber;

    @NotBlank(message = "주차장 주소는 필수입니다.")
    private String address;

    @NotBlank(message = "주차장 요금은 필수입니다.")
    private String fee;

}
