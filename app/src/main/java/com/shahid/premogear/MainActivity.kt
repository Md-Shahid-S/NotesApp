package com.shahid.premogear

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shahid.premogear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Only set the title for minimal test
        binding.tvTitle.text = "Test Launch"
        // All other view references are commented out for this test layout
        // Uncomment and restore after restoring the full layout
    }
}