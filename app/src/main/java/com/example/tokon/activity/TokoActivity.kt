package com.example.tokon.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokon.databinding.ActivityTokoBinding


class TokoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTokoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}