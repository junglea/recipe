package com.lj.app.repositories;

import java.util.List;

import com.lj.app.models.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String>{
    
    @Query(value = "{ 'title': {$regex : ?0, $options: 'i'} }")
    List<Recipe> findByTitle(String title);

    /*
    mongoTemplate.find(Query.query(new Criteria()
                        .orOperator(Criteria.where("description").regex(text, "i"),
                                    Criteria.where("make").regex(text, "i"),
                                    Criteria.where("model").regex(text, "i"))
                        ), Car.class);
                        */
}