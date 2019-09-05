package com.maxgarfinkel.treeStore.controller;

import com.maxgarfinkel.treeStore.exceptions.DuplicateEntityException;
import com.maxgarfinkel.treeStore.model.TreeRepository;
import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/V1/tree")
public class TreeController {

    private final TreeRepository treeRepository;

    @Autowired
    public TreeController(TreeRepository treeRepository){
        this.treeRepository = treeRepository;
    }

    @RequestMapping(value = "/{uuid}/", method = RequestMethod.GET)
    public ResponseEntity<Tree> getTree(@PathVariable final UUID uuid){
        Tree tree;
        try{
            tree = treeRepository.getById(uuid);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tree> createTree(@RequestBody Tree tree){
        try {
            treeRepository.store(tree);
        }catch (DuplicateEntityException ex){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Tree> updateTree(@RequestBody Tree tree){
        boolean success = false;
        try{
            treeRepository.update(tree);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Tree> deleteTree(@RequestBody Tree tree){
        try{
            treeRepository.delete(tree);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
