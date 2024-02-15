package com.umc.gusto.domain.myCategory.controller;

import com.umc.gusto.domain.myCategory.model.request.CreateMyCategoryRequest;
import com.umc.gusto.domain.myCategory.model.request.UpdateMyCategoryRequest;
import com.umc.gusto.domain.myCategory.model.response.MyCategoryResponse;
import com.umc.gusto.domain.myCategory.model.response.PinByMyCategoryResponse;
import com.umc.gusto.domain.myCategory.service.MyCategoryService;
import com.umc.gusto.domain.user.entity.User;
import com.umc.gusto.global.auth.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/myCategories")
public class MyCategoryController {
    private final MyCategoryService myCategoryService;

    /**
     * 카테고리 전체 조회
     * [GET] /myCategories/{nickname}
     */
    @GetMapping("/{nickname}")
    public ResponseEntity<List<MyCategoryResponse>> allMyCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable String nickname) {
        User user = authUser.getUser();
        List<MyCategoryResponse> myCategoryList = myCategoryService.getAllMyCategory(user, nickname);

        return ResponseEntity.status(HttpStatus.OK).body(myCategoryList);
    }

    /**
     * 위치 기반 내 카테고리 전체 조회
     * [GET] /myCategories?townName={townName}
     */
    @GetMapping
    public ResponseEntity<List<MyCategoryResponse>> allMyCategoryWithLocation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "townName") String townName) {
            User user = authUser.getUser();
            List<MyCategoryResponse> myCategoryList = myCategoryService.getAllMyCategoryWithLocation(user, townName);

            return ResponseEntity.status(HttpStatus.OK).body(myCategoryList);
    }

    /**
     * 카테고리 별 가게 목록 조회
     * [GET] /myCategories/pins/{nickname}?myCategoryId={myCategoryId}
     */
    @GetMapping("/pins/{nickname}")
    public ResponseEntity<List<PinByMyCategoryResponse>> allPinByMyCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "myCategoryId") Long myCategoryId,
            @PathVariable String nickname) {
            User user = authUser.getUser();
            List<PinByMyCategoryResponse> myStoreList = myCategoryService.getAllPinByMyCategory(user, nickname, myCategoryId);

            return ResponseEntity.status(HttpStatus.OK).body(myStoreList);
    }

    /**
     * 위치 기반 내 카테고리 별 가게 목록 조회
     * [GET] /myCategories/pins?myCategoryId={mycategoryId}&townName={townName}
     */
    @GetMapping("/pins")
    public ResponseEntity<List<PinByMyCategoryResponse>> allPinByCategoryWithLocation(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "myCategoryId") Long myCategoryId,
            @RequestParam(name = "townName") String townName) {
            User user = authUser.getUser();
            List<PinByMyCategoryResponse> myCategoryList = myCategoryService.getAllPinByMyCategoryWithLocation(user, myCategoryId,townName);

            return ResponseEntity.status(HttpStatus.OK).body(myCategoryList);
    }

    /**
     * 내 카테고리 생성
     * [POST] /myCategories
     */
    @PostMapping
    public ResponseEntity<?> createMyCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreateMyCategoryRequest request
    ) {
            User user = authUser.getUser();
            myCategoryService.createMyCategory(user, request);

            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 내 카테고리 수정
     * [PATCH] /myCategories/{myCategoryId}
     */
    @PatchMapping("/{myCategoryId}")
    public ResponseEntity<?> modifyMyCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long myCategoryId,
            @RequestBody UpdateMyCategoryRequest request
    ) {
        User user = authUser.getUser();
        myCategoryService.modifyMyCategory(user, myCategoryId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 내 카테고리 삭제
     * [DELETE] /myCategories?myCategoryId={myCategoryId}&myCategoryId={myCategoryId}...
     */
    @DeleteMapping
    public ResponseEntity<?> deleteMyCategory(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "myCategoryId") List<Long> myCategoryIds) {
            User user = authUser.getUser();
            myCategoryService.deleteMyCategories(user, myCategoryIds);

            return ResponseEntity.status(HttpStatus.OK).build();

    }
}