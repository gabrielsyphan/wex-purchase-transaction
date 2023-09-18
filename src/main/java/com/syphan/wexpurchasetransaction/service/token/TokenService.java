package com.syphan.wexpurchasetransaction.service.token;

import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.User;

public interface TokenService {

    String generateToken(UserDto user);

    String generateToken(User user);

    String generateToken(String id, String name, String email);

    String getSubject(String jwtToken);
}
