package shop.biday.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
public class SizeTest {
    private String size;
    private AuctionTest auctions;
}
