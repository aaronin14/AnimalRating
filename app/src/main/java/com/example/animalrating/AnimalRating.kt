package com.example.animalrating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class AnimalRating : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_rating)

        // Get the animal name
        val animalName = intent.getStringExtra("selectedAnimalName").toString()
        val selectedAnimalImage = when(animalName) {
            "Dog" -> R.drawable.dog
            "Cat" -> R.drawable.cat
            "Bear" -> R.drawable.bear
            else -> R.drawable.rabbit
        }
        findViewById<ImageView>(R.id.animal_image).setImageResource(selectedAnimalImage)
        findViewById<TextView>(R.id.animal_name_tv).text = animalName


        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val ratingBar = findViewById<RatingBar>(R.id.animal_rating_bar)
        val rating = sharedPref.getString(animalName,"-")

        if (rating != null) {
            if (rating != "-")
                ratingBar.rating = rating.toFloat()
            else
                ratingBar.rating = 0.0F
        }
    }

    fun saveRating(view: View){ // Send data back to MainActivity
        // Prep the editor
        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val editor = sharedPref.edit()

        val animalName = findViewById<TextView>(R.id.animal_name_tv).text.toString()
        val myRatingBar = findViewById<RatingBar>(R.id.animal_rating_bar)
        val rating = myRatingBar.rating.toString()
        editor.putString("mostRecent", animalName)
        editor.putString(animalName, rating)
        editor.apply()
        finish()
    }
}