package diegoreyesmo.imprimeboleta.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;
    private String idPos;
}
