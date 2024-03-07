package com.example.animalrating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class AnimalRating : AppCompatActivity() {
    private val TAG = "AnimalRatingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_rating)

        // Get the animal name
        val animalName = intent.getStringExtra("animalName").toString()
        val selectedAnimalImage = when(animalName) {
            "Dog" -> R.drawable.dog
            "Cat" -> R.drawable.cat
            "Bear" -> R.drawable.bear
            else -> R.drawable.rabbit
        }
        findViewById<ImageView>(R.id.selected_animal_image).setImageResource(selectedAnimalImage)
        findViewById<TextView>(R.id.selected_animal_name_tv).text = animalName

    }

    fun saveRating(view: View){ // Send data back to MainActivity
        Log.i(TAG, "saveRating was called")
        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val animalName = findViewById<TextView>(R.id.selected_animal_name_tv).text.toString()
        val myRatingBar = findViewById<RatingBar>(R.id.selected_animal_rating_bar)
        val rating = myRatingBar.rating.toString()
        editor.putString("mostRecent", animalName)
        editor.putString("rating", rating)
        editor.apply()
        Log.i(TAG, "animal is " + animalName)
        Log.i(TAG, "rating is " + rating)
    }
}