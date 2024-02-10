package com.umc.gusto.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 에러 발생, 관리자에게 문의해주세요."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST,400, "잘못된 요청입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN,403, "해당 권한이 없습니다."),

    //User 관련 에러 +0
    USER_DUPLICATE_NICKNAME(HttpStatus.CONFLICT, 409001, "이미 사용중인 닉네임입니다."),
    USER_ALREADY_SIGNUP(HttpStatus.CONFLICT, 409003, "가입이 완료된 유저입니다."),
    DONT_EXIST_USER(HttpStatus.NOT_FOUND, 404101, "존재하지 않는 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404001, "존재하지 않는 유저입니다."),
    USER_FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, 404002, "팔로우한 유저가 아닙니다."),
    USER_FOLLOW_NO_MORE_CONTENT(HttpStatus.NOT_FOUND, 404003, "리스트가 더이상 존재하지 않습니다."),
    USER_FOLLOW_ALREADY(HttpStatus.CONFLICT, 409002, "이미 팔로우했습니다."),
    USER_FOLLOW_SELF(HttpStatus.FORBIDDEN, 403001, "자신을 팔로우할 수 없습니다."),

    //Store 관련 에러 +1
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, 404101, "존재하지 않는 가게입니다."),

    //Review 관련 에러 +2
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, 404201, "존재하지 않는 리뷰입니다."),
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, 404202, "존재하지 않는 해시태그입니다."),
    USER_NO_PERMISSION_FOR_REVIEW(HttpStatus.FORBIDDEN, 403203, "해당 유저는 해당 리뷰에 대한 권한이 없습니다."),
    NO_ONESELF_LIKE(HttpStatus.BAD_REQUEST, 400204, "자기자신의 리뷰는 좋아요할 수 없습니다."),
    NO_LIKE_REVIEW(HttpStatus.BAD_REQUEST, 400205, "해당 리뷰에 좋아요를 한 적이 없습니다."),

    //Route 관련 에러 +3
    ROUTE_DUPLICATE_ROUTENAME(HttpStatus.CONFLICT, 409301,"이미 사용중인 루트명입니다."),
    ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND,404302,"존재하지 않는 루트입니다."),
    ROUTELIST_NOT_FOUND(HttpStatus.NOT_FOUND,404302,"루트에 존재하지 않은 상점 항목입니다"),
    USER_NO_PERMISSION_FOR_ROUTE(HttpStatus.FORBIDDEN,403303,"해당 유저는 해당 루트에 대한 권한이 없습니다"),

    //Group 관련 에러 +4
    FIND_FAIL_GROUP(HttpStatus.NOT_FOUND, 404401, "존재하지 않는 그룹입니다."),
    UNAUTHORIZED_DELETE_GROUP(HttpStatus.FORBIDDEN, 403402, "그룹을 삭제할 권한이 없습니다."),
    UNAUTHORIZED_MODIFY_GROUP_NAME(HttpStatus.FORBIDDEN, 403403, "그룹명을 수정할 권한이 없습니다."),
    INVITATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, 404404,"그룹의 초대 코드를 찾을 수 없습니다."),
    INVALID_INVITATION_CODE(HttpStatus.FORBIDDEN, 403405, "그룹의 초대 코드가 올바르지 않습니다."),
    USER_NOT_IN_GROUP(HttpStatus.NOT_FOUND, 404406, "그룹에 가입되지 않은 유저입니다."),
    NO_TRANSFER_PERMISSION(HttpStatus.FORBIDDEN, 403407, "그룹 소유자만이 그룹 소유권을 이전할 수 있습니다."),
    USER_NO_PERMISSION_FOR_GROUP(HttpStatus.FORBIDDEN,403408,"해당 유저는 그룹 구성원이 아닙니다."),
    GROUPLIST_NOT_FROUND(HttpStatus.NOT_FOUND,403409,"존재하지 않는 그룹 내 상점입니다."),
  
    //myCategory 관련 에러 +5
    MYCATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, 404501, "존재하지 않는 카테고리입니다."),
    MYCATEGORY_DUPLICATE_NAME(HttpStatus.CONFLICT, 404502, "이미 존재하는 카테고리입니다."),
    USER_NO_PERMISSION_FOR_MYCATEGORY(HttpStatus.FORBIDDEN, 404503, "해당 유저는 해당 카테고리에 대한 권한이 없습니다."),
    PIN_NOT_FOUND(HttpStatus.NOT_FOUND, 404504,"존재하지 않는 찜(Pin) 입니다"),

    // token 관련 에러 +6
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401601, "유효하지 않은 토큰입니다."),


    FOR_TEST_ERROR(HttpStatus.BAD_REQUEST,49999, "테스트용 에러")


    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
