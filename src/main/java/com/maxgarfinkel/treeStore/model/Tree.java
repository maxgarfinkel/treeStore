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
        if(latinName == null || latinName.isBlank()){throw new IllegalArgumentException("Tree latinName cannot be blank");}
        if(latinName.length() > 100){throw new IllegalArgumentException("Tree latin name can't be longer than 100 characters");}
        if(commonName == null || commonName.isBlank()){throw new IllegalArgumentException("Tree common name cannot be blank");}
        if(commonName.length() > 100){throw new IllegalArgumentException("Tree common name can't be longer than 100 characters");}
        if(location == null || location.length != 2){throw new IllegalArgumentException("Tree location must be array of length exactly");}

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
