package lk.ijse.dep10.pos.dao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private final Connection connection;

    public JdbcTemplate(Connection connection) {
        this.connection = connection;
    }

    private PreparedStatement getStatement(String sql, Object... args) throws Exception {
        PreparedStatement stm = connection.prepareStatement(sql);
        for (int i = 1; i <= args.length; i++) {
            stm.setObject(i, args[i - 1]);
        }
        return stm;
    }

    public <T> T queryForObject(String sql, Class<T> returnClass) throws Exception {
        ResultSet rst = getStatement(sql).executeQuery();
        if (!rst.next()) return null;
        rst.next();
        return rst.getObject(1, returnClass);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws Exception {
        ResultSet rst = getStatement(sql, args).executeQuery();
        if (!rst.next()) return null;
        return rowMapper.mapRow(rst, 0);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws Exception {
        ResultSet rst = getStatement(sql, args).executeQuery();
        List<T> rowList = new ArrayList<>();
        while (rst.next()) {
            rowList.add(rowMapper.mapRow(rst, rowList.size()));
        }
        return rowList;
    }

    public int update(String sql, Object... args) throws Exception {
        return getStatement(sql, args).executeUpdate();
    }

    public int update(PreparedStatementCreator pc, KeyHolder kh) throws Exception {
        PreparedStatement stm = pc.createPreparedStatement(connection);
        int affectedRows = stm.executeUpdate();
        ResultSet generatedKeys = stm.getGeneratedKeys();
        generatedKeys.next();
        int key = generatedKeys.getInt(1);
        kh.getKeys().put("1", key);
        return affectedRows;
    }
}
