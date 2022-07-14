package com.cookbook.recipe.service;

import com.cookbook.recipe.common.ApplicationConstants;
import com.cookbook.recipe.entity.Ingredient;
import com.cookbook.recipe.entity.Recipe;
import com.cookbook.recipe.exception.ResourceNotFoundException;
import com.cookbook.recipe.model.FilterRequest;
import com.cookbook.recipe.repository.RecipeRepository;
import com.cookbook.recipe.util.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author gopal_re
 */
@Service("recipeService")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    /**
     * To get Recipe by id
     *
     * @param id
     * @return
     */
    @Override
    public Recipe findById(long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        Recipe recipe = recipeOptional.orElseThrow(() -> new ResourceNotFoundException(ApplicationConstants.RECIPE_ID_NOT_FOUND));
        return recipe;
    }

    /**
     * To get all recipes
     *
     * @return
     */
    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    /**
     * This method handles both save and update recipe
     *
     * @param recipe
     * @return
     */
    @Override
    public Recipe saveOrUpdateRecipe(Recipe recipe) {
        List<Ingredient> ingredient = recipe.getIngredient().stream().peek(ingrediant -> ingrediant.setRecipe(recipe)).collect(Collectors.toList());
        recipe.getIngredient().clear();
        recipe.getIngredient().addAll(ingredient);
        return recipeRepository.save(recipe);
    }

    /**
     * To delete recipe by id
     *
     * @param id
     */
    @Override
    public void deleteRecipe(long id) {
        recipeRepository.deleteById(id);
    }

    /**
     * To filter and search recipes based on filter params
     *
     * @param request
     * @return
     */
    @Override
    public Page<Recipe> searchRecipe(FilterRequest request) {
        SearchSpecification specification = new SearchSpecification(request);
        return recipeRepository.findAll(specification, PageRequest.of(request.getPageNumber() < 0 ? 0 : request.getPageNumber(), request.getNoOfRecords()));
    }


}
