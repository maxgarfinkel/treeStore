package com.maxgarfinkel.treeStore.model;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class Tree {

    private final UUID uuid;
    private final String latinName;
    private final String commonName;
    private final double[] location;

    public Tree(UUID uuid, String latinName, String commonName, double[] location){
        if(latinName == null || latinName.isBlank()){throw new IllegalArgumentException();}
        if(commonName == null || commonName.isBlank()){throw new IllegalArgumentException();}
        if(location == null || location.length != 2){throw new IllegalArgumentException();}

        this.uuid = uuid != null ? uuid : UUID.randomUUID();
        this.latinName = latinName;
        this.commonName = commonName;
        this.location = location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLatinName() {
        return latinName;
    }

    public String getCommonName() {
        return commonName;
    }

    public double[] getLocation() {
        return location;
    }
}
