package com.example.tastycreations.Listeners;

import com.example.tastycreations.Models.RecipeDetailsResponse;

public interface RecipeInformationListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);
}
