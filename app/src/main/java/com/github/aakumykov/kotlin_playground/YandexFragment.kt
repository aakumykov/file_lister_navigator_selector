package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.aakumykov.kotlin_playground.databinding.FragmentYandexBinding

class YandexFragment : Fragment(R.layout.fragment_yandex) {

    private var _binding: FragmentYandexBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentYandexBinding.bind(view)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}