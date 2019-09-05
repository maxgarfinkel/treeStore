package com.maxgarfinkel.treeStore.model;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreeRepository {
    public Tree getById(UUID id);
    public void store(Tree tree);
    public void delete(Tree tree);
    public void update(Tree tree);
}
