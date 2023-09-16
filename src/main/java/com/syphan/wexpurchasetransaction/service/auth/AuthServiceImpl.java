package com.syphan.wexpurchasetransaction.service.auth;

import com.syphan.wexpurchasetransaction.exception.InvalidUserException;
import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.model.mapper.UserMapper;
import com.syphan.wexpurchasetransaction.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserDto createUser(UserDto userDto) throws InvalidUserException {
        try {
            this.logger.info("AuthServiceImpl -> createUser(): " + userDto.email());
            User user = UserMapper.INSTANCE.dtoToEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.password()));

            if(this.userRepository.findByEmail(userDto.email()).isPresent()) {
                throw new InvalidUserException("Email already exists.");
            }

            return UserMapper.INSTANCE.entityToDto(this.userRepository.save(user));
        } catch (InvalidUserException e) {
            this.logger.severe("AuthServiceImpl -> createUser(): " + e.getMessage());
            throw e;
        } catch (Exception e) {
            this.logger.severe("AuthServiceImpl -> createUser(): Error when create user. " + e.getMessage());
            throw new InvalidUserException("Error when create user.");
        }
    }
}