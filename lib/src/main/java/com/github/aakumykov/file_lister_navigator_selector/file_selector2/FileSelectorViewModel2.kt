package com.github.aakumykov.file_lister_navigator_selector.file_selector2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.aakumykov.file_lister_navigator_selector.file_lister.SortingMode
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_navigator.FileExplorer
import kotlinx.coroutines.launch

class FileSelectorViewModel2 (
    private val fileExplorer: FileExplorer<SortingMode>
)
    : ViewModel()
{
    private val _currentPath: MutableLiveData<String> = MutableLiveData()
    private val _currentList: MutableLiveData<List<FSItem>> = MutableLiveData(emptyList())
    private val _selectedList: MutableLiveData<List<FSItem>> = MutableLiveData(emptyList())
    private val _currentError: MutableLiveData<Throwable> = MutableLiveData()
    private val _isBusy: MutableLiveData<Boolean> = MutableLiveData()

    private val selectedItems: MutableList<FSItem> = mutableListOf()

    val path: LiveData<String> = _currentPath
    val list: LiveData<List<FSItem>> = _currentList
    val selectedList: LiveData<List<FSItem>> = _selectedList
    val errorMsg: LiveData<Throwable> = _currentError
    val isBusy: LiveData<Boolean> = _isBusy


    fun startWork() {
        processCurrentPath()
    }

    fun reopenCurrentDir() {
        processCurrentPath()
    }

    fun onItemClick(position: Int) {
        getItemAtPosition(position).also { fsItem ->
            fsItem?.also {
                if (fsItem.isDir) {
                    fileExplorer.changeDir(fsItem as DirItem)
                    processCurrentPath()
                }
            }
        }
    }

    fun onItemLongClick(position: Int) {
        getItemAtPosition(position)?.also { fsItem ->
            if (selectedItems.contains(fsItem))
                selectedItems.remove(fsItem)
            else
                selectedItems.add(fsItem)

            _selectedList.value = selectedItems
        }
    }

    private fun getItemAtPosition(position: Int): FSItem? {
        return _currentList.value?.get(position)
    }

    private fun processCurrentPath() {

        _currentPath.value = fileExplorer.getCurrentPath()
        _selectedList.value = emptyList()

        viewModelScope.launch {
            _isBusy.value = true
            try {
                _currentList.value = fileExplorer.listCurrentPath()
            } catch (e: Exception) {
                _currentError.value = e
            } finally {
                _isBusy.value = false
            }
        }
    }

    fun toggleSortingMode() {
        /*fileExplorer.setSortingComparator(
            when(fileExplorer.getSortingMode()) {
                SortingMode.NAME_DIRECT -> SortingMode.NAME_REVERSE
                else -> SortingMode.NAME_DIRECT
            }
        )*/
        processCurrentPath()
    }


    class Factory(
        private val fileExplorer: FileExplorer<SortingMode>
    )
        : ViewModelProvider.Factory
    {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FileSelectorViewModel2(fileExplorer) as T
        }
    }
}