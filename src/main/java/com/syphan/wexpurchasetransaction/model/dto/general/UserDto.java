package com.syphan.wexpurchasetransaction.model.dto.general;

import javax.validation.constraints.NotBlank;

public record UserDto (

        Long id,

        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Email is required.")
        String email,

        @NotBlank(message = "Password is required.")
        String password
) {}
