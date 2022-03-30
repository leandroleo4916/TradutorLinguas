package com.example.tradutorlinguas.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradutorlinguas.R
import com.example.tradutorlinguas.databinding.ActivityTranslateSaveBinding

class ActivityTranslateSave : AppCompatActivity() {

    private val binding by lazy { ActivityTranslateSaveBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }
}