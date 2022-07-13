package com.cookbook.recipe.model;

import com.cookbook.recipe.common.DietType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    private long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 500)
    private String description;

    @NotBlank
    private String imagePath;

    @NotNull
    private DietType dietType;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(50)
    private Integer servings;

    @NotBlank
    private String instructions;

    @Valid
    @NotEmpty
    private List<Ingredient> ingredient = new ArrayList<>();
}
