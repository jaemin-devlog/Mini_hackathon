package org.likelion.couplediray.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    private Long userId;
    private String code;
}