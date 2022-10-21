package com.example.tokon.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokon.databinding.ActivityHomeBinding
import com.example.tokon.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}