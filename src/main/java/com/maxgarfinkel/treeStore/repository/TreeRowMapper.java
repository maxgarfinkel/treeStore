package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TreeRowMapper implements RowMapper<Tree> {

    @Override
    public Tree mapRow(ResultSet resultSet, int i) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("uuid"));
        String latinName = resultSet.getString("latinName");
        String commonName = resultSet.getString("commonName");
        double lat = resultSet.getDouble("lat");
        double lon = resultSet.getDouble("lon");
        return new Tree(id, latinName, commonName, new double[]{lat,lon});
    }
}
