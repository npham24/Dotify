package com.example.dotify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var playCount = 0
    private var changingUserName = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.playCount = Random.nextInt(0, 1000)
        displayPlayCount()

        // I tried it without passing the View and it seems that it does it
        // natively with the onClickListener
        btnPrev.setOnClickListener {
            makeToast("Skipping to previous track")
        }

        btnNext.setOnClickListener {
            makeToast("Skipping to next track")
        }

        btnPlay.setOnClickListener {
            this.playCount++;
            displayPlayCount()
        }

        btnChangeUser.setOnClickListener {
            changeUser()
        }
    }


    private fun changeUser() {
        // clicking on changing user
        if (!this.changingUserName) {
            tvUserName.visibility = View.INVISIBLE
            editUserName.visibility = View.VISIBLE
            btnChangeUser.text = getString(R.string.apply)
            this.changingUserName = true
        }
        // clicking on apply
        else {
            if (editUserName.text.toString() != "") {
                tvUserName.text = editUserName.text
                editUserName.visibility = View.INVISIBLE
                tvUserName.visibility = View.VISIBLE
                btnChangeUser.text = getString(R.string.change_user)
                this.changingUserName = false
            }
        }
    }


    private fun makeToast(toast: String) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }


    private fun displayPlayCount() {
        var play = " play";
        if (this.playCount > 1) {
            play = " plays";
        }
        // getString method does not work even though the other one give me a warning
        //tvPlayCounter.text = getString(R.string.play_count, this.playCount, play)
        tvPlayCounter.text = "" + this.playCount + play
    }

}
