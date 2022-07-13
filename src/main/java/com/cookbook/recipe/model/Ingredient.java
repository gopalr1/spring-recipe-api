package com.cookbook.recipe.model;

import com.cookbook.recipe.common.UnitOfMeasurement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;


    @Min(1)
    @Max(5000)
    private Integer amount;

    @NotNull
    private UnitOfMeasurement unitOfMeasurement;


}
