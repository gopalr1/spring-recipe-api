DROP sequence IF EXISTS recipe_seq;
ALTER TABLE ingredient DROP constraint recipe_id_fk;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS ingredient;


create sequence recipe_seq START WITH 1;

CREATE TABLE IF NOT EXISTS recipe
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(50)   NOT NULL,
    description  VARCHAR(500)  NOT NULL,
    image_path   VARCHAR(100)  NOT NULL,
    diet_type    VARCHAR(50),
    prep_time    INT           NOT NULL,
    cook_time    INT           NOT NULL,
    servings     INT           NOT NULL,
    instructions VARCHAR(1000) NOT NULL
);

create TABLE IF NOT EXISTS ingredient
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(50) NOT NULL,
    amount              numeric     NOT NULL,
    unit_of_measurement varchar(50) NOT NULL,
    recipe_id           BIGINT      NOT NULL,
    CONSTRAINT recipe_id_fk FOREIGN KEY (recipe_id)
        references recipe (id)

);

INSERT INTO recipe (name, description, image_path, diet_type, prep_time, cook_time, servings, instructions)
VALUES ('Omlette', 'The dish made by frying raw egg on pan', 'test/omlette.png', 'NON_VEGETARIAN', '5', '3', '1',
        'Can be cooked in Oven');

INSERT INTO ingredient (name, amount, unit_of_measurement, recipe_id)
VALUES ('Egg', 1, 'NUMBERS', 1),
       ('Salt', 1, 'TABLE_SPOON', 1),
       ('Pepper', 1, 'TABLE_SPOON', 1)