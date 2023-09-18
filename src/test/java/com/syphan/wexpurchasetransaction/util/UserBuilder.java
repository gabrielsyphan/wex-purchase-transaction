package com.syphan.wexpurchasetransaction.util;

import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.model.mapper.UserMapper;

import java.util.UUID;

public class UserBuilder {

    public static User generateUserEntity() {
        return User.builder().id((UUID.randomUUID())).name("Test").email("test@test.com").password("123456").build();
    }

    public static UserDto generateUserDto() {
        return UserMapper.INSTANCE.entityToDto(generateUserEntity());
    }
}
