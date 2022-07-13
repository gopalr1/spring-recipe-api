### Spring Boot Application - spring-recipe-api

## Background
Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.
   For example, the API should be able to handle the following search requests:
   • All vegetarian recipes
   • Recipes that can serve 4 persons and have “potatoes” as an ingredient
   • Recipes without “salmon” as an ingredient that has “oven” in the instructions.

Technical requirements:

- Java 11 runtime
- Spring Boot 2
- H2 Database


## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites
For building and running the application you need:

- Java 11 Runtime
- Maven 3

Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the package com.cookbook.recipe.RecipeApplication from your IDE.


Alternatively you can use the Spring Boot Maven plugin like so:

mvn spring-boot:run

## CustomerDetail API
The Application exposes REST Endpoints as below

API Request:

Get All Recipes - http://localhost:8080/api/recipe Http Method : GET

Get Recipe by id - http://localhost:8080/api/recipe/{id} Http Method : GET

Filter recipe by 5 criterias dietType,noOfServings,instructions, containsIngredient, notContainsIngredient  
                 - http://localhost:8080/api/recipe/search Http Method : POST
Request:
```
      {
         "dietType":"VEGETARIAN",
         "noOfServings":2,
         "instructions":"Oven",
         "containsIngredient":"Wheat Floor",
         "notContainsIngredient":"Oil",
         "noOfRecords":10,
         "pageNumber":0
      }
```
Add Recipe - http://localhost:8080/api/recipe Http Method : POST
Request:
```
        {
        "name":"Omlette",
        "description":"The dish made by frying raw egg on cooking pan",
        "imagePath":"test/omlette.png",
        "dietType":"NON_VEGETARIAN",
        "prepTime":5,
        "cookTime":3,
        "servings":2,
        "instructions":"Can also baked in oven",
        "ingredient":[
                        {
                        "name":"Egg",
                        "amount":2,
                        "unitOfMeasurement":"NUMBERS"
                        },
                        {
                        "name":"SALT",
                        "amount":1,
                        "unitOfMeasurement":"TABLE_SPOON"
                        },
                        {
                        "name":"PEPPER",
                        "amount":1,
                        "unitOfMeasurement":"TABLE_SPOON"
                        }
                    ]
        }
```
Update Recipe - http://localhost:8080/api/recipe Http Method : PUT
Request:
```
        {
        "id": 1,
        "name":"Omlette",
        "description":"The dish made by frying raw egg on cooking pan",
        "imagePath":"test/omlette.png",
        "dietType":"NON_VEGETARIAN",
        "prepTime":5,
        "cookTime":3,
        "servings":2,
        "instructions":"Can also easily baked in oven",
        "ingredient":[
                        {
                        "name":"Egg",
                        "amount":2,
                        "unitOfMeasurement":"NUMBERS"
                        },
                        {
                        "name":"SALT",
                        "amount":1,
                        "unitOfMeasurement":"TABLE_SPOON"
                        },
                        {
                        "name":"PEPPER",
                        "amount":1,
                        "unitOfMeasurement":"TABLE_SPOON"
                        },
                        {
                        "name":"ONION",
                        "amount":1,
                        "unitOfMeasurement":"NUMBERS"
                        }
                    ]
        }
```

Delete Recipe by id - http://localhost:8080/api/recipe/{id} Http Method : DELETE

![ScreenShot](https://raw.github.com/gopalr1/CustomerDetailAPI/master/PostMan.JPG)


Sample JSON Response for get Recipe by id:
```
{
    "id": 8,
    "name": "Omlette",
    "description": "The dish made by frying raw egg on cooking pan",
    "imagePath": "test/omlette.png",
    "dietType": "NON_VEGETARIAN",
    "prepTime": 5,
    "cookTime": 3,
    "servings": 2,
    "instructions": "Can also baked in oven",
    "ingredient": [
        {
            "name": "Egg",
            "amount": 2,
            "unitOfMeasurement": "NUMBERS"
        },
        {
            "name": "SALT",
            "amount": 1,
            "unitOfMeasurement": "TABLE_SPOON"
        },
        {
            "name": "PEPPER",
            "amount": 1,
            "unitOfMeasurement": "TABLE_SPOON"
        }
    ]
}
```

Sample Search Response
```
{
   "recipes":[
      {
         "id":3,
         "name":"French Fries",
         "description":"The dish made by frying Potatoes",
         "imagePath":"test/Fries.png",
         "dietType":"VEGETARIAN",
         "prepTime":5,
         "cookTime":15,
         "servings":3,
         "instructions":"Can be cooked in pan",
         "ingredient":[
            {
               "name":"Potatoes",
               "amount":500,
               "unitOfMeasurement":"GRAM"
            },
            {
               "name":"Salt",
               "amount":1,
               "unitOfMeasurement":"TABLE_SPOON"
            }
         ]
      }
   ],
   "totalRecords":1,
   "totalPages":1
}

```


## Swagger UI - API Documentation
In this Application the swagger UI is also configured for the API documentation purpose.

Swagger UI URL : http://localhost:8080/swagger-ui/index.html



## Test Cases



Unit and Integration cases are written with 87% Test Coverage.

For running test case
```
mvn clean verify
```

 
