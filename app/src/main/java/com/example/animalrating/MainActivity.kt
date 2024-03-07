package com.example.animalrating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "onCreate was called")

        // Setup Animal ListView
        val animalList = listOf("Dog","Cat","Bear","Rabbit")
        val animalAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, animalList)
        val animalListView = findViewById<ListView>(R.id.animal_lv)
        animalListView.adapter = animalAdapter
        // Setup myIntent
        val myIntent = Intent(this,AnimalRating::class.java)

        // AnimalListView onClickListener
        animalListView.setOnItemClickListener { list, item, position, id ->
            val animalName = when(position) {
                0 -> "Dog"
                1 -> "Cat"
                2 -> "Bear"
                else -> "Rabbit"
            }
            myIntent.putExtra("animalName", animalName)
            startActivity(myIntent)
        }

    }

    override fun onResume() {
        super.onResume()
        // Load data using sharedPreference
        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val ratedAnimalName = sharedPref.getString("mostRecent","")
        val ratedAnimalRating = sharedPref.getString("rating","")

        val ratedAnimalNameTV = findViewById<TextView>(R.id.selected_animal_name_tv)
        val animalImage = findViewById<ImageView>(R.id.selected_animal_image)
        val animalRatingBar = findViewById<RatingBar>(R.id.selected_animal_rating_bar)
        Log.i(TAG, "We got into the onResume()")
        Log.i(TAG, "ratedAnimalName is " + ratedAnimalName)
        Log.i(TAG, "ratedAnimalRating is " + ratedAnimalRating)

        if (ratedAnimalName != null) {
            Log.i(TAG, "ratedAnimalName is not null")
            if (ratedAnimalName.isEmpty()) {
                Log.i(TAG, "ratedAnimalName is empty")
                animalImage.isVisible = false
                ratedAnimalNameTV.isVisible = false
                animalRatingBar.isVisible = false
                findViewById<TextView>(R.id.rated_animal_title_tv).isVisible = false
                findViewById<TextView>(R.id.selected_animal_rating_tv).isVisible = false

            } else {
                // Prep image resource
                val selectedAnimalImage = when (ratedAnimalName) {
                    "Dog" -> R.drawable.dog
                    "Cat" -> R.drawable.cat
                    "Bear" -> R.drawable.bear
                    else -> R.drawable.rabbit
                }

                // Set rated animal image
                animalImage.setImageResource(selectedAnimalImage)

                // Set rated animal name
                ratedAnimalNameTV.text = ratedAnimalName

                // Set rated animal rating
                if (ratedAnimalRating != null) {
                    animalRatingBar.rating = ratedAnimalRating.toFloat()
                }

                animalImage.isVisible = true
                ratedAnimalNameTV.isVisible = true
                animalRatingBar.isVisible = true
                findViewById<TextView>(R.id.rated_animal_title_tv).isVisible = true
                findViewById<TextView>(R.id.selected_animal_rating_tv).isVisible = true
            }
        }

    }

}