package com.github.aakumykov.file_lister_navigator_selector.file_selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.aakumykov.file_lister_navigator_selector.file_explorer.FileExplorer
import com.github.aakumykov.file_lister_navigator_selector.fs_item.DirItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.FSItem
import com.github.aakumykov.file_lister_navigator_selector.fs_item.ParentDirItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileSelectorViewModel<SortingModeType> (
    private val fileExplorer: FileExplorer<SortingModeType>,
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

    val currentSortingMode get() = fileExplorer.getSortingMode()
    val isReverseOrder: Boolean get() = fileExplorer.getReverseOrder()
    val isFoldersFirst: Boolean get() = fileExplorer.getFoldersFirst()

    fun startWork() {
        listCurrentPath()
    }

    fun reopenCurrentDir() {
        listCurrentPath()
    }

    fun onItemClick(position: Int) {
        getItemAtPosition(position).also { fsItem ->
            fsItem?.also {
                if (fsItem.isDir) {
                    fileExplorer.changeDir(fsItem as DirItem)
                    listCurrentPath()
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

    fun onBackClicked() {
        fileExplorer.changeDir(ParentDirItem())
        listCurrentPath()
    }

    private fun getItemAtPosition(position: Int): FSItem? {
        return _currentList.value?.get(position)
    }

    private fun listCurrentPath() {

        _currentPath.value = fileExplorer.getCurrentPath()
        _selectedList.value = emptyList()

        viewModelScope.launch {

            _isBusy.value = true

            try {
                withContext(Dispatchers.IO) {
                    fileExplorer.listCurrentPath()
                }.also {
                    _currentList.value = it
                }
            } catch (e: Exception) {
                _currentError.value = e
            } finally {
                _isBusy.value = false
            }
        }
    }

    fun changeSortingMode(sortingMode: SortingModeType) {
        fileExplorer.changeSortingMode(sortingMode)
        listCurrentPath()
    }

    fun changeReverseOrder(isReverseOrder: Boolean) {
        fileExplorer.setReverseOrder(isReverseOrder)
        listCurrentPath()
    }

    fun changeFoldersFist(foldersFirst: Boolean) {
        fileExplorer.setFoldersFirst(foldersFirst)
        listCurrentPath()
    }

    class Factory<SortingModeType>(
        private val fileExplorer: FileExplorer<SortingModeType>,
    )
        : ViewModelProvider.Factory
    {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FileSelectorViewModel(fileExplorer) as T
        }
    }
}