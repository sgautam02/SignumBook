package com.signumapp.mapper;

import org.mapstruct.*;

import com.signumapp.dto.UpdateUserInfoDto;
import com.signumapp.entity.User;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapstructMapperUpdate {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "accountVerified", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "profilePhoto", ignore = true)
    @Mapping(target = "country", ignore = true)
    void updateUserFromUserUpdateDto(UpdateUserInfoDto updateUserInfoDto, @MappingTarget User user);
}
