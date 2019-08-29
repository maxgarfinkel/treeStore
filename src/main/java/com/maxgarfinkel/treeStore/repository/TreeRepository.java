package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreeRepository {
    public Tree getTreeById(UUID id);
    public boolean createTree(Tree tree);
    public boolean deleteTree(Tree tree);
    public boolean updateTree(Tree tree);
}
