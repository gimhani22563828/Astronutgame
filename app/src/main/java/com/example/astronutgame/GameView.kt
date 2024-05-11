package com.example.astronutgame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View


class GameView(var c: Context, var gameTask: MainActivity) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var highestScore = 0
    private var myastroPosition = 0
    private val spaceships = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0
    private lateinit var sharedPreferences: SharedPreferences

    init {
        myPaint = Paint()
        sharedPreferences = c.getSharedPreferences("GamePreferences", Context.MODE_PRIVATE)
        highestScore = sharedPreferences.getInt("HighestScore", 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            spaceships.add(map)
        }
        time += 10 + speed

        val spaceshipWidth = viewWidth / 4
        val spaceHeight = spaceshipWidth + 15


        // Draw player's astronut
        val jetDrawable = resources.getDrawable(R.drawable.astro,null)
        jetDrawable.setBounds(
            myastroPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - spaceHeight,
            myastroPosition * viewWidth / 3 + viewWidth / 15 + spaceshipWidth - 25,
            viewHeight - 2
        )
        jetDrawable.draw(canvas)

        // Draw spaceships

        val iterator = spaceships.iterator()
        while (iterator.hasNext()) {
            val spaceship = iterator.next()
            val lane = spaceship["lane"] as Int
            val spaceshipX = lane * viewWidth / 3 + viewWidth / 15
            var spaceshipY = time - spaceship["startTime"] as Int


            val flyingSaucerDrawable = resources.getDrawable(R.drawable.spaceship, null)
            flyingSaucerDrawable.setBounds(
                spaceshipX + 25, spaceshipY - spaceHeight, spaceshipX + spaceshipWidth - 25, spaceshipY
            )
            flyingSaucerDrawable.draw(canvas)

            if (lane == myastroPosition && spaceshipY > viewHeight - 2 - spaceHeight && spaceshipY < viewHeight - 2) {
                gameTask.closeGame(score)
            }

            if (spaceshipY > viewHeight + spaceHeight) {
                iterator.remove()
                score++
                speed = 1 + score / 8
            }
        }

        // Update highest score if the current score exceeds it
        if (score > highestScore) {
            highestScore = score
            val editor = sharedPreferences.edit()
            editor.putInt("HighestScore", highestScore)
            editor.apply()
        }

        // Draw score, speed, and highest score
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 45f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        canvas.drawText("Highest Score : $highestScore", 680f, 80f, myPaint!!)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myastroPosition > 0) {
                        myastroPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myastroPosition < 2) {
                        myastroPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}