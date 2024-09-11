package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.Role;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Role role;
    private String status;
    private Long totalRating;



    // Convert Role to String
    public String getRoleAsString() {
        return role != null ? role.getAuthority() : "";
    }
}
