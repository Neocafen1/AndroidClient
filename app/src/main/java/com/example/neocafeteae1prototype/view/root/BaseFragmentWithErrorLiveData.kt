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
import androidx.viewbinding.ViewBinding
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.showToast

abstract class BaseFragmentWithErrorLiveData<Binding : ViewBinding> : Fragment() {

    private var _binding: Binding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = inflateView(inflater, container)
        setUpToolbar()
        errorListener().observe(viewLifecycleOwner){
            if (it){
                "Error".showToast(requireContext(), Toast.LENGTH_LONG)
            }
        }
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup?): Binding

    abstract fun errorListener(): LiveData<Boolean>

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setUpToolbar()

}