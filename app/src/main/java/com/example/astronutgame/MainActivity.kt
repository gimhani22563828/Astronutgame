package com.example.astronutgame

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var scoreTextView: TextView
    private lateinit var highestScoreTextView: TextView
    private var highestScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        scoreTextView = findViewById(R.id.score)
        highestScoreTextView = findViewById(R.id.highestScoreTextView)
        mGameView = GameView(this, this)

        // Retrieve highest score from SharedPreferences
        val sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)
        highestScore = sharedPreferences.getInt("HighestScore", 0)
        updateHighestScore()

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.background)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            scoreTextView.visibility = View.GONE
            highestScoreTextView.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    fun closeGame(mScore: Int) {
        scoreTextView.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
        highestScoreTextView.visibility = View.VISIBLE

        // Update highest score if needed
        if (mScore > highestScore) {
            highestScore = mScore
            updateHighestScore()
            // Save the highest score in SharedPreferences
            val sharedPreferences = getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("HighestScore", highestScore).apply()
        }

        // Reset variables for a new game session
        mGameView = GameView(this, this)
    }

    private fun updateHighestScore() {
        highestScoreTextView.text = "Highest Score: $highestScore"
    }
}