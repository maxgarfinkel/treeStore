package com.maxgarfinkel.treeStore.infrastructure;

import com.maxgarfinkel.treeStore.TestUtilities;
import com.maxgarfinkel.treeStore.exceptions.DuplicateEntityException;
import com.maxgarfinkel.treeStore.model.Tree;
import com.maxgarfinkel.treeStore.model.TreeRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeSqlRepositoryIntegrationTest {

    @Rule public ExpectedException exception = ExpectedException.none();

    @Autowired()
    private TreeRepository treeSqlRepository;

    @Test(expected = EntityNotFoundException.class)
    public void getNonExistentTreeThrowsException(){
        treeSqlRepository.getById(UUID.randomUUID());
    }

    @Test
    public void createTreeSucceedsWithValidTree(){
        Tree tree = TestUtilities.getRandomValidTree();
        treeSqlRepository.store(tree);
    }

    @Test
    public void createdTreeCanBeRetrieved(){
        Tree tree1 = TestUtilities.getRandomValidTree();
        treeSqlRepository.store(tree1);
        Tree tree2 = treeSqlRepository.getById(tree1.getUuid());
        assertEquals(tree1, tree2);
    }

    @Test(expected = DuplicateEntityException.class)
    public void attemptingToCreateAlreadyPersistedTreeCausesException(){
        Tree tree1 = TestUtilities.getRandomValidTree();
        Tree tree1Duplicate = new Tree(tree1.getUuid(), tree1.getLatinName(), tree1.getCommonName(), tree1.getLocation());
        treeSqlRepository.store(tree1);
        treeSqlRepository.store(tree1Duplicate);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updatingNonExistentTreeCausesException(){
        Tree uncreatedTree = TestUtilities.getRandomValidTree();
        treeSqlRepository.update(uncreatedTree);
    }

    @Test
    public void updatingExistingTreePersistsChanges(){
        Tree tree1 = TestUtilities.getRandomValidTree();
        treeSqlRepository.store(tree1);
        Tree updatedTree = new Tree(tree1.getUuid(), tree1.getLatinName(),
                tree1.getCommonName(), new double[]{14d,192.12234d});
        treeSqlRepository.update(updatedTree);
        Tree persistedUpdatedTree = treeSqlRepository.getById(updatedTree.getUuid());
        assertEquals(updatedTree, persistedUpdatedTree);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletingNonExistentTreeThrowException(){
        Tree tree = TestUtilities.getRandomValidTree();
        treeSqlRepository.delete(tree);
    }

    @Test
    public void deletingExistingTreeRemovesTreeFromDatabase(){
        Tree tree = TestUtilities.getRandomValidTree();
        treeSqlRepository.store(tree);
        treeSqlRepository.delete(tree);
        exception.expect(EntityNotFoundException.class);
        treeSqlRepository.getById(tree.getUuid());
    }

}
