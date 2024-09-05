package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table(name = "addresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "address1", nullable = false)
    private String address1;

    @Column(name = "address2", nullable = true)
    private String address2;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "type", nullable = false)
    private String type;

    @ColumnDefault("b'1'")
    @Column(name = "pick", nullable = false)
    private boolean pick;
}
