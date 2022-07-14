package com.cookbook.recipe.util;

import com.cookbook.recipe.common.ApplicationConstants;
import com.cookbook.recipe.entity.Ingredient;
import com.cookbook.recipe.entity.Recipe;
import com.cookbook.recipe.model.FilterRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gopal_re
 */
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
        if (!StringUtils.isBlank(request.getInstructions())) {
            predicates.add(cb.like(cb.lower(root.get(ApplicationConstants.INSTRUCTIONS)), "%" + request.getInstructions().toLowerCase() + "%"));
        }
        if (request.getNoOfServings() != null) {
            predicates.add(cb.equal(root.get(ApplicationConstants.SERVINGS), request.getNoOfServings()));
        }
        if (!StringUtils.isBlank(request.getContainsIngredient())) {
            predicates.add(cb.lower(root.join(ApplicationConstants.INGREDIENT).get(ApplicationConstants.NAME)).in(request.getContainsIngredient().toLowerCase()));
        }
        if (!StringUtils.isBlank(request.getNotContainsIngredient())) {
            Subquery<Recipe> sq = query.subquery(Recipe.class);
            Root<Ingredient> project = sq.from(Ingredient.class);
            Join<Ingredient, Recipe> sqEmp = project.join(ApplicationConstants.RECIPE);
            sq.select(sqEmp).where(cb.lower(project.get(ApplicationConstants.NAME)).in(request.getNotContainsIngredient().toLowerCase()));
            predicates.add(cb.not(cb.in(root).value(sq)));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
