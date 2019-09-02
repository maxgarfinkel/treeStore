package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TreeRepository {
    public Tree getTreeById(UUID id);
    public void createTree(Tree tree);
    public void deleteTree(Tree tree);
    public void updateTree(Tree tree);
}
