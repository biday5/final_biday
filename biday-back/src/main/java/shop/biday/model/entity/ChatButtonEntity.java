package shop.biday.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
@Table(name = "chat_button")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatButtonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "button_name", nullable = false)
    private String buttonName;

    @Column(name = "button_link", nullable = false)
    private String buttonLink;

    @Column(name = "button_img", nullable = false)
    private String buttonImg;

    @Column(name = "button_description", nullable = false)
    private String buttonDescription;
}