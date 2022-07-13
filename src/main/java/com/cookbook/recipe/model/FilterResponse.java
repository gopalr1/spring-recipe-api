package com.cookbook.recipe.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FilterResponse {

    private List<Recipe> recipes = new ArrayList<>();
    private Long totalRecords;
    private Integer totalPages;
}
