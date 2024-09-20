package shop.biday.model.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String oauthName;
    private String name;
    private String email;
    private String password;
    private String phoneNum;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Role role;
    private String status;
    private Long totalRating;

    private String zipcode;
    private String streetaddress;
    private String detailaddress;
    private String type;
    private String newPassword;


    public String getRoleAsString() {
        return role != null ? role.getAuthority() : "";
    }
}
