package diegoreyesmo.imprimeboleta.dto;

import lombok.Data;

@Data
public class DocumentoRequestDTO {
    private String tipodocumento;
    private String monto;
    private String username;
    private String password;
    private String idPos;
}
