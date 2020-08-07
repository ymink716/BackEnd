package kr.or.watermelon.show.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    private Integer runningTime;

    @Length(max = 8200)
    private String imgUrl;

    @Length(max = 8200)
    private String thumbnailImgUrl;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;

    private LocalDateTime releaseStartTime;

    private LocalDateTime releaseEndTime;

    @Enumerated(EnumType.ORDINAL)
    private Category category;

    @Enumerated(EnumType.ORDINAL)
    private RRatedType rRated;

    @ManyToOne
    private Place place;

    @ManyToMany(mappedBy = "products")
    @Builder.Default
    private List<Artist> artists = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public List<String> getArtistNames() {
        return artists.stream()
                .map(a -> a.getName())
                .limit(3)
                .collect(Collectors.toList());
    }

    public static class ProductBuilder implements CustomBuilder {
    }
}
