package com.example.animalrating

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var animalList = mutableListOf("Dog","Cat","Bear","Rabbit")
    private lateinit var animalAdapter : ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "onCreate was called")

        // Setup Animal ListView
        animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, animalList)
        val animalListView = findViewById<ListView>(R.id.animal_lv)
        animalListView.adapter = animalAdapter
        // Setup myIntent
        val myIntent = Intent(this,AnimalRating::class.java)

        // AnimalListView onClickListener
        animalListView.setOnItemClickListener { list, item, position, id ->
            val selectedAnimalName = when(position) {
                0 -> "Dog"
                1 -> "Cat"
                2 -> "Bear"
                else -> "Rabbit"
            }

            myIntent.putExtra("selectedAnimalName", selectedAnimalName)
            startActivity(myIntent)
        }



    }

    override fun onResume() {
        super.onResume()
        // Load data using sharedPreference
        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val mostRecentlyRated = sharedPref.getString("mostRecent","")

        if (mostRecentlyRated != null) {
            // If mostRecentlyRated is empty and null then keep everything invisible
            if (mostRecentlyRated.isEmpty()) {
                visibleSwitch(false)    // Keep visible OFF

            } else {
                // Display the AnimalRatingActivity in the MainActivity
                val animalImage = when (mostRecentlyRated) {
                    "Dog" -> R.drawable.dog
                    "Cat" -> R.drawable.cat
                    "Bear" -> R.drawable.bear
                    else -> R.drawable.rabbit
                }
                findViewById<ImageView>(R.id.animal_image).setImageResource(animalImage)                // Set recent animal image
                findViewById<TextView>(R.id.animal_name_tv).text = mostRecentlyRated                    // Set recent animal name TextView

                val mostRecentRating = sharedPref.getString(mostRecentlyRated,"-")
                if (mostRecentRating != null) {
                    findViewById<RatingBar>(R.id.animal_rating_bar).rating = mostRecentRating.toFloat() // Set rated animal rating
                }
                visibleSwitch(true)     // Set visible ON

                // Update the ListView
                /*
                val allRatingsArray = ArrayList<String>()
                val allRatings = sharedPref.all
                allRatings.forEach { entry ->

                    if (entry.key != "mostRecent") {
                        if (entry.value != "")
                            allRatingsArray.add("${entry.key} -- Rating: ${entry.value}/5")
                        else
                            allRatingsArray.add(entry.key)
                    }
                }
                val newList = ArrayList<String>()
                animalList.sorted().zip(allRatingsArray.toList().sorted()) { original, new ->
                    Log.i(TAG,"${original} + ${new}")
                    if (original != new)
                        newList.add(new)
                    else
                        newList.add(original)
                }

                animalAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, newList)
                findViewById<ListView>(R.id.animal_lv).adapter = animalAdapter
                */

                for ( (index, animal) in animalList.withIndex()) {
                    val animalName = animal.split(" ")[0]
                    val rating = sharedPref.getString(animalName, "-1.0")
                    if (rating != null) {
                        if (rating.toDouble() != -1.0) {
                            animalList[index] = "$animalName -- Rating: ${rating}/5"
                        }
                    }
                }
                animalAdapter.notifyDataSetChanged()

            }
        }

    }

    fun clearRatingButton(view: View) {
        visibleSwitch(false)
        val sharedPref = getSharedPreferences("animal-rating.txt", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
        for ( (index, animal) in animalList.withIndex()) {
            val animalName = animal.split(" ")[0]
            animalList[index] = animalName
        }
        animalAdapter.notifyDataSetChanged()

    }


    private fun visibleSwitch(status : Boolean) {
        findViewById<TextView>(R.id.recently_rated_animal_title_tv).isVisible = status
        findViewById<ImageView>(R.id.animal_image).isVisible = status
        findViewById<TextView>(R.id.animal_name_tv).isVisible = status
        findViewById<TextView>(R.id.animal_rating_tv).isVisible = status
        findViewById<RatingBar>(R.id.animal_rating_bar).isVisible = status
    }

}