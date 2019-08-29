package com.maxgarfinkel.treeStore.repository;

import com.maxgarfinkel.treeStore.TestUtillities;
import com.maxgarfinkel.treeStore.model.Tree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeSqlRepositoryIntegrationTest {

    @Autowired()
    private TreeRepository treeSqlRepository;

    @Test(expected = EntityNotFoundException.class)
    public void getNonExistentTreeThrowsException(){
        treeSqlRepository.getTreeById(UUID.randomUUID());
    }

    @Test
    public void createTreeReturnsTrueWithValidTree(){
        Tree tree = TestUtillities.getRandomValidTree();
        assertTrue(treeSqlRepository.createTree(tree));
    }

    @Test
    public void createdTreeCanBeRetrieved(){
        Tree tree1 = TestUtillities.getRandomValidTree();
        treeSqlRepository.createTree(tree1);
        Tree tree2 = treeSqlRepository.getTreeById(tree1.getUuid());
        assertEquals(tree1, tree2);
    }

    @Test
    public void attemptingToCreateAlreadyPersistedTreeCausesCreateToReturnFalse(){
        Tree tree1 = TestUtillities.getRandomValidTree();
        Tree tree1Duplicate = new Tree(tree1.getUuid(), tree1.getLatinName(), tree1.getCommonName(), tree1.getLocation());
        assertTrue(treeSqlRepository.createTree(tree1));
        assertFalse(treeSqlRepository.createTree(tree1Duplicate));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updatingNonExistentTreeCausesException(){
        Tree uncreatedTree = TestUtillities.getRandomValidTree();
        treeSqlRepository.updateTree(uncreatedTree);
    }

    @Test
    public void updatingExistingTreePersistsChanges(){
        Tree tree1 = TestUtillities.getRandomValidTree();
        treeSqlRepository.createTree(tree1);
        Tree updatedTree = new Tree(tree1.getUuid(), tree1.getLatinName(),
                tree1.getCommonName(), new double[]{14d,192.12234d});
        assertTrue(treeSqlRepository.updateTree(updatedTree));
        Tree persistedUpdatedTree = treeSqlRepository.getTreeById(updatedTree.getUuid());
        assertEquals(updatedTree, persistedUpdatedTree);
        assertNotEquals(tree1, persistedUpdatedTree);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletingNonExistentTreeThrowException(){
        Tree tree = TestUtillities.getRandomValidTree();
        treeSqlRepository.deleteTree(tree);
    }

    public void deletingExistingSuccessfullyReturnsTrue(){
        Tree tree = TestUtillities.getRandomValidTree();
        treeSqlRepository.createTree(tree);
        assertTrue(treeSqlRepository.deleteTree(tree));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletingExistingTreeRemovesTreeFromDatabase(){
        Tree tree = TestUtillities.getRandomValidTree();
        treeSqlRepository.createTree(tree);
        treeSqlRepository.deleteTree(tree);
        treeSqlRepository.getTreeById(tree.getUuid());
    }

}
