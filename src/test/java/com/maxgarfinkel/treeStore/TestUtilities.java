package com.maxgarfinkel.treeStore;

import com.maxgarfinkel.treeStore.model.Tree;

import java.util.UUID;

public class TestUtilities {

    public static final String LONG_101_LENGTH_STRING = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    public static Tree getRandomValidTree(){
        return new Tree(UUID.randomUUID(), "Acer", "Maple", new double[]{51.479275, -0.048461});
    }
}
