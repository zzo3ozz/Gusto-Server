package com.umc.gusto.domain.group.model.request;

import com.umc.gusto.global.common.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class GroupRequestDto {
    @Getter
    public static class CreateGroupDTO{
        @NotBlank
        @Size(max=10, message = "그룹 이름은 10자를 초과할 수 없습니다.")
        String groupName;
        @NotBlank
        @Size(max=20, message = "그룹 설명은 20자를 초과할 수 없습니다.")
        String groupScript;
        BaseEntity.Status status = BaseEntity.Status.ACTIVE;
    }

    @Getter
    public static class UpdateGroupDTO{
        @NotBlank
        @Size(max=10, message = "그룹 이름은 10자를 초과할 수 없습니다.")
        String groupName;
        @Size(max=50, message = "그룹 공지는 50자를 초과할 수 없습니다.")
        String notice;
    }
}
