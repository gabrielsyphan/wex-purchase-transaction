package com.syphan.wexpurchasetransaction.service.auth;

import com.syphan.wexpurchasetransaction.model.dto.general.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.UserEntity;
import com.syphan.wexpurchasetransaction.model.mapper.UserMapper;
import com.syphan.wexpurchasetransaction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found.")
        );
    }

    @Override
    public UserDto createUser(UserDto userDto) throws RuntimeException {
        try {
            this.logger.info("AuthServiceImpl -> createUser(): " + userDto.email());
            UserEntity userEntity = UserMapper.INSTANCE.dtoToEntity(userDto);
            userEntity.setPassword(passwordEncoder.encode(userDto.password()));
            return UserMapper.INSTANCE.entityToDto(this.userRepository.save(userEntity));
        } catch (Exception e) {
            this.logger.severe("AuthServiceImpl -> createUser(): " + e.getMessage());
            throw new RuntimeException("Error when create user.");
        }
    }
}