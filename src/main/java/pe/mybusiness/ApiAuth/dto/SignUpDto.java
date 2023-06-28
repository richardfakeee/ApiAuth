package pe.mybusiness.ApiAuth.dto;

import java.util.Set;
import lombok.Data;
import pe.mybusiness.ApiAuth.entity.Role;

@Data
public class SignUpDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}
