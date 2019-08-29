package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class TreeMemoryRepository implements TreeRepository {

    private final static ArrayList<Tree> db = new ArrayList<Tree>();

    @Override
    public Tree getTreeById(UUID id) {
        for(Tree tree : db){
            if(tree.getUuid().equals(id))
                return tree;
        }
        return null;
    }

    @Override
    public boolean createTree(Tree tree) {
        db.add(tree);
        return true;
    }

    @Override
    public boolean deleteTree(Tree tree) {
        return false;
    }

    @Override
    public boolean updateTree(Tree tree) {
        Tree foundTree = null;
        for(Tree treei : db){
            if(treei.getUuid().equals(tree.getUuid())){
                foundTree = treei;
                break;
            }
        }
        db.remove(foundTree);
        db.add(tree);
        return true;
    }
}
