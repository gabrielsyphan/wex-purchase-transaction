package com.syphan.wexpurchasetransaction.controller;

import com.syphan.wexpurchasetransaction.util.exception.InvalidUserException;
import com.syphan.wexpurchasetransaction.model.entity.User;
import com.syphan.wexpurchasetransaction.service.token.TokenService;
import com.syphan.wexpurchasetransaction.model.dto.AuthLoginDto;
import com.syphan.wexpurchasetransaction.model.dto.JwtDto;
import com.syphan.wexpurchasetransaction.model.dto.UserDto;
import com.syphan.wexpurchasetransaction.service.auth.AuthService;
import com.syphan.wexpurchasetransaction.util.constant.PathConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "The auth API")
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

    @PostMapping("/signup")
    @Operation(summary = "Sign up", description = "Use this endpoint to register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid user")
    })
    public ResponseEntity<JwtDto> createUser(@RequestBody @Valid UserDto userDto, UriComponentsBuilder uriComponentsBuilder) throws InvalidUserException {
        UserDto createdUser = this.authService.createUser(userDto);
        String tokenJwt = this.tokenService.generateToken(createdUser);
        return ResponseEntity.ok(new JwtDto(tokenJwt));
    }

    @PostMapping
    @Operation(summary = "Sign in", description = "Use this endpoint to get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials")
    })
    public ResponseEntity<JwtDto> login(@RequestBody @Valid AuthLoginDto authLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authLoginDto.email(), authLoginDto.password());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        String tokenJwt = this.tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtDto(tokenJwt));
    }
}
