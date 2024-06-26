package com.umc.gusto.domain.myCategory.service;

import com.umc.gusto.domain.myCategory.entity.MyCategory;
import com.umc.gusto.domain.myCategory.entity.Pin;
import com.umc.gusto.domain.myCategory.model.request.CreateMyCategoryRequest;
import com.umc.gusto.domain.myCategory.model.request.UpdateMyCategoryRequest;
import com.umc.gusto.domain.myCategory.model.response.MyCategoryResponse;
import com.umc.gusto.domain.myCategory.model.response.PinByMyCategoryResponse;
import com.umc.gusto.domain.myCategory.repository.MyCategoryRepository;
import com.umc.gusto.domain.myCategory.repository.PinRepository;
import com.umc.gusto.domain.review.entity.Review;
import com.umc.gusto.domain.review.repository.ReviewRepository;
import com.umc.gusto.domain.store.entity.Store;
import com.umc.gusto.domain.user.entity.User;
import com.umc.gusto.domain.user.repository.UserRepository;
import com.umc.gusto.global.common.BaseEntity;
import com.umc.gusto.global.exception.Code;
import com.umc.gusto.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyCategoryServiceImpl implements MyCategoryService {

    private final MyCategoryRepository myCategoryRepository;
    private final PinRepository pinRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private static final int MY_CATEGORY_PAGE_SIZE = 7;
    private static final int PIN_PAGE_SIZE = 5;

    @Transactional(readOnly = true)
    public List<MyCategoryResponse> getAllMyCategory(User user, String nickname, String townName, Long myCategoryId, Pageable pageable) {

        int pageNumber = pageable.getPageNumber();
        List<MyCategory> myCategoryList;
        if (nickname != null) {
            if (nickname.equals(user.getNickname())) {
                throw new GeneralException(Code.USER_NOT_FOUND_SELF);
            }
            user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new GeneralException(Code.USER_NOT_FOUND));
            if (myCategoryId != null) {
                myCategoryList = myCategoryRepository.findByUserNicknameAndPublishCategoryPublicPaging(user, myCategoryId, PageRequest.of(pageNumber, MY_CATEGORY_PAGE_SIZE));
            } else {
                myCategoryList = myCategoryRepository.findByUserNicknameAndPublishCategoryPublic(user, PageRequest.of(pageNumber, MY_CATEGORY_PAGE_SIZE));
            }
        } else {
            if (myCategoryId != null) {
                myCategoryList = myCategoryRepository.findByUserNicknameAndPublishCategoryPaging(user, myCategoryId, PageRequest.of(pageNumber, MY_CATEGORY_PAGE_SIZE));   // 받아온 nickname과 User의 nickname 값이 다른 경우(쿼리문 사용)
            } else {
                myCategoryList = myCategoryRepository.findByUserNicknameAndPublishCategory(user, PageRequest.of(pageNumber, MY_CATEGORY_PAGE_SIZE));   // 받아온 nickname과 User의 nickname 값이 다른 경우(쿼리문 사용)
            }

        }
        User finalUser = user;


        return myCategoryList.stream()
                .map(myCategory -> {
                    List<Pin> pinList;
                    if (townName != null) {
                        pinList = pinRepository.findPinsByMyCategoryAndTownNameAndPinIdDESC(myCategory, townName);     // 먼저 카테고리로 구분
                    } else {
                        pinList = pinRepository.findPinsByMyCategoryAndPinIdDESC(myCategory);     // 먼저 카테고리로 구분
                    }
                    return MyCategoryResponse.builder()
                            .myCategoryId(myCategory.getMyCategoryId())
                            .myCategoryName(myCategory.getMyCategoryName())
                            .myCategoryScript(myCategory.getMyCategoryScript())
                            .myCategoryIcon(myCategory.getMyCategoryIcon())
                            .publishCategory(finalUser.getPublishCategory())
                            .pinCnt(pinList.size())            // pin 개수 받아오기로 변경
                            .build();
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<PinByMyCategoryResponse> getAllPinByMyCategory(User user, String nickname, Long myCategoryId, String townName, Long pinId, Pageable pageable) {
        Optional<MyCategory> myCategory;

        int pageNumber = pageable.getPageNumber();
        List<Pin> pinList;
        if (nickname != null) {
            if (nickname.equals(user.getNickname())) {
                throw new GeneralException(Code.USER_NOT_FOUND_SELF);
                }
            user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new GeneralException(Code.USER_NOT_FOUND));
            myCategory = myCategoryRepository.findByMyCategoryPublicIdAndUserNickname(nickname, myCategoryId);
        } else {
            myCategory = myCategoryRepository.findByMyCategoryIdAndUserNickname(user.getNickname(), myCategoryId);
        }

        if (townName != null) {
            if (pinId != null) {
                pinList = myCategory.map(category -> pinRepository.findPinsByMyCategoryAndTownNameAndPinIdDESCPaging(category, townName, pinId, PageRequest.of(pageNumber, PIN_PAGE_SIZE)))
                        .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));
            } else {
                pinList = myCategory.map(category -> pinRepository.findPinsByMyCategoryAndTownNameAndPinIdDESCFirstPaging(category, townName, PageRequest.of(pageNumber, PIN_PAGE_SIZE)))
                        .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));
            }
        } else {
            if (pinId != null) {
                pinList = myCategory.map(category -> pinRepository.findPinsByMyCategoryAndPinIdDESCPaging(category, pinId, PageRequest.of(pageNumber, PIN_PAGE_SIZE)))
                        .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));
            } else {
                pinList = myCategory.map(category -> pinRepository.findPinsByMyCategoryAndPinIdDESCFirstPaging(category, PageRequest.of(pageNumber, PIN_PAGE_SIZE)))
                        .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));
            }
        }


        User finalUser = user;
        return pinList.stream()                                     // townName을 기준으로 보일 수 있는 store가 포함된 pin만 보이기
                .map(pin -> {
                    Store store = pin.getStore();
                    Optional<Review> topReviewOptional = reviewRepository.findFirstByStoreOrderByLikedDesc(store);               // 가장 좋아요가 많은 review
                    String reviewImg = topReviewOptional.map(Review::getImg1).orElse("");                               // 가장 좋아요가 많은 review 이미지(TO DO: 3개 출력으로 변경)
                    Integer reviewCnt = reviewRepository.countByStoreAndUserNickname(store, finalUser.getNickname());                        // 내가 작성한 리뷰의 개수 == 방문 횟수

                    return  PinByMyCategoryResponse.builder()
                            .pinId(pin.getPinId())
                            .storeId(store.getStoreId())
                            .storeName(store.getStoreName())
                            .address(store.getAddress())
                            .reviewImg(reviewImg)
                            .reviewCnt(reviewCnt)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createMyCategory(User user, CreateMyCategoryRequest createMyCategory) {
        // 중복 이름 체크
        myCategoryRepository.findByMyCategoryNameAndUser(createMyCategory.getMyCategoryName(), user)
                .ifPresent(existingCategory -> {
                    throw new GeneralException(Code.MY_CATEGORY_DUPLICATE_NAME);
                });

        // 중복된 이름이 없으면 새로운 MyCategory 생성
        MyCategory myCategory = MyCategory.builder()
                .myCategoryName(createMyCategory.getMyCategoryName())
                .myCategoryIcon(createMyCategory.getMyCategoryIcon())
                .myCategoryScript(createMyCategory.getMyCategoryScript())
                .user(user)
                .build();

        myCategoryRepository.save(myCategory);
    }


    @Transactional
    public void modifyMyCategory(User user, Long myCategoryId, UpdateMyCategoryRequest updateMyCategory) {
        MyCategory existingMyCategory = myCategoryRepository.findByUserAndMyCategoryId(user,myCategoryId)
                .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));

        // 중복 이름 체크
        if (updateMyCategory.getMyCategoryName() != null && !updateMyCategory.getMyCategoryName().equals(existingMyCategory.getMyCategoryName())) {
            myCategoryRepository.findByMyCategoryNameAndUser(updateMyCategory.getMyCategoryName(), user)
                    .ifPresent(existingCategory -> {
                throw new GeneralException(Code.MY_CATEGORY_DUPLICATE_NAME);
            });
        }
        // 변경하려는 필드만 업데이트
        if (updateMyCategory.getMyCategoryName() != null) {
            existingMyCategory.updateMyCategoryName(updateMyCategory.getMyCategoryName());
        }

        if (updateMyCategory.getMyCategoryIcon() != null) {
            existingMyCategory.updateMyCategoryIcon(updateMyCategory.getMyCategoryIcon());
        }

        if (updateMyCategory.getMyCategoryScript() != null) {
            existingMyCategory.updateMyCategoryScript(updateMyCategory.getMyCategoryScript());
        }

        myCategoryRepository.save(existingMyCategory);


    }

    @Transactional
    public void deleteMyCategories(User user, List<Long> myCategoryIds) {
        for (Long myCategoryId : myCategoryIds) {
            MyCategory existingMyCategory = myCategoryRepository.findByUserAndMyCategoryId(user, myCategoryId)
                    .orElseThrow(() -> new GeneralException(Code.MY_CATEGORY_NOT_FOUND));

            existingMyCategory.updateStatus(BaseEntity.Status.INACTIVE);

            myCategoryRepository.save(existingMyCategory);
        }
    }

}