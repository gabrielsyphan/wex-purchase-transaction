package com.syphan.wexpurchasetransaction.model.mapper;

import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToEntity(UserDto dto);

    @Mapping(target = "password", ignore = true)
    UserDto entityToDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User updateEntity(User user, @MappingTarget User userUpdate);
}
