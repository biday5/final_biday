package shop.biday.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {
    private Long id;
    private Long userId;
    private String address1;
    private String address2;
    private String zipcode;
    private String type;
    private boolean pick;
}
