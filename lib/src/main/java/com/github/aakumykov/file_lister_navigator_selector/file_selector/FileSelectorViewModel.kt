package com.github.aakumykov.file_lister_navigator_selector.file_selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem

class FileSelectorViewModel(initialSortingMode: SortingMode = SortingMode.NAME) : ViewModel() {

    private var currentSortingMode: SortingMode = initialSortingMode
    private val _sortingModeMutableLiveData: MutableLiveData<SortingMode> = MutableLiveData(initialSortingMode)
    val sortingMode: LiveData<SortingMode> = _sortingModeMutableLiveData

    private val _currentPathMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val currentPath get(): LiveData<String> = _currentPathMutableLiveData

    private val _fileListMutableLiveData: MutableLiveData<List<FSItem>> = MutableLiveData()
    val fileList get(): LiveData<List<FSItem>> = _fileListMutableLiveData

    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    val errorMessage get(): LiveData<Throwable> = _errorMutableLiveData

    private val _selectedList: MutableList<FSItem> = mutableListOf()
    private val _selectedListMutableLiveData: MutableLiveData<List<FSItem>> = MutableLiveData(_selectedList)
    val selectedList get(): LiveData<List<FSItem>> = _selectedListMutableLiveData


    fun setFileList(list: List<FSItem>) {
        _fileListMutableLiveData.value = list
    }


    fun toggleItemSelection(fsItem: FSItem) {
        if (_selectedList.contains(fsItem))
            _selectedList.remove(fsItem)
        else
            _selectedList.add(fsItem)
        _selectedListMutableLiveData.value = _selectedList
    }

    fun setSelectedItem(fsItem: FSItem) {
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

    fun getSelectedList(): List<FSItem> {
        return _selectedList
    }

    fun toggleSortingDirection() {
        _sortingModeMutableLiveData.value = currentSortingMode
    }
}