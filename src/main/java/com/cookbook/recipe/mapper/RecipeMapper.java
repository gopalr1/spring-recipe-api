package com.cookbook.recipe.mapper;

import com.cookbook.recipe.model.Recipe;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @InheritInverseConfiguration
    Recipe mapToModel(com.cookbook.recipe.entity.Recipe source);

    @Mapping(target = "ingredient", source = "ingredient")
    com.cookbook.recipe.entity.Recipe mapToEntity(Recipe source);

    List<Recipe> mapToModelList(List<com.cookbook.recipe.entity.Recipe> sourceList);
}
