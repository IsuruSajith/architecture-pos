package lk.ijse.dep10.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements SuperEntity {
    private int id;
    private Timestamp datetime;

    public Order(Timestamp datetime) {
        this.datetime = datetime;
    }
}
