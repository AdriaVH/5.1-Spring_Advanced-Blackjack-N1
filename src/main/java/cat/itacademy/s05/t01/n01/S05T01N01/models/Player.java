package cat.itacademy.s05.t01.n01.S05T01N01.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("player") // Your MySQL table name
@Getter
@Setter
public class Player {
    @Id
    private long id;
    private String name;
    private Integer balance;

}
