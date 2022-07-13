package com.cookbook.recipe.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DietType {
    VEGAN("Vegan"), VEGETARIAN("Vegetarian"), NON_VEGETARIAN("Non Vegetarian"), GLUTEN_FREE("Gluten Free");
    private String label;

    public String getLabel() {
        return label;
    }
}
