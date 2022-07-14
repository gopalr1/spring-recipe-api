package com.cookbook.recipe.entity;


import com.cookbook.recipe.common.DietType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gopal_re
 */
@Getter
@Setter
@Entity(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "diet_type")
    @Enumerated(EnumType.STRING)
    private DietType dietType;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cook_time")
    private Integer cookTime;

    private Integer servings;

    private String instructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonBackReference
    private List<Ingredient> ingredient = new ArrayList<>();
}
