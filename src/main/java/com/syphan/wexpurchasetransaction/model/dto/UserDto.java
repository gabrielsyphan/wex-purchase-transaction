package com.syphan.wexpurchasetransaction.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

public record UserDto (

        UUID id,

        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email is invalid.")
        String email,

        @NotBlank(message = "Password is required.")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String password
) {}
