package com.cookbook.recipe.controller;

import com.cookbook.recipe.mapper.RecipeMapper;
import com.cookbook.recipe.model.FilterRequest;
import com.cookbook.recipe.model.FilterResponse;
import com.cookbook.recipe.model.Recipe;
import com.cookbook.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    private final RecipeMapper recipeMapper;


    @GetMapping("/{id}")
    public ResponseEntity<Recipe> findById(@PathVariable long id) {
        log.debug("Getting Recipe by id:" + id);
        Recipe recipeModel = recipeMapper.mapToModel(recipeService.findById(id));
        return new ResponseEntity<>(recipeModel, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> findAll() {
        log.debug("Getting all recipes");
        List<Recipe> recipeList = recipeMapper.mapToModelList(recipeService.findAll());
        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveRecipe(@Valid @RequestBody Recipe recipe) {
        log.debug("Saving new recipe");
        com.cookbook.recipe.entity.Recipe recipeEntity = recipeMapper.mapToEntity(recipe);
        com.cookbook.recipe.entity.Recipe recipeCreated = recipeService.saveOrUpdateRecipe(recipeEntity);
        log.debug("Recipe saved id" + recipeCreated.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(recipeCreated.getId()).toUri();
        return ResponseEntity.created(location).body(String.format("{\"id\":\"%s\"}", recipeCreated.getId()));
    }

    @PutMapping()
    public ResponseEntity<Object> updateRecipes(@Valid @RequestBody Recipe recipe) {
        log.debug("Updating recipe by id: " + recipe.getId());
        com.cookbook.recipe.entity.Recipe recipeEntity = recipeMapper.mapToEntity(recipe);
        Recipe recipeResponse = recipeMapper.mapToModel(recipeService.saveOrUpdateRecipe(recipeEntity));
        return ResponseEntity.ok(recipeResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(@PathVariable long id) {
        log.debug("Deleting recipe by id: " + id);
        recipeService.findById(id);
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<FilterResponse> searchRecipes(@Valid @RequestBody FilterRequest filterRequest) {

        Page<com.cookbook.recipe.entity.Recipe> pagedRecipe = recipeService.searchRecipe(filterRequest);
        List<Recipe> recipes = recipeMapper.mapToModelList(pagedRecipe.getContent());
        FilterResponse filterResponse = new FilterResponse();
        filterResponse.getRecipes().addAll(recipes);
        filterResponse.setTotalRecords(pagedRecipe.getTotalElements());
        filterResponse.setTotalPages(pagedRecipe.getTotalPages());
        return ResponseEntity.ok(filterResponse);
    }

}
