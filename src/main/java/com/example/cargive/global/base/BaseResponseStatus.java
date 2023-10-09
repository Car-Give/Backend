package com.example.cargive.global.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus implements BaseResponseStatusImpl {
    /**
     * 100 : 진행 정보
     */

    /**
     * 200 : 요청 성공
     */
    SUCCESS(HttpStatus.OK, "SUCCESS", "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "CREATED", "요청에 성공했으며 리소스가 정상적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "ACCEPTED", "요청에 성공했으나 처리가 완료되지 않았습니다."),
    DELETED(HttpStatus.NO_CONTENT, "DELETED", "요청에 성공했으며 더 이상 응답할 내용이 존재하지 않습니다."),

    /**
     * 300 : 리다이렉션
     */
    SEE_OTHER(HttpStatus.SEE_OTHER, "REDIRECT", "다른 주소로 요청해주세요."),

    /**
     * 400 : 요청 실패
     */
    INPUT_INVALID_VALUE(HttpStatus.BAD_REQUEST, "REQUEST_ERROR_001", "잘못된 요청입니다."),
    INVALID_ENUM(HttpStatus.BAD_REQUEST, "REQUEST_012", "Enum 타입으로 변경할 수 없습니다."),

    // Member
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 사용자 입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "M002", "이메일 형식이 올바르지 않습니다."),

    // Favorite
    FAVORITE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "F001", "존재하지 않는 즐겨찾기 입니다."),
    NOT_FOUND_SORT_CONDITION(HttpStatus.NOT_FOUND, "F002", "지원하지 않는 정렬 방식입니다."),
    FAVORITE_TYPE_ERROR(HttpStatus.NOT_FOUND, "F003", "일치하지 않는 타입의 데이터입니다."),
    FAVORITE_MEMBER_NOT_MATCH_ERROR(HttpStatus.BAD_REQUEST, "F004", "생성자와 이용자의 정보가 일치하지 않습니다."),

    // ParkingLot
    PARKING_LOT_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 주차장 입니다."),

    // Question
    QUESTION_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Q001", "존재하지 않는 질문 입니다."),
    QUESTION_MEMBER_NOT_MATCH_ERROR(HttpStatus.BAD_REQUEST, "Q002", "현재 사용자가 작성한 질문이 아닙니다."),
    QUESTION_CATEGORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "Q003", "존재하지 않는 질문 카테고리 입니다."),

    // Car
    CAR_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 자동차입니다."),
    CAR_MEMBER_NOT_MATCH_ERROR(HttpStatus.CONFLICT, "C002", "생성자와 이용자의 정보가 일치하지 않습니다."),
    CAR_LIST_EMPTY_ERROR(HttpStatus.NOT_FOUND, "C003", "차량 정보가 존재하지 않습니다."),

    // Tag
    TAG_LIST_EMPTY_ERROR(HttpStatus.NOT_FOUND, "T001", "일치하는 차량 특징 카드가 존재하지 않습니다."),
    TAG_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "T002", "존재하지 않는 차량 특징 카드입니다."),
    CAR_NOT_MATCH_ERROR(HttpStatus.CONFLICT, "T003", "차량 특징 카드에 등록된 정보와 일치하지 않습니다."),

    // History
    HISTORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "H001", "일치하는 기록이 존재하지 않습니다."),
    HISTORY_CAR_NOT_MATCH_ERROR(HttpStatus.CONFLICT, "H002", "차량 정보와 이용 기록 정보가 일치하지 않습니다."),
    HISTORY_LIST_EMPTY_ERROR(HttpStatus.NOT_FOUND, "H003", "이용 기록이 존재하지 않습니다."),

    // S3
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "파일 업로드에 실패했습니다."),
    FILE_EMPTY_ERROR(HttpStatus.NOT_FOUND, "S002", "파일이 존재하지 않습니다."),

    /**
     * 500 : 응답 실패
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RESPONSE_ERROR_001", "서버와의 연결에 실패했습니다."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "RESPONSE_ERROR_002", "다른 서버로부터 잘못된 응답이 수신되었습니다."),
    INSUFFICIENT_STORAGE(HttpStatus.INSUFFICIENT_STORAGE, "RESPONSE_ERROR_003", "서버의 용량이 부족해 요청에 실패했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
