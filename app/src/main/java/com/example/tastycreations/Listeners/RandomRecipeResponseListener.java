package com.example.tastycreations.Listeners;

import com.example.tastycreations.Models.RandomRecipeApiResponse;

//Listener to get the response to the main activity
public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
