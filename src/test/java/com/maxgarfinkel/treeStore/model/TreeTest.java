package com.maxgarfinkel.treeStore.model;

import com.maxgarfinkel.treeStore.TestUtilities;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TreeTest {

    @Test
    public void newTreeWithNullIdCreatesRandomId(){
        Tree tree1 = new Tree(null, "acer","maple", new double[]{1d,1.1d});
        Tree tree2 = new Tree(null, "acer","maple", new double[]{1d,1.1d});
        assertNotSame(tree1.getUuid(), tree2.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWithNullLatinNameThrowsException(){
        new Tree(UUID.randomUUID(), null,"maple", new double[]{1d,1.1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWithEmptyLatinNameThrowsException(){
        new Tree(UUID.randomUUID(), "","maple", new double[]{1d,1.1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWith101CharacterLatinNameThrowsException(){
        new Tree(UUID.randomUUID(), TestUtilities.LONG_101_LENGTH_STRING,"maple", new double[]{1d,1.1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWithNullCommonNameThrowsException() {
        new Tree(UUID.randomUUID(), "acer", null, new double[]{1d, 1.1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWithEmptyCommonNameThrowsException() {
        new Tree(UUID.randomUUID(), "acer", "", new double[]{1d, 1.1d});
    }

    @Test(expected = IllegalArgumentException.class)
    public void newTreeWith101CharacterCommonNameThrowsException() {
        new Tree(UUID.randomUUID(), "acer", TestUtilities.LONG_101_LENGTH_STRING, new double[]{1d, 1.1d});
    }

    @Test(expected =  IllegalArgumentException.class)
    public void locationOfNoUnitsThrowsException(){
        new Tree( UUID.randomUUID(), "Acer", "Maple", new double[]{});
    }

    @Test(expected =  IllegalArgumentException.class)
    public void locationOfOneUnitThrowsException(){
        new Tree( UUID.randomUUID(), "Acer", "Maple", new double[]{1.1d});
    }

    @Test(expected =  IllegalArgumentException.class)
    public void locationOfThreeUnitsThrowsException(){
        new Tree( UUID.randomUUID(), "Acer", "Maple", new double[]{1.1d,1.1d,2.1d});
    }

    @Test
    public void validArgumentsCreateCorrectTree(){
        double[] coord = new double[]{1d,1.1d};
        UUID id = UUID.randomUUID();
        Tree acer = new Tree(id, "Acer", "Maple", coord);
        assertEquals(id, acer.getUuid());
        assertEquals("Maple", acer.getCommonName());
        assertEquals("Acer", acer.getLatinName());
        assertArrayEquals(coord, acer.getLocation(), Double.MIN_NORMAL);
    }

    @Test
    public void equalityRequiresAllPropertiesToMatch(){
        double[] coord1 = new double[]{1d,1d};
        UUID id1 = UUID.randomUUID();
        Tree tree1 = new Tree(id1, "Tree1","Tree1",coord1);
        Tree tree2 = new Tree(id1, "Tree1","Tree1", coord1);
        Tree tree3 = new Tree(id1, "Tree3","Tree1", coord1);
        assertEquals(tree1,tree2);
        assertNotEquals(tree1,tree3);
    }

}
