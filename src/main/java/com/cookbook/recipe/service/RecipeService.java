package com.cookbook.recipe.service;

import com.cookbook.recipe.entity.Recipe;
import com.cookbook.recipe.model.FilterRequest;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author gopal_re
 */
public interface RecipeService {
    List<Recipe> findAll();

    Recipe findById(long id);

    Recipe saveOrUpdateRecipe(Recipe recipe);

    Page<Recipe> searchRecipe(FilterRequest request);

    void deleteRecipe(long id);
}
