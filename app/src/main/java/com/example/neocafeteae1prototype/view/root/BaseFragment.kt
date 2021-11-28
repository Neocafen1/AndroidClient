package com.example.neocafeteae1prototype.view.root

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

abstract class BaseFragment<Binding : ViewBinding> : Fragment() {

    private var _binding: Binding? = null
    val binding get() = _binding!!
    val navController by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = inflateView(inflater, container)
        setUpToolbar()
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?): Binding

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setUpToolbar()
}