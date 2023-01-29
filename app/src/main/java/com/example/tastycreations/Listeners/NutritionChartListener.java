package com.example.tastycreations.Listeners;

import com.example.tastycreations.Models.NutritionResponse;

public interface NutritionChartListener {
    void didFetch(NutritionResponse response, String message);
    void didError(String message);
}
