package shop.biday.model;

import jakarta.persistence.SecondaryTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.SizeModel;

import java.util.List;
import java.util.Set;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ProductTest {
    private Long id;
    private String name;
    private SizeTest sizes;
//    private Set<AuctionTest> auctions;
}
