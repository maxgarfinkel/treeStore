package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.model.Tree;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeMemoryDAOTest {

    @Test
    public void savedTreeMatchesTreeSaved(){
        Tree acer = new Tree(UUID.randomUUID(), "Acer", "Maple", new double[]{1.1,1.1});
        TreeMemoryRepository treeMemoryDAO = new TreeMemoryRepository();
        treeMemoryDAO.createTree(acer);
        Tree savedAcer = treeMemoryDAO.getTreeById(acer.getUuid());
        Assert.assertEquals(acer,savedAcer);
    }

    @Test
    public void savedTreeAvailableWithSelectById(){
        Tree acer = new Tree(UUID.randomUUID(), "Acer", "Maple", new double[]{1.1,1.1});
        TreeMemoryRepository treeMemoryDAO = new TreeMemoryRepository();
        treeMemoryDAO.createTree(acer);
        Tree acerPersisted = treeMemoryDAO.getTreeById(acer.getUuid());
        Assert.assertEquals(acer, acerPersisted);
    }

    @Test
    public void getNonExistantTreeReturnsNull(){
        TreeRepository dao = new TreeMemoryRepository();
        Tree foundTree = dao.getTreeById(UUID.randomUUID());
        Assert.assertNull(foundTree);
    }

    @Test
    public void getNullIdReturnsNull(){
        TreeRepository dao = new TreeMemoryRepository();
        Tree foundTree = dao.getTreeById(null);
        Assert.assertNull(foundTree);
    }

    @Test
    public void updateTreeUpdatePersists(){
        TreeRepository dao = new TreeMemoryRepository();
        UUID id = UUID.randomUUID();
        Tree acer = new Tree(id, "Acer", "Maple", new double[]{1.1,1.1});
        Tree updatedAcer = new Tree(id, "Acer", "Maple", new double[]{1.2,1.1});
        dao.createTree(acer);
        dao.updateTree(updatedAcer);
        Tree updatedAcer2 = dao.getTreeById(id);
        Assert.assertEquals(updatedAcer, updatedAcer2);
    }
}
