package lk.ijse.dep10.pos.api;

import lk.ijse.dep10.pos.dto.ItemDTO;
import lk.ijse.dep10.pos.dto.ResponseErrorDTO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@CrossOrigin
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private BasicDataSource pool;

    @GetMapping("/{code}")
    public ResponseEntity<?> getItem(@PathVariable String code) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
            stm.setString(1, code);
            ResultSet rst = stm.executeQuery();
            if (rst.next()){
                String description = rst.getString("description");
                int qty = rst.getInt("qty");
                BigDecimal unitPrice = rst.getBigDecimal("unit_price").setScale(2);
                ItemDTO item = new ItemDTO(code, description, qty, unitPrice);
                return new ResponseEntity<>(item, HttpStatus.OK);
            }else {
                ResponseErrorDTO error = new ResponseErrorDTO(404, "Item not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseErrorDTO error = new ResponseErrorDTO(500, "Failed to fetch the item, try again!");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
