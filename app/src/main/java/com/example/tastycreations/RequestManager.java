package com.example.tastycreations;

import android.content.Context;

import com.example.tastycreations.Listeners.InstructionsListener;
import com.example.tastycreations.Listeners.RandomRecipeResponseListener;
import com.example.tastycreations.Listeners.RecipeInformationListener;
import com.example.tastycreations.Listeners.SimilarRecipesListener;
import com.example.tastycreations.Models.InstructionsResponse;
import com.example.tastycreations.Models.NutritionResponse;
import com.example.tastycreations.Models.RandomRecipeApiResponse;
import com.example.tastycreations.Models.RecipeDetailsResponse;
import com.example.tastycreations.Models.SimilarRecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Class to manage the API call
public class RequestManager {
    Context context;

    //Connecting to the api
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/") //Connecting with the main spoonacular URL
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //Creating the request manager constructor
    public RequestManager(Context context) {
        this.context = context;
    }


    //Get random method to get the data from the api
    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){
        //calling the random recipe from the interface
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        //generating the random recipe with the random recipe interface, getting the API key from the strings XML
        // 10 is the number of recipes we want to generate, this number can go from 1 - 100
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key), "10", tags);

        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeInformationListener listener, int id){
        //getting the recipes details with the id from the spoonocular api with retrofit
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getSimilarRecipes(SimilarRecipesListener listener, int id){
        //getting similar  with the id with the spoonocular api
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        //calling the similar recipe from the api, setting only 4 simmilar recipes this can also be 100
        Call<List<SimilarRecipeResponse>> call = callSimilarRecipes.callSimilarRecipe(id, "4", context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getInstructions(InstructionsListener listener, int id){
        //getting the recipes instructions with the id from the spoonocular api with retrofit
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        //calling the api to get the instructions
        Call<List<InstructionsResponse>> call = callInstructions.callInstructions(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<InstructionsResponse>> call, Response<List<InstructionsResponse>> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<InstructionsResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }


    //Creating an interface for generating the random recipe api call
    private interface CallRandomRecipes{
        @GET("recipes/random") //getting the random recipe from the spoonacular api

        //Calling the api response, converted from a jason format to java
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                //tags implemented for filtering recipe with spinner
                @Query("tags")List<String> tags
                );
    }

    //getting recipe information from the api
    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        //defining the id
        Call<RecipeDetailsResponse> callRecipeDetails(
                //path to the details of the id
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }


    //getting similar recipes
    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipe(
                @Path("id") int id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }

    //getting the instruction of each recipe

    private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsResponse>> callInstructions(
                //path to the details of the id
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }

    //getting similar recipes
    private interface CallNutritionChart{
        @GET("recipes/{id}/nutritionWidget.json")
        Call<NutritionResponse> callSimilarRecipe(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}


