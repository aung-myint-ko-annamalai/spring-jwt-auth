package com.ucsy.springjwtauth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPairs {

    private String accessToken;
    private String refreshToken;

}
