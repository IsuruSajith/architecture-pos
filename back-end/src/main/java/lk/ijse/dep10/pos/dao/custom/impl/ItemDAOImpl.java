package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.ItemDAO;
import lk.ijse.dep10.pos.dao.util.JdbcTemplate;
import lk.ijse.dep10.pos.entity.Item;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import static lk.ijse.dep10.pos.dao.util.Mappers.ITEM_ROW_MAPPER;

public class ItemDAOImpl implements ItemDAO {

    private JdbcTemplate jdbcTemplate;

    public void setConnection(Connection connection) {
        jdbcTemplate = new JdbcTemplate(connection);
    }

    public long count() throws Exception {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM item", Long.class);
    }

    public Item save(Item item) throws Exception {
        jdbcTemplate.update("INSERT INTO item (code, description, qty, unit_price) VALUES (?, ?, ?, ?)", item.getCode(), item.getDescription(), item.getQty(), item.getUnitPrice());
        return item;
    }

    public void update(Item item) throws Exception {
        jdbcTemplate.update("UPDATE item SET description=?, unit_price=?, qty=? WHERE code=?", item.getDescription(), item.getUnitPrice(), item.getQty(), item.getCode());
    }

    public void deleteById(String code) throws Exception {
        jdbcTemplate.update("DELETE FROM item WHERE code=?", code);
    }

    public Optional<Item> findById(String code) throws Exception {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM item WHERE code=?", ITEM_ROW_MAPPER, code));
    }

    public List<Item> findAll() throws Exception {
        return jdbcTemplate.query("SELECT * FROM item", ITEM_ROW_MAPPER);
    }

    public boolean existsById(String code) throws Exception {
        return findById(code).isPresent();
    }
}
