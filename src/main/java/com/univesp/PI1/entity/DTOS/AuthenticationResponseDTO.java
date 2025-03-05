package com.univesp.PI1.entity.DTOS;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseDTO {
    private final String accessToken;
    private final String refreshToken;
}
