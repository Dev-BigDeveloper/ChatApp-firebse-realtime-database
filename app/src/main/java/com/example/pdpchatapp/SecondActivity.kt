package com.example.pdpchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pdpchatapp.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.signInText.setOnClickListener {
//            binding.signInCard1.visibility = View.INVISIBLE
//            binding.signInCard2.visibility = View.VISIBLE
//            binding.backImage.visibility = View.VISIBLE
//        }
//
//        binding.backImage.setOnClickListener {
//            binding.signInCard1.visibility = View.VISIBLE
//            binding.signInCard2.visibility = View.INVISIBLE
//            binding.backImage.visibility = View.INVISIBLE
//        }
    }
}