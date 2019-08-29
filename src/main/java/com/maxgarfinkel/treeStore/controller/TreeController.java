package com.maxgarfinkel.treeStore.controller;

import com.maxgarfinkel.treeStore.repository.TreeRepository;
import com.maxgarfinkel.treeStore.model.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/tree")
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
            tree = treeRepository.getTreeById(uuid);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tree, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tree> createTree(@RequestBody Tree tree){
        //Tree newTree = new Tree(tree.getUuid(), tree.getLatinName(), tree.getCommonName(), tree.getLocation());
        boolean success = treeRepository.createTree(tree);
        if(success) return new ResponseEntity<>(tree, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping
    public ResponseEntity<Tree> updateTree(@RequestBody Tree tree){
        boolean success = false;
        try{
            success = treeRepository.updateTree(tree);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(success) return new ResponseEntity<>(tree, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping()
    public ResponseEntity<Tree> deleteTree(@RequestBody Tree tree){
        boolean success = false;
        try{
            success = treeRepository.deleteTree(tree);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(success) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
