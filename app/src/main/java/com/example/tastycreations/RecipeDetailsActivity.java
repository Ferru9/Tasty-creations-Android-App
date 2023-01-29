package com.example.tastycreations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastycreations.Adapters.IngredientsAdapter;
import com.example.tastycreations.Adapters.InstructionsAdapter;
import com.example.tastycreations.Adapters.SimilarRecipeAdapter;
import com.example.tastycreations.Listeners.InstructionsListener;
import com.example.tastycreations.Listeners.RecipeClickListener;
import com.example.tastycreations.Listeners.RecipeInformationListener;
import com.example.tastycreations.Listeners.SimilarRecipesListener;
import com.example.tastycreations.Models.InstructionsResponse;
import com.example.tastycreations.Models.RecipeDetailsResponse;
import com.example.tastycreations.Models.SimilarRecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_meal_name, textView_meal_source;
    ImageView imageView_meal_image;
    TextView textView_meal_summary;
    RecyclerView recycler_meal_ingredients, recycler_meal_similar, recycler_meal_instructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_similar = findViewById(R.id.recycler_meal_similar);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);



        //Using the RequestManager class to manage every call it is made from the api
        //getting the id from the onRecipeClick listener
        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        //calling the api to get the recipe details
        manager.getRecipeDetails(recipeInformationListener, id);
        //calling the api to get the similar recipes
        manager.getSimilarRecipes(similarRecipesListener, id);
        //calling the api to get the instructions recipes
        manager.getInstructions(instructionsListener, id);
        //"Loading Details ..." when the app is loading the information of the api
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();

    }



    private final RecipeInformationListener recipeInformationListener = new RecipeInformationListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            //many details available in the api
            //getting the title of the recipe in the api
            textView_meal_name.setText(response.title);
            //getting the source of the recipe give credit
            textView_meal_source.setText(response.sourceName);
            //getting the summary of the recipe
            //parsing an html format
            String tmpHtml = response.summary;
            String htmlTextStr = Html.fromHtml(tmpHtml).toString();
            textView_meal_summary.setText(htmlTextStr);

            //getting the image of the recipe
            Picasso.get().load(response.image).into(imageView_meal_image);

            //showing the ingredients in a recycler view
            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            //toasting message if there was an error
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {

            //setting the recycler for the similar recipes
            recycler_meal_similar.setHasFixedSize(true);
            recycler_meal_similar.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, response, recipeClickListener );
            recycler_meal_similar.setAdapter(similarRecipeAdapter);

        }

        @Override
        public void didError(String message) {
            //toasting message if there was an error
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            //starting the new activity
            startActivity(new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
            //sending the id of each recipe to the second activity
            .putExtra("id", id));
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            // setting the recycler view for the instructions
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {
            //toasting message if there was an error
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

}