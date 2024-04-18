package aws.aws.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UsuarioDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String img;
}
