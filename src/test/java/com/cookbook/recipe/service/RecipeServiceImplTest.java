package com.cookbook.recipe.service;

import com.cookbook.recipe.common.DietType;
import com.cookbook.recipe.common.UnitOfMeasurement;
import com.cookbook.recipe.entity.Recipe;
import com.cookbook.recipe.exception.ResourceNotFoundException;
import com.cookbook.recipe.model.FilterRequest;
import com.cookbook.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    private static final Long ID = 1l;
    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;


    @Test
    void testFindAll() {
        when(recipeRepository.findAll()).thenReturn(List.of(createMockEntity()));
        assertThat(1, equalTo(recipeService.findAll().size()));
    }

    @Test
    void testFindById() {
        when(recipeRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(createMockEntity()));
        assertThat(1l, is(recipeService.findById(1l).getId()));
    }

    @Test
    void testFindByIdNotFound() {
        when(recipeRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> recipeService.findById(1l));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testSaveOrUpdateRecipe() {
        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(createMockEntity());
        assertThat(ID, equalTo(recipeService.saveOrUpdateRecipe(new Recipe()).getId()));
    }

    @Test
    void testUpdate() {
        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(createMockEntity());
        assertThat(ID, equalTo(recipeService.saveOrUpdateRecipe(createMockEntity()).getId()));
    }

    @Test
    void testSearchRecipe() {
        Page<Recipe> page = new PageImpl(List.of(createMockEntity()), PageRequest.of(0, 10), 1);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        Root<Recipe> root = mock(Root.class);


        Path<Object> dietTypePath = mock(Path.class);
        Path<Object> noOfServingsPath = mock(Path.class);
        Path<Object> instructionsPath = mock(Path.class);

        when(root.get("dietType")).thenReturn(dietTypePath);
        when(root.get("servings")).thenReturn(noOfServingsPath);
        when(root.get("instructions")).thenReturn(instructionsPath);

        Predicate dietTypePredicate = mock(Predicate.class);
        Predicate noOfServingsPredicate = mock(Predicate.class);
        Predicate instructionsPredicate = mock(Predicate.class);

        when(criteriaBuilder.equal(root.get("dietType"), DietType.VEGETARIAN)).thenReturn(dietTypePredicate);
        when(criteriaBuilder.equal(root.get("servings"), 2)).thenReturn(noOfServingsPredicate);
        when(criteriaBuilder.like(criteriaBuilder.lower(root.get("instructions")), "%oven%")).thenReturn(instructionsPredicate);
        Predicate finalPredicate = mock(Predicate.class);

        when(criteriaBuilder.and(dietTypePredicate, instructionsPredicate,noOfServingsPredicate)).thenReturn(finalPredicate);

        when(recipeRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(page);
        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setDietType(DietType.VEGETARIAN);
        filterRequest.setNoOfServings(2);
        filterRequest.setInstructions("Oven");
        filterRequest.setNoOfRecords(10);
        filterRequest.setPageNumber(0);
        Page<Recipe> result = recipeService.searchRecipe(filterRequest);

        assertEquals(1, result.get().count());

        ArgumentCaptor<Specification<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(recipeRepository).findAll(argumentCaptor.capture(), eq(PageRequest.of(0, 10)));
        Specification<Recipe> specificationValue = argumentCaptor.getValue();
        assertEquals(finalPredicate, specificationValue.toPredicate(root, criteriaQuery, criteriaBuilder));
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
        recipe.setInstructions("Can be easily baked in Oven");

        com.cookbook.recipe.entity.Ingredient ingredient = new com.cookbook.recipe.entity.Ingredient();
        ingredient.setId(1l);
        ingredient.setName("Wheat floor");
        ingredient.setAmount(200);
        ingredient.setUnitOfMeasurement(UnitOfMeasurement.GRAM);

        recipe.getIngredient().add(ingredient);
        return recipe;
    }
}
