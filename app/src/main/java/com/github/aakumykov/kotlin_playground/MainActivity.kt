package com.github.aakumykov.kotlin_playground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.kotlin_playground.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener { onButtonClicked() }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun onButtonClicked() {

        val fileExplorer = mainViewModel.getFileExplorer()
        fileExplorer.goToRootDir()

        fileExplorer.listDir(fileExplorer.getCurrentPath()).let { list ->
            val sb = StringBuilder()
            for (fsItem: FSItem in list) {
                sb.append(fsItem.name)
                sb.append("\n")
            }
            binding.textView.text = sb
        }
    }
}