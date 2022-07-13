package com.cookbook.recipe.mapper;

import com.cookbook.recipe.common.DietType;
import com.cookbook.recipe.common.UnitOfMeasurement;
import com.cookbook.recipe.model.Ingredient;
import com.cookbook.recipe.model.Recipe;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class RecipeMapperTest {

    @InjectMocks
    RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);

    @Test
    void testToModel() {
        com.cookbook.recipe.entity.Recipe entity = createMockEntity();

        Recipe model = mapper.mapToModel(entity);
        assertThat(model.getId(), equalTo(entity.getId()));
        assertThat(model.getName(), equalTo(entity.getName()));
        assertThat(model.getDescription(), equalTo(entity.getDescription()));
        assertThat(model.getImagePath(), equalTo(entity.getImagePath()));
        assertThat(model.getDietType(), equalTo(entity.getDietType()));
        assertThat(model.getCookTime(), equalTo(entity.getCookTime()));
        assertThat(model.getPrepTime(), equalTo(entity.getPrepTime()));
        assertThat(model.getServings(), equalTo(entity.getServings()));
        assertThat(model.getIngredient(), hasSize(1));
        assertThat(model.getInstructions(), equalTo(entity.getInstructions()));
        assertThat(model.getIngredient().get(0).getName(), equalTo(entity.getIngredient().get(0).getName()));
        assertThat(model.getIngredient().get(0).getAmount(), equalTo(entity.getIngredient().get(0).getAmount()));
        assertThat(model.getIngredient().get(0).getUnitOfMeasurement(), equalTo(entity.getIngredient().get(0).getUnitOfMeasurement()));
    }

    @Test
    void testToEntity() {
        Recipe model = createMockModel();

        com.cookbook.recipe.entity.Recipe entity = mapper.mapToEntity(model);
        assertThat(entity.getId(), equalTo(model.getId()));
        assertThat(entity.getName(), equalTo(model.getName()));
        assertThat(entity.getDescription(), equalTo(model.getDescription()));
        assertThat(entity.getImagePath(), equalTo(model.getImagePath()));
        assertThat(entity.getDietType(), equalTo(model.getDietType()));
        assertThat(entity.getCookTime(), equalTo(model.getCookTime()));
        assertThat(entity.getPrepTime(), equalTo(model.getPrepTime()));
        assertThat(entity.getServings(), equalTo(model.getServings()));
        assertThat(entity.getInstructions(), equalTo(model.getInstructions()));
        assertThat(entity.getIngredient(), hasSize(1));
        assertThat(entity.getIngredient().get(0).getName(), equalTo(model.getIngredient().get(0).getName()));
        assertThat(entity.getIngredient().get(0).getAmount(), equalTo(model.getIngredient().get(0).getAmount()));
        assertThat(entity.getIngredient().get(0).getUnitOfMeasurement(), equalTo(entity.getIngredient().get(0).getUnitOfMeasurement()));
    }

    @Test
    void testToMapToModalList() {
        List<com.cookbook.recipe.entity.Recipe> entity = List.of(createMockEntity());
        List<Recipe> recipeList = mapper.mapToModelList(entity);
        assertThat(entity.size(), equalTo(recipeList.size()));
    }

    private com.cookbook.recipe.entity.Recipe createMockEntity() {
        com.cookbook.recipe.entity.Recipe recipe = new com.cookbook.recipe.entity.Recipe();
        recipe.setId(1l);
        recipe.setName("Bread");
        recipe.setDescription("Wheat bread");
        recipe.setCookTime(45);
        recipe.setPrepTime(5);
        recipe.setDietType(DietType.VEGAN);
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

    private Recipe createMockModel() {
        Recipe recipe = new Recipe();
        recipe.setId(1l);
        recipe.setName("Bread");
        recipe.setDescription("Wheat bread");
        recipe.setCookTime(45);
        recipe.setPrepTime(5);
        recipe.setDietType(DietType.VEGAN);
        recipe.setImagePath("test/bread.png");
        recipe.setServings(2);
        recipe.setInstructions("Can be easily baked in Oven");

        Ingredient ingredient = new Ingredient();
        ingredient.setName("Wheat floor");
        ingredient.setAmount(200);
        ingredient.setUnitOfMeasurement(UnitOfMeasurement.GRAM);

        recipe.getIngredient().add(ingredient);
        return recipe;
    }
}
