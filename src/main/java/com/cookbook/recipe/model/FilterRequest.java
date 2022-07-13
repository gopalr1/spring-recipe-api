package com.cookbook.recipe.model;

import com.cookbook.recipe.common.DietType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterRequest {

    private DietType dietType;
    private Integer noOfServings;
    private String instructions;
    private String containsIngredient;
    private String notContainsIngredient;
    private Integer noOfRecords = 10;
    private Integer pageNumber = 0;

}
