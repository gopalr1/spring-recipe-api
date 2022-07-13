package com.cookbook.recipe.controller;


import com.cookbook.recipe.TestUtils;
import com.cookbook.recipe.common.DietType;
import com.cookbook.recipe.common.UnitOfMeasurement;
import com.cookbook.recipe.model.Ingredient;
import com.cookbook.recipe.model.Recipe;
import com.cookbook.recipe.model.FilterRequest;
import com.cookbook.recipe.repository.RecipeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
public class RecipeControllertest {

    private static final String localhostUri = "http://localhost:";
    @MockBean
    RecipeRepository recipeRepository;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllCustomersSuccess() {
        when(recipeRepository.findAll()).thenReturn(List.of(createMockEntity()));
        ResponseEntity<String> response = restTemplate.getForEntity("/api/recipe",
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testGetRecipeNotFound() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<String> response = restTemplate.getForEntity(localhostUri + port + "/api/recipe/1",
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }


    @Test
    public void testGetRecipeSuccess() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(createMockEntity()));
        ResponseEntity<String> response = restTemplate.getForEntity(localhostUri + port + "/api/recipe/1",
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testSaveRecipeSuccess() throws JSONException {
        when(recipeRepository.save(any(com.cookbook.recipe.entity.Recipe.class)))
                .thenReturn(createMockEntity());
        HttpEntity<Recipe> httpEntity = new HttpEntity<>(createMockModel(false));
        ResponseEntity<String> response = restTemplate.postForEntity(localhostUri + port + "/api/recipe/", httpEntity,
                String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals("{\"id\":\"1\"}", response.getBody(), true);

    }

    @Test
    public void testSaveRecipeNameValidationFailure() throws JSONException {
        when(recipeRepository.save(any(com.cookbook.recipe.entity.Recipe.class)))
                .thenReturn(createMockEntity());

        Recipe recipe = createMockModel(false);
        recipe.setName("T");

        HttpEntity<Recipe> httpEntity = new HttpEntity<>(recipe);
        ResponseEntity<String> response = restTemplate.postForEntity(localhostUri + port + "/api/recipe/", httpEntity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSaveRecipeDietTypeValidationFailure() throws JSONException {
        when(recipeRepository.save(any(com.cookbook.recipe.entity.Recipe.class)))
                .thenReturn(createMockEntity());

        Recipe recipe = createMockModel(false);
        recipe.setDietType(null);

        HttpEntity<Recipe> httpEntity = new HttpEntity<>(recipe);
        ResponseEntity<String> response = restTemplate.postForEntity(localhostUri + port + "/api/recipe/", httpEntity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSaveRecipeNullIngredientFailure() throws JSONException {
        when(recipeRepository.save(any(com.cookbook.recipe.entity.Recipe.class)))
                .thenReturn(createMockEntity());

        Recipe recipe = createMockModel(false);
        recipe.setIngredient(null);

        HttpEntity<Recipe> httpEntity = new HttpEntity<>(recipe);
        ResponseEntity<String> response = restTemplate.postForEntity(localhostUri + port + "/api/recipe/", httpEntity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testUpdateRecipeSuccess() throws JSONException {
        when(recipeRepository.save(any(com.cookbook.recipe.entity.Recipe.class)))
                .thenReturn(createMockEntity());
        Recipe recipe = createMockModel(true);
        HttpEntity<Recipe> httpEntity = new HttpEntity<>(recipe);
        ResponseEntity<String> response = restTemplate.exchange("/api/recipe/", HttpMethod.PUT, httpEntity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        JSONAssert.assertEquals(TestUtils.asJsonString(createMockModel(true)), response.getBody(), true);

    }

    @Test
    public void testDeleteRecipeFailure()  {
        when(recipeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        HttpEntity<Recipe> httpEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = restTemplate.exchange("/api/recipe/1", HttpMethod.DELETE, httpEntity,
                String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchRecipeSuccess() {
        Page<com.cookbook.recipe.entity.Recipe> page = new PageImpl(List.of(createMockEntity()), PageRequest.of(0, 10), 1);
        when(recipeRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(page);
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDietType(DietType.GLUTEN_FREE);
        filterRequest.setNoOfServings(2);
        filterRequest.setNoOfRecords(10);
        filterRequest.setPageNumber(0);
        HttpEntity<FilterRequest> httpEntity = new HttpEntity<>(filterRequest);
        ResponseEntity<String> response = restTemplate.exchange("/api/recipe/search", HttpMethod.POST, httpEntity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().contains("\"totalRecords\":1"));
    }

    @Test
    public void testEmptySearchRecipeSuccess() {
        Page<com.cookbook.recipe.entity.Recipe> page = new PageImpl(List.of(createMockEntity()), PageRequest.of(0, 10), 1);
        when(recipeRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(page);
        FilterRequest filterRequest = new FilterRequest();
        HttpEntity<FilterRequest> httpEntity = new HttpEntity<>(filterRequest);
        ResponseEntity<String> response = restTemplate.exchange("/api/recipe/search", HttpMethod.POST, httpEntity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().contains("\"totalRecords\":1"));
    }

    @Test
    public void testSearchRecipeNoResult() {
        Page<com.cookbook.recipe.entity.Recipe> page = new PageImpl(List.of(), PageRequest.of(0, 10), 0);
        when(recipeRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(page);
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDietType(DietType.NON_VEGETARIAN);
        filterRequest.setNoOfServings(2);
        filterRequest.setNoOfRecords(10);
        filterRequest.setPageNumber(0);
        HttpEntity<FilterRequest> httpEntity = new HttpEntity<>(filterRequest);
        ResponseEntity<String> response = restTemplate.exchange("/api/recipe/search", HttpMethod.POST, httpEntity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().contains("\"totalRecords\":0"));
    }



    private com.cookbook.recipe.entity.Recipe createMockEntity() {
        com.cookbook.recipe.entity.Recipe recipe = new com.cookbook.recipe.entity.Recipe();
        recipe.setId(1l);
        recipe.setName("Bread");
        recipe.setDescription("Wheat bread");
        recipe.setCookTime(45);
        recipe.setPrepTime(5);
        recipe.setDietType(DietType.VEGETARIAN);
        recipe.setImagePath("test/bread.png");
        recipe.setServings(2);
        recipe.setInstructions("Can be baked baked in Oven");

        com.cookbook.recipe.entity.Ingredient ingredient = new com.cookbook.recipe.entity.Ingredient();
        ingredient.setId(1l);
        ingredient.setName("Wheat floor");
        ingredient.setAmount(200);
        ingredient.setUnitOfMeasurement(UnitOfMeasurement.GRAM);

        recipe.getIngredient().add(ingredient);
        return recipe;
    }


    private Recipe createMockModel(boolean withId) {
        Recipe recipe = new Recipe();
        if (withId) {
            recipe.setId(1l);
        }
        recipe.setName("Bread");
        recipe.setDescription("Wheat bread");
        recipe.setCookTime(45);
        recipe.setPrepTime(5);
        recipe.setDietType(DietType.VEGETARIAN);
        recipe.setImagePath("test/bread.png");
        recipe.setServings(2);
        recipe.setInstructions("Can be baked baked in Oven");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Wheat floor");
        ingredient.setAmount(200);
        ingredient.setUnitOfMeasurement(UnitOfMeasurement.GRAM);

        recipe.getIngredient().add(ingredient);
        return recipe;
    }
}
