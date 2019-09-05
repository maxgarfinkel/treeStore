package com.maxgarfinkel.treeStore.infrastructure;

import com.maxgarfinkel.treeStore.exceptions.DuplicateEntityException;
import com.maxgarfinkel.treeStore.model.Tree;
import com.maxgarfinkel.treeStore.model.TreeRepository;
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
    public Tree getById(UUID id) {
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
    public void store(Tree tree) {
        String sql = "INSERT INTO tree (uuid, latinName, commonName, lat, lon) "+
                "VALUES(:uuid, :latinName, :commonName, :lat, :lon);";
        Map<String, Object> params = Map.of("uuid",tree.getUuid().toString(),
                "latinName", tree.getLatinName(),
                "commonName", tree.getCommonName(),
                "lat",tree.getLocation()[0],
                "lon", tree.getLocation()[1]);

        try{
            jdbcTemplate.update(sql, params);
        }catch (DuplicateKeyException ex){
            throw new DuplicateEntityException("Duplicate tree creation attempted", ex);
        }
    }

    @Override
    public void delete(Tree tree) {
        String sql = "DELETE FROM tree WHERE tree.uuid = :uuid";
        Map<String, String> params = Map.of("uuid", tree.getUuid().toString());
        if(jdbcTemplate.update(sql, params) == 0)
            throw new EntityNotFoundException("Delete failed. Tree: " + tree.getUuid() + " doesn't exist.");
    }

    @Override
    public void update(Tree tree) {
        String sql = "UPDATE tree SET latinName = :latinName, "
                        +"commonName = :commonName, lat = :lat, lon = :lon "
                        +"WHERE uuid = :uuid";
        Map<String, Object> params = Map.of("uuid", tree.getUuid().toString(),
                "latinName", tree.getLatinName(),
                "commonName", tree.getCommonName(),
                "lat", tree.getLocation()[0],
                "lon", tree.getLocation()[1]);
        if(jdbcTemplate.update(sql,params) == 0){
            throw new EntityNotFoundException("Update failed. Tree: " + tree.getUuid() + " doesn't exist.");
        }
    }
}
