package com.syphan.wexpurchasetransaction.service.auth;

import com.syphan.wexpurchasetransaction.util.exception.InvalidUserException;
import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    UserDto createUser(UserDto userDto) throws InvalidUserException;
}
