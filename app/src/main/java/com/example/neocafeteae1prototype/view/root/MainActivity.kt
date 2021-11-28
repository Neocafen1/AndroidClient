package com.example.neocafeteae1prototype.view.root

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.internet_checker.ConnectionLiveData
import com.example.neocafeteae1prototype.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        connectionLiveData = ConnectionLiveData(this) // Слушаетель интернета

        val snackbar = Snackbar.make(binding.view, "Включите интернет", Snackbar.LENGTH_INDEFINITE).apply {
                setBackgroundTint(ContextCompat.getColor(this@MainActivity, R.color.red))
            }

        connectionLiveData.observe(this, { // если есть инет оно удаляет Snackba
            if (it == false) snackbar.show()
            else snackbar.dismiss()
        })
    }

}