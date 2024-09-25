package shop.biday.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupedProduct {
    Long id;
    String name;
    List<GroupedSize> sizesWithAuctions; // 사이즈와 해당 경매 ID 리스트
}
