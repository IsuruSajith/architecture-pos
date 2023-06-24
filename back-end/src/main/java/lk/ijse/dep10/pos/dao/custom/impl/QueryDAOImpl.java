package lk.ijse.dep10.pos.dao.custom.impl;

import lk.ijse.dep10.pos.dao.custom.QueryDAO;

import java.sql.Connection;

public class QueryDAOImpl implements QueryDAO {

    private Connection connection;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
