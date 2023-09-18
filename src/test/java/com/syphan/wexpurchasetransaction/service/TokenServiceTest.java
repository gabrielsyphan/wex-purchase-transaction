package com.syphan.wexpurchasetransaction.service;

import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.model.mapper.UserMapper;
import com.syphan.wexpurchasetransaction.service.token.TokenService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void generateTokenFromUserDto() {
        UserDto userDto = this.generateUserDto();
        assertDoesNotThrow(() -> {
            String token = this.tokenService.generateToken(userDto);
            this.validateToken(token, userDto.id());
        });
    }

    @Test
    public void generateTokenFromUserEntity() {
        User user = this.generateUserEntity();
        assertDoesNotThrow(() -> {
            String token = this.tokenService.generateToken(user);
            this.validateToken(token, user.getId());
        });
    }

    private void validateToken(String token, UUID id) {
        assertNotNull(token);

        String subject = this.tokenService.getSubject(token);
        assertEquals(id, UUID.fromString(subject));
    }

    private User generateUserEntity() {
        return User.builder().id((UUID.randomUUID())).name("Test").email("test@test.com").password("123456").build();
    }

    private UserDto generateUserDto() {
        return UserMapper.INSTANCE.entityToDto(this.generateUserEntity());
    }
}
