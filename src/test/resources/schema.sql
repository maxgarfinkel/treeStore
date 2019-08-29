CREATE SCHEMA treeDB AUTHORIZATION sa;
CREATE TABLE tree (
    uuid VARCHAR(36) NOT NULL,
    latinName VARCHAR(45) NOT NULL,
    commonName VARCHAR(45) NOT NULL,
    lat DOUBLE NOT NULL,
    lon DOUBLE NOT NULL,
    PRIMARY KEY (uuid)
)