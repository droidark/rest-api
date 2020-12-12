package xyz.krakenkat.restapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Schema(description = "All details about the post")
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
