package cat.itacademy.s05.t01.n01.S05T01N01.models;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("player") // Your MySQL table name
public class Player {
    @Id
    private long id;
    private String name;
    private Integer balance;
    private int gamesWon;
    private int gamesPlayed;

}
