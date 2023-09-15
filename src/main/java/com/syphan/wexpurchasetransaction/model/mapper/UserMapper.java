package com.syphan.wexpurchasetransaction.model.mapper;

import com.syphan.wexpurchasetransaction.model.dto.general.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.UserEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity dtoToEntity(UserDto dto);

    UserDto entityToDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity updateEntity(UserEntity userEntity, @MappingTarget UserEntity userEntityUpdate);
}
