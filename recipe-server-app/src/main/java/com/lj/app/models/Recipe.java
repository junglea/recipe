package com.lj.app.models;

import java.util.List;

import javax.validation.constraints.Size;

import org.bson.types.Binary;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="recipes")
public class Recipe {

    @Id
    private String id;

    @NotBlank
    @Size(max=100)
    @Indexed(unique=true)
    private String title;


    private String nbPers;
    private String making;
    private String baking;
    private String cooling;

    private String makeDiff;
    private String toTest;

    private String foundOn;

    private String categ;
    
    private List<String> ingredients;
    private List<String> steps;

    @Field
    private Binary photo;

    public Recipe() {
        
    }

    public Recipe(String title) {
        this.title = title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setNbPers(String nbPers){
                this.nbPers = nbPers;
    }

    public void setMaking(String making){
        this.making = making;
    }

    /**
     * @param baking the baking to set
     */
    public void setBaking(String baking) {
        this.baking = baking;
    }
    /**
     * @param cooling the cooling to set
     */
    public void setCooling(String cooling) {
        this.cooling = cooling;
    }
    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    /**
     * @param steps the steps to set
     */
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    /**
     * @param makeDiff the makeDiff to set
     */
    public void setMakeDiff(String makeDiff) {
        this.makeDiff = makeDiff;
    }
    /**
     * @param toTest the toTest to set
     */
    public void setToTest(String toTest) {
        this.toTest = toTest;
    }
    /**
     * @param foundOn the foundOn to set
     */
    public void setFoundOn(String foundOn) {
        this.foundOn = foundOn;
    }
    /**
     * @param categ the categ to set
     */
    public void setCateg(String categ) {
        this.categ = categ;
    }
    /**
     * @param photo the photo to set
     */
    public void setPhoto(Binary photo) {
        this.photo = photo;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @return the baking
     */
    public String getBaking() {
        return baking;
    }
    /**
     * @return the cooling
     */
    public String getCooling() {
        return cooling;
    }
    /**
     * @return the ingredients
     */
    public List<String> getIngredients() {
        return ingredients;
    }
    /**
     * @return the making
     */
    public String getMaking() {
        return making;
    }
    /**
     * @return the nbPers
     */
    public String getNbPers() {
        return nbPers;
    }
    /**
     * @return the steps
     */
    public List<String> getSteps() {
        return steps;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @return the makeDiff
     */
    public String getMakeDiff() {
        return makeDiff;
    }
    /**
     * @return the toTest
     */
    public String getToTest() {
        return toTest;
    }
    /**
     * @return the foundOn
     */
    public String getFoundOn() {
        return foundOn;
    }
    /**
     * @return the categ
     */
    public String getCateg() {
        return categ;
    }
    /**
     * @return the photo
     */
    public Binary getPhoto() {
        return photo;
    }
    @Override
    public String toString() {
        return String.format(
                "Recipe[id=%s, title='%s']",
                id, title);
    }


}