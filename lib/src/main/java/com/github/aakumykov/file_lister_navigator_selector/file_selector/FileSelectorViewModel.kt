package com.github.aakumykov.file_lister_navigator_selector.file_selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FileSelectorViewModel : ViewModel() {

    private val _currentPathMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val currentPath get(): LiveData<String> = _currentPathMutableLiveData

    private val _fileListMutableLiveData: MutableLiveData<List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>> = MutableLiveData()
    val fileList get(): LiveData<List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>> = _fileListMutableLiveData

    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    val errorMessage get(): LiveData<Throwable> = _errorMutableLiveData

    private val _selectedList: MutableList<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> = mutableListOf()
    private val _selectedListMutableLiveData: MutableLiveData<List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>> = MutableLiveData(_selectedList)
    val selectedList get(): LiveData<List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>> = _selectedListMutableLiveData


    fun setFileList(list: List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem>) {
        _fileListMutableLiveData.value = list
    }


    fun toggleItemSelection(fsItem: com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem) {
        if (_selectedList.contains(fsItem))
            _selectedList.remove(fsItem)
        else
            _selectedList.add(fsItem)
        _selectedListMutableLiveData.value = _selectedList
    }

    fun setSelectedItem(fsItem: com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem) {
        clearSelectionList()
        _selectedList.add(fsItem)
        _selectedListMutableLiveData.value = _selectedList
    }

    fun clearSelectionList() {
        _selectedList.clear()
    }


    fun setCurrentPath(path: String) {
        _currentPathMutableLiveData.value = path
    }

    fun setError(throwable: Throwable) {
        _errorMutableLiveData.value = throwable
    }

    fun getSelectedList(): List<com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem> {
        return _selectedList
    }
}