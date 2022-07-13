package com.cookbook.recipe.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UnitOfMeasurement {
    GRAM("Gram"), MILLI_LITRE("Milli_Litre"), OUNCE("Onunce"), TABLE_SPOON("Table_Spoon"), NUMBERS("nos");
    private String label;

    public String getLabel() {
        return label;
    }
}
