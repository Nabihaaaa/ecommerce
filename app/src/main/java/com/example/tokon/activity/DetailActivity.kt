package com.example.tokon.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokon.databinding.ActivityDetailBinding
import com.example.tokon.databinding.ActivityHomeBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}