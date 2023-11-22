package com.github.aakumykov.file_selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.github.aakumykov.file_selector.databinding.FragmentLayoutProbeBinding

class LayoutProbeDialog : DialogFragment(R.layout.fragment_layout_probe) {

    private var _binding: FragmentLayoutProbeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLayoutProbeBinding.bind(view)
    }

    companion object {
        val TAG: String = LayoutProbeDialog::class.java.simpleName
    }
}