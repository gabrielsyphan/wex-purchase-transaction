package com.syphan.wexpurchasetransaction.model.dto.general;

import javax.validation.constraints.NotBlank;

public record AuthLoginDto (

        @NotBlank(message = "Email is required.")
        String email,

        @NotBlank(message = "Password is required.")
        String password
) { }
