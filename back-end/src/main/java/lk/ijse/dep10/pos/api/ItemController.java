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
import java.util.ArrayList;
import java.util.List;

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
            if (rst.next()) {
                String description = rst.getString("description");
                int qty = rst.getInt("qty");
                BigDecimal unitPrice = rst.getBigDecimal("unit_price").setScale(2);
                ItemDTO item = new ItemDTO(code, description, qty, unitPrice);
                return new ResponseEntity<>(item, HttpStatus.OK);
            } else {
                ResponseErrorDTO error = new ResponseErrorDTO(404, "Item not found");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseErrorDTO error = new ResponseErrorDTO(500, "Failed to fetch the item, try again!");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getItems(@RequestParam(value = "q", required = false) String query) {
        if (query == null) query = "";
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM item WHERE code LIKE ? OR description LIKE ?");
            query = "%" + query + "%";
            stm.setString(1, query);
            stm.setString(2, query);
            ResultSet rst = stm.executeQuery();

            List<ItemDTO> itemList = new ArrayList<>();
            while (rst.next()) {
                String code = rst.getString("code");
                String description = rst.getString("description");
                int qty = rst.getInt("qty");
                BigDecimal unitPrice = rst.getBigDecimal("unit_price");
                itemList.add(new ItemDTO(code, description, qty, unitPrice));
            }
            return new ResponseEntity<>(itemList, HttpStatus.OK);
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseErrorDTO error = new ResponseErrorDTO(500, "Failed to fetch items");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveItem(@RequestBody ItemDTO item) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO item (code, description, qty, unit_price) VALUES (?, ?, ?, ?)");
            stm.setString(1, item.getCode());
            stm.setString(2, item.getDescription());
            stm.setInt(3, item.getQty());
            stm.setBigDecimal(4, item.getUnitPrice());
            stm.executeUpdate();
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getSQLState().equals("23000")) {
                return new ResponseEntity<>(new ResponseErrorDTO(HttpStatus.CONFLICT.value(), e.getMessage()), HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(new ResponseErrorDTO(500, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PatchMapping(value = "/{code}", consumes = "application/json")
    public ResponseEntity<?> updateItem(@RequestBody ItemDTO item, @PathVariable String code) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection
    .prepareStatement("UPDATE item SET description=?, qty=?, unit_price=? WHERE code=?");
            stm.setString(1, item.getDescription());
            stm.setInt(2, item.getQty());
            stm.setBigDecimal(3, item.getUnitPrice());
            stm.setString(4, code);
            int affectedRows = stm.executeUpdate();
            if (affectedRows == 1){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                ResponseErrorDTO error = new ResponseErrorDTO(404, "Item does not exist");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getSQLState().equals("23000")) {
                return new ResponseEntity<>(new ResponseErrorDTO(409, e.getMessage()),
                        HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(new ResponseErrorDTO(500, e.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteItem(@PathVariable String code){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection
                    .prepareStatement("DELETE FROM item WHERE code=?");
            stm.setString(1, code);
            int affectedRows = stm.executeUpdate();
            if (affectedRows == 1){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                ResponseErrorDTO error = new ResponseErrorDTO(404, "Item does not exist");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getSQLState().equals("23000")) {
                return new ResponseEntity<>(new ResponseErrorDTO(409, e.getMessage()),
                        HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(new ResponseErrorDTO(500, e.getMessage()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
