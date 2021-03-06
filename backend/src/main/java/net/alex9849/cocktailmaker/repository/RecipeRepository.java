package net.alex9849.cocktailmaker.repository;

import net.alex9849.cocktailmaker.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {


    List<Recipe> findAllByOwnerId(long id);

    List<Recipe> findAllByInPublicIsTrue();

    List<Recipe> findAllByOwnerIsNull();
}
