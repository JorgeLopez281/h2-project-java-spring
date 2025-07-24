package com.tecnova.technical_test.domain.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterResponse {
    private String status;
    private String userName;
    private String role;
}
