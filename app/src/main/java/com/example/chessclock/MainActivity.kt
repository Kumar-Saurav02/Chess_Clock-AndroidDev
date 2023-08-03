package com.example.chessclock

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer

import android.widget.Button

import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        variables
        val buttonup= findViewById<Button>(R.id.button_up)
        val buttondown: Button =findViewById(R.id.button_down)
        val restart= findViewById<ImageButton>(R.id.restart)
        val pause= findViewById<ImageButton>(R.id.pause)
        val set = findViewById<ImageButton>(R.id.set)


        var settimer:Long = 5
        var isreadypause:Boolean = false
        var clockup:Boolean=false
        var clockdown:Boolean=false

//        timer functions are defined here
        class Timer
            {
                var countDownTimer: CountDownTimer? =null;
                var timeleftinMillisec : Long = settimer*60000;
                var istimerunning: Boolean=false


                fun updateTimer(button: Button)
                    {
                        var minute : Long =timeleftinMillisec/60000;
                        var seconds : Long = timeleftinMillisec%60000/1000;

                        var timeleftsting: String
                        timeleftsting=""+minute+":"
                        if (seconds<10)
                            timeleftsting+="0"
                        timeleftsting+=seconds

                        button.text=timeleftsting
                    }

                fun startTimer(button: Button)
                    {
                        countDownTimer = object : CountDownTimer(timeleftinMillisec,1000)
                        {
                            override fun onTick(millisUntilFinished: Long) {
                                timeleftinMillisec = millisUntilFinished;
                                updateTimer(button)
                            }

                            override fun onFinish() {
                                button.text="You Lost"
                            }
                        }.start()
                        istimerunning=true
                    }

                fun stopTimer()
                    {
                        countDownTimer?.cancel()
                        istimerunning=false
                    }

        }


//       obejcts of the class timer
        val forup= Timer()
        val fordown=Timer()


        fun reset()
            {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Reset Timer")
                builder.setMessage("You sure want to reset timer?")

                builder.setPositiveButton("Yes")
                {
    //                    reset functions
                        dialog, id ->

                    forup.timeleftinMillisec= settimer*60000
                    forup.stopTimer()

                    fordown.timeleftinMillisec= settimer*60000
                    fordown.stopTimer()


                    buttonup.isEnabled = true
                    buttondown.isEnabled = true

                    pause.setImageDrawable(getDrawable(R.drawable.play))

                    forup.updateTimer(buttonup)
                    fordown.updateTimer(buttondown)

                    buttondown.setBackgroundColor(Color.parseColor("#c6c6c6"))
                    buttonup.setBackgroundColor(Color.parseColor("#c6c6c6"))

                    Toast.makeText(this, "Timer has been Reset",Toast.LENGTH_LONG).show()


                    isreadypause=false
                    clockup=false
                    clockdown=false
            }

            builder.setNegativeButton("No")
            {
                    dialog, id -> dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

        }


//        events

            buttonup.setOnClickListener {

                clockdown=true
                clockup=false

                fordown.startTimer(buttondown)
                forup.stopTimer()

                buttonup.isEnabled = false
                buttondown.isEnabled = true

                pause.setImageDrawable(getDrawable(R.drawable.pause))

                isreadypause=true

                buttonup.setBackgroundColor(Color.parseColor("#ccc2d8"))
                buttondown.setBackgroundColor(Color.parseColor("#317dff"))

            }

            buttondown.setOnClickListener{

                clockup=true
                clockdown=false

                forup.startTimer(buttonup)
                fordown.stopTimer()

                buttonup.isEnabled = true
                buttondown.isEnabled = false

                pause.setImageDrawable(getDrawable(R.drawable.pause))

                isreadypause=true

                buttonup.setBackgroundColor(Color.parseColor("#317dff"))
                buttondown.setBackgroundColor(Color.parseColor("#ccc2d8"))


            }

            restart.setOnClickListener{

                reset()
            }

            pause.setOnClickListener{

                if(clockup or clockdown)
                {
                    pause.setImageDrawable(getDrawable(R.drawable.play))
                    if(clockdown)
                    {
                        fordown.stopTimer()
                        clockdown=false
                        clockup=false

                    }
                    else if(clockup)
                    {
                        forup.stopTimer()
                        clockup=false
                        clockdown=false
                    }

                    isreadypause=true
                    Toast.makeText(this, "Timer Paused",Toast.LENGTH_SHORT).show()
                }

                else if(!clockup and !clockdown and isreadypause)
                {
                    pause.setImageDrawable(getDrawable(R.drawable.pause))
                    if(buttondown.isEnabled)
                    {
                        fordown.startTimer(buttondown)
                        clockdown=true
                    }
                    else if(buttonup.isEnabled)
                    {
                        forup.startTimer(buttonup)
                        clockup=true
                    }

                    Toast.makeText(this, "Timer Resumed",Toast.LENGTH_SHORT).show()
                }

                else if(!clockup and !clockdown and !isreadypause)
                {
                    Toast.makeText(this, "Start Game first",Toast.LENGTH_SHORT).show()
                }

            }

            set.setOnClickListener{

                val numberPicker = NumberPicker(this)
                numberPicker.minValue = 1
                numberPicker.maxValue = 15

                val builder = AlertDialog.Builder(this)
                builder.setView(numberPicker)
                builder.setTitle("Select Time")
                builder.setPositiveButton("OK") { _, _ ->
                    settimer=numberPicker.value.toLong()
                    reset()
                    forup.updateTimer(buttonup)
                    fordown.updateTimer(buttondown)
                }
                builder.setNegativeButton("Cancel", null)
                builder.show()
        }

    }

}




