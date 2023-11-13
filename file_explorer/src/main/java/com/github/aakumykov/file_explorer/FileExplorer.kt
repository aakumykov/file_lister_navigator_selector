package com.github.aakumykov.file_explorer

import com.github.aakumykov.file_lister.FSItem
import com.github.aakumykov.file_lister.FileLister

/**
 * Предназначение - осуществлять навигацию по файловой системе.
 * Для этого реализации класса (ожидаемо) хранят состояние: начальный каталог (считающийся корневым)
 * и текущий путь.
 *
 * Интерфефс расширяет интерфейс FileLister, так как навигация в отрыве от получения списка файлов бессмысленна.
 */
interface FileExplorer : FileLister {

    fun goToChildDir(dirName: String) // throws IOException, AccessDeniesException
    fun goToParentDir() // throws IOException, AccessDeniesException
    fun goToRootDir() // throws IOException, AccessDeniesException

    fun listCurrentPath(): List<FSItem>
    fun getCurrentPath(): String

    companion object {
        // Используется в качестве "последнего пути", когда переходы в родительский
        // каталог приводят к тому, что секущий путь превращается в пустую строку.
        const val ROOT_DIR_PATH: String = "/"
    }
}