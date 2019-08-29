package com.maxgarfinkel.treeStore.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TreeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private String latinName = "Acer";
    private String commonName = "Maple";
    private double[] location = new double[]{1.1,1.1};

    @Test
    public void getNonExistentTreeReturns404() throws Exception {
        mvc.perform(get("/tree/152c1960-3802-4cdf-ba41-a729a1117111/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()
            );
    }

    @Test
    public void creatValidTreeAndGetTreeByIdSucceeds() throws Exception {
        String uuid = "152c1960-3802-4cdf-ba41-a729a1117111";
        String validTreeJson = getDefaultValidTreeJsonWithUuid(uuid);
        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTreeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(uuid)))
                .andExpect(jsonPath("$.latinName", is(latinName)))
                .andExpect(jsonPath("$.commonName", is(commonName)))
                .andExpect(jsonPath("$.location[0]", is(location[0])))
                .andExpect(jsonPath("$.location[1]", is(location[1])))
                .andExpect(jsonPath("$.location.length()", is(location.length)));
        mvc.perform(get("/tree/"+uuid+"/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(uuid)))
                .andExpect(jsonPath("$.latinName", is(latinName)))
                .andExpect(jsonPath("$.commonName", is(commonName)))
                .andExpect(jsonPath("$.location[0]", is(location[0])))
                .andExpect(jsonPath("$.location[1]", is(location[1])))
                .andExpect(jsonPath("$.location.length()", is(location.length)));
    }

    @Test
    public void createTreeWithoutIdReturnsTreeWithId() throws Exception {
        String uuid = "";
        String treeJson = getDefaultValidTreeJsonWithUuid(uuid);
        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.latinName", is(latinName)))
                .andExpect(jsonPath("$.commonName", is(commonName)))
                .andExpect(jsonPath("$.location[0]", is(location[0])))
                .andExpect(jsonPath("$.location[1]", is(location[1])))
                .andExpect(jsonPath("$.location.length()", is(location.length)));
    }

    @Test
    public void reCreatingExistingTreeCauses409ConflictError() throws Exception {
        String uuid = "152c1960-3802-4cdf-ba41-a729a1117112";
        String treeJson = getDefaultValidTreeJsonWithUuid(uuid);
        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(uuid)))
                .andExpect(jsonPath("$.latinName", is(latinName)))
                .andExpect(jsonPath("$.commonName", is(commonName)))
                .andExpect(jsonPath("$.location[0]", is(location[0])))
                .andExpect(jsonPath("$.location[1]", is(location[1])))
                .andExpect(jsonPath("$.location.length()", is(location.length)));
        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treeJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void updatingExistingTreePersistsChanges() throws Exception {
        String uuid = "152c1960-3802-4cdf-ba41-a729a1117123";
        String validTreeJson = getDefaultValidTreeJsonWithUuid(uuid);
        String updatedTreeJson = createCustomTree(uuid,"Betula", "Birch",
                new double[]{25.1234,26.321});
        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTreeJson))
                .andExpect(status().isOk());
        mvc.perform(put("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTreeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(uuid)))
                .andExpect(jsonPath("$.latinName", is("Betula")))
                .andExpect(jsonPath("$.commonName", is("Birch")))
                .andExpect(jsonPath("$.location[0]", is(25.1234)))
                .andExpect(jsonPath("$.location[1]", is(26.321)))
                .andExpect(jsonPath("$.location.length()", is(2)));
    }

    @Test
    public void updatingNonExistentTreeCauses404Error() throws Exception {
        String treeJson = getDefaultValidTreeJsonWithUuid("152c1960-3802-4cdf-ba41-a729a1117125");
        mvc.perform(put("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(treeJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteExistingTreeSucceedsAndSubsequentGetReturn404() throws Exception {
        String uuid = "152c1960-3802-4cdf-ba41-a729a1117123";
        String validTreeJson = getDefaultValidTreeJsonWithUuid(uuid);

        mvc.perform(post("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTreeJson));
        mvc.perform(delete("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validTreeJson))
                .andExpect(status().isNoContent());
        mvc.perform(get("/tree/"+uuid+"/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNonExistentTreeCauses404Response() throws Exception {
        String nonExistentTree = getDefaultValidTreeJsonWithUuid("152c1960-3802-4cdf-ba41-a729a1117125");
        mvc.perform(delete("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nonExistentTree))
                .andExpect(status().isNotFound());
    }

    private String createCustomTree(String uuid, String latinName, String commonName, double[] location){
        return "{\"uuid\":\""+uuid+"\"," +
                " \"latinName\":\""+latinName+"\"," +
                " \"commonName\":\""+commonName+"\"," +
                " \"location\":["+Double.toString(location[0])+","+Double.toString(location[1])+"]}";
    }

    private String getDefaultValidTreeJsonWithUuid(String uuid){
        return createCustomTree(uuid,latinName,commonName,location);
    }

}
