package com.c_code.bate_ponto.dto.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private Long targetUserId;
}
