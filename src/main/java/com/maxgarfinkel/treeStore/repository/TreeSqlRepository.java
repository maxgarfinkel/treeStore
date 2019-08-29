package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Transactional
@Repository()
public class TreeSqlRepository implements TreeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TreeSqlRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tree getTreeById(UUID id) {
        String sql = "SELECT * from tree where tree.uuid = :id";
        Map<String, String> params = Map.of("id",id.toString());
        TreeRowMapper rowMapper = new TreeRowMapper();
        List<Tree> trees = jdbcTemplate.query(sql,params, rowMapper);
        if(trees.size() == 0){
            throw new EntityNotFoundException();
        }
        return trees.get(0);
    }

    @Override
    public boolean createTree(Tree tree) {
        String sql = "INSERT INTO tree (uuid, latinName, commonName, lat, lon) "+
                "VALUES(:uuid, :latinName, :commonName, :lat, :lon);";
        Map<String, Object> params = Map.of("uuid",tree.getUuid().toString(),
                "latinName", tree.getLatinName(),
                "commonName", tree.getCommonName(),
                "lat",tree.getLocation()[0],
                "lon", tree.getLocation()[1]);
        boolean success = false;
        try{
            success = jdbcTemplate.execute(sql,params, (ps) -> {
                if(!ps.execute()){
                    return ps.getUpdateCount() == 1;
                }else{
                    return false;
                }
            });
        }catch (DuplicateKeyException ex){
            success = false;
        }
        return success;
    }

    @Override
    public boolean deleteTree(Tree tree) {
        String sql = "DELETE FROM tree WHERE tree.uuid = :uuid";
        Map<String, String> params = Map.of("uuid", tree.getUuid().toString());
        return jdbcTemplate.execute(sql, params, (ps) -> {
            if(!ps.execute()){
                if(ps.getUpdateCount() == 1){
                    return true;
                }else throw new EntityNotFoundException();
            }else return false;
        });
    }

    @Override
    public boolean updateTree(Tree tree) {
        String sql = "UPDATE tree SET latinName = :latinName, "
                        +"commonName = :commonName, lat = :lat, lon = :lon "
                        +"WHERE uuid = :uuid";
        Map<String, Object> params = Map.of("uuid", tree.getUuid().toString(),
                "latinName", tree.getLatinName(),
                "commonName", tree.getCommonName(),
                "lat", tree.getLocation()[0],
                "lon", tree.getLocation()[1]);
        return jdbcTemplate.execute(sql, params, ps -> {
            if(!ps.execute()){
                if(ps.getUpdateCount() == 1){
                    return true;
                }
            }
            throw new EntityNotFoundException();
        });
    }
}
