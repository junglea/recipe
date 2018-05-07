package com.lj.app.controllers;

import javax.validation.Valid;

import com.lj.app.models.Recipe;
import com.lj.app.repositories.RecipeRepository;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepo;

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes(@RequestParam(value = "title", required = false) String title) {
        Sort sortByTitle = new Sort(Sort.Direction.DESC, "title");
        if (title != null) {
            System.out.println("find a recipe with title : " + title);
            return recipeRepo.findByTitle(title);
        }
        return recipeRepo.findAll(sortByTitle);
    }

    @PostMapping("/recipes")
    public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
        // todo.setCompleted(false);
        System.out.println("Create a recipe with title : " + recipe.getTitle());
        System.out.println("Create a recipe with ingredients : " + recipe.getIngredients());
        return recipeRepo.save(recipe);
    }

    @GetMapping(value = "/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable("id") String id) {
        Recipe recipe = recipeRepo.findOne(id);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable("id") String id, @Valid @RequestBody Recipe recipe) {
        Recipe recipeData = recipeRepo.findOne(id);
        if (recipeData == null) {
            System.out.println("Failed to update recipe with id " + id + " not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        recipeData.setTitle(recipe.getTitle());
        recipeData.setNbPers(recipe.getNbPers());
        recipeData.setMaking(recipe.getMaking());
        recipeData.setBaking(recipe.getBaking());
        recipeData.setCooling(recipe.getCooling());
        recipeData.setMakeDiff(recipe.getMakeDiff());
        recipeData.setToTest(recipe.getToTest());
        recipeData.setFoundOn(recipe.getFoundOn());
        recipeData.setIngredients(recipe.getIngredients());
        recipeData.setSteps(recipe.getSteps());
        recipeData.setCateg(recipe.getCateg());
        System.out.println("Update a recipe with title : " + recipe.getTitle());
        System.out.println("Update a recipe with ingredients : " + recipe.getIngredients());
        // recipeData.setCompleted(recipe.getCompleted());
        Recipe updatedRecipe = recipeRepo.save(recipeData);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    @DeleteMapping(value = "/recipes/{id}")
    public void deleteRecipe(@PathVariable("id") String id) {
        recipeRepo.delete(id);
    }

    @PostMapping("/recipes/{id}/upload")
    public ResponseEntity<Recipe> uploadFile(@PathVariable("id") String id,
            @RequestParam("file") MultipartFile multipart) {
        System.out.println("Update a recipe with id : " + id);
        Recipe recipeData = recipeRepo.findOne(id);
        if (recipeData == null) {
            System.out.println("Failed to update recipe with id " + id + " not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            recipeData.setPhoto(new Binary(BsonBinarySubType.BINARY, multipart.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload file due to: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Recipe updatedRecipe = recipeRepo.save(recipeData);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    // @GetMapping("/recipes/{id}/photo")
    // @ResponseBody
    // public ResponseEntity<ByteArrayResource> servePhoto(@PathVariable("id") String id) {
    //     System.out.println("Download a photo of recipe with id : " + id);
    //     Recipe recipeData = recipeRepo.findOne(id);
    //     if (recipeData == null || recipeData.getPhoto() == null) {
    //         System.out.println("Failed to download photo for recipe with id " + id + " not found.");
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     FileOutputStream fileOutputStream = null;
    //     try {
    //         fileOutputStream = new FileOutputStream("photo_recipe.jpg");
    //         fileOutputStream.write(recipeData.getPhoto().getData());
    //     } catch (Exception e) {
    //         System.out.println("Failed to download file due to: " + e.getMessage());
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     } finally {
    //         if (fileOutputStream != null) {
    //             try {
    //                 fileOutputStream.close();
    //             } catch (IOException e) {
    //                 System.out.println("Failed to download file due to: " + e.getMessage());
    //                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //             }
    //         }
    //     }
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("content-disposition", "attachment; filename=" + "photo_recipe.jpg");
    //     return new ResponseEntity<ByteArrayResource>(new ByteArrayResource(recipeData.getPhoto().getData()), headers, HttpStatus.OK);

    // }

    @GetMapping("/recipes/{id}/photo")
    @ResponseBody
    public ResponseEntity<String> servePhoto(@PathVariable("id") String id) {
        System.out.println("Download a photo of recipe with id : " + id);
        Recipe recipeData = recipeRepo.findOne(id);
        if (recipeData == null || recipeData.getPhoto() == null) {
            System.out.println("Failed to download photo for recipe with id " + id + " not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // FileOutputStream fileOutputStream = null;
        // try {
        //     fileOutputStream = new FileOutputStream("photo_recipe.jpg");
        //     fileOutputStream.write(recipeData.getPhoto().getData());
        // } catch (Exception e) {
        //     System.out.println("Failed to download file due to: " + e.getMessage());
        //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        // } finally {
        //     if (fileOutputStream != null) {
        //         try {
        //             fileOutputStream.close();
        //         } catch (IOException e) {
        //             System.out.println("Failed to download file due to: " + e.getMessage());
        //             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //         }
        //     }
        // }
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment; filename=" + "photo_recipe.jpg");
        return new ResponseEntity<String>(Base64Utils.encodeToString(recipeData.getPhoto().getData()), headers, HttpStatus.OK);

    }

    // @GetMapping("/recipes/{id}/notation")
    // public void noteRecipe(@PathVariable("id") String id,
    //         @RequestParam(value = "memberId", required = true) String memberId,
    //         @RequestParam(value = "note", required = true) Number note) {
    //     Recipe recipeData = recipeRepo.findOne(id);
    //     if (recipeData != null) {
    //         recipeData.addNotation(memberId, note);
    //         recipeRepo.save(recipeData);
    //     }
    // }
}