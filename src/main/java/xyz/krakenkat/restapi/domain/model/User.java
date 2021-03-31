package xyz.krakenkat.restapi.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Schema(description = "All details about the user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Size(min = 2, message = "Name should have at least two characters.")
    @Schema(description = "Name should have at least two characters")   // Legacy annotation: @ApiModelProperty
    private String name;

    private String password;

    @Past
    @Schema(description = "Birth date should be in the past.")
    private Date birthDay;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
}
