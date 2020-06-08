package com.sijanrijal.bakingapp;

import java.util.List;

public class Recipe {

    public int id;
    public String name;
    public List<Ingredients> ingredients;
    public List<Steps> steps;

    public static class Ingredients {
        public double quantity;
        public String measure;
        public String ingredient;
    }

    public static class Steps {
        public String shortDescription;
        public String description;
        public String videoURL;
        public String thumbnailURL;
    }
}
