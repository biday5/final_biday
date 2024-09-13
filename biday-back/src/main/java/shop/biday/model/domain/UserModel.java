package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.entity.eNum.Role;

import java.time.LocalDateTime;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Role role;
    private String status;
    private Long totalRating;
}
