package com.syphan.wexpurchasetransaction.controller;

import com.syphan.wexpurchasetransaction.authentication.TokenService;
import com.syphan.wexpurchasetransaction.model.dto.general.AuthLoginDto;
import com.syphan.wexpurchasetransaction.model.dto.general.JwtDto;
import com.syphan.wexpurchasetransaction.model.dto.general.UserDto;
import com.syphan.wexpurchasetransaction.model.entity.UserEntity;
import com.syphan.wexpurchasetransaction.service.auth.AuthService;
import com.syphan.wexpurchasetransaction.util.constant.PathConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;


@RestController
@RequestMapping(value = PathConstants.BASE_PATH_AUTH, produces = "application/json", consumes = "application/json")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Autowired
    public AuthController(
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            AuthService authService
    ) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<JwtDto> createUser(@RequestBody @Valid UserDto userDto, UriComponentsBuilder uriComponentsBuilder) throws RuntimeException {
        UserDto createdUser = this.authService.createUser(userDto);
        String tokenJwt = this.tokenService.generateToken(createdUser);
        return ResponseEntity.ok(new JwtDto(tokenJwt));
    }

    @PostMapping
    public ResponseEntity<JwtDto> login(@RequestBody @Valid AuthLoginDto authLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authLoginDto.email(), authLoginDto.password());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        String tokenJwt = this.tokenService.generateToken((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtDto(tokenJwt));
    }
}
