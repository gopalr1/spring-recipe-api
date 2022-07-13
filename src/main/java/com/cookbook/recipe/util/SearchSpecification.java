package com.cookbook.recipe.util;

import com.cookbook.recipe.common.ApplicationConstants;
import com.cookbook.recipe.entity.Ingredient;
import com.cookbook.recipe.entity.Recipe;
import com.cookbook.recipe.model.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class SearchSpecification implements Specification<Recipe> {


    private final transient FilterRequest request;

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (request.getDietType() != null) {
            predicates.add(cb.equal(root.get(ApplicationConstants.DIET_TYPE), request.getDietType()));
        }
        if (request.getInstructions() != null && !request.getInstructions().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get(ApplicationConstants.INSTRUCTIONS)), "%" + request.getInstructions().toLowerCase() + "%"));
        }
        if (request.getNoOfServings() != null) {
            predicates.add(cb.equal(root.get(ApplicationConstants.SERVINGS), request.getNoOfServings()));
        }
        if (request.getContainsIngredient() != null && !request.getContainsIngredient().isBlank()) {
            predicates.add(cb.lower(root.join(ApplicationConstants.INGREDIENT).get(ApplicationConstants.NAME)).in(request.getContainsIngredient().toLowerCase()));
        }
        if (request.getNotContainsIngredient() != null && !request.getNotContainsIngredient().isBlank()) {
            Subquery<Recipe> sq = query.subquery(Recipe.class);
            Root<Ingredient> project = sq.from(Ingredient.class);
            Join<Ingredient, Recipe> sqEmp = project.join(ApplicationConstants.RECIPE);
            sq.select(sqEmp).where(cb.lower(project.get(ApplicationConstants.NAME)).in(request.getNotContainsIngredient().toLowerCase()));
            predicates.add(cb.not(cb.in(root).value(sq)));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
