package com.example.tastycreations.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastycreations.Listeners.RecipeClickListener;
import com.example.tastycreations.Models.SimilarRecipeResponse;
import com.example.tastycreations.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter  extends RecyclerView.Adapter<SimilarRecipeViewHolder>{
    Context context;
    List<SimilarRecipeResponse> list;
    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipeResponse> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {

        //getting the image of each similar recipe
        Picasso.get().load("https://spoonacular.com/recipeImages/" + list.get(position).id + "-556x370." + list.get(position).imageType).into(holder.imageView_similar);

        //getting the title of the similar recipes
        holder.textView_similar_title.setText(list.get(position).title);
        holder.textView_similar_title.setSelected(true);
        //getting how many serving it contains
        holder.textView_similar_serving.setText(list.get(position).servings + " People");

        holder.similar_recipe_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SimilarRecipeViewHolder extends RecyclerView.ViewHolder {
    CardView similar_recipe_holder;
    TextView textView_similar_title,textView_similar_serving;
    ImageView imageView_similar;

    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        similar_recipe_holder = itemView.findViewById(R.id.similar_recipe_holder);
        textView_similar_title = itemView.findViewById(R.id.textView_similar_title);
        textView_similar_serving = itemView.findViewById(R.id.textView_similar_serving);
        imageView_similar = itemView.findViewById(R.id.imageView_similar);
    }
}
