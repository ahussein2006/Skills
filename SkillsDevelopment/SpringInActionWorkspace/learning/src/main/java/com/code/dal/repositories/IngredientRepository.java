package com.code.dal.repositories;

import org.springframework.data.repository.CrudRepository;

import com.code.dal.entities.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
