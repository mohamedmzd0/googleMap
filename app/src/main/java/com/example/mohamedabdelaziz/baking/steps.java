package com.example.mohamedabdelaziz.baking;

/**
 * Created by Mohamed Abd ELaziz on 6/17/2017.
 */

public class steps {

    private int id,recipe_ID ;
    private String shortDescription,description,videoURL,thumbnailURL ;

    public steps(int id, String shortDescription, String description, String videoURL,int recipe_ID,String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.recipe_ID=recipe_ID ;
        this.thumbnailURL=thumbnailURL ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
