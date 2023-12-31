package venediktts.backuper.service

import venediktts.backuper.exception.HandleableException
import java.io.File

abstract class DirectoryService(path: String, private val purpose: String) {
    protected val directory = File(path)

    private fun isAvailable() = directory.exists() && directory.isDirectory

    protected fun <T> directorySafe(callable: () -> T): T {
        if (!isAvailable()) {
            throw HandleableException("$purpose directory is not available")
        }
        return callable()
    }

    fun getDirectorySafe() = directorySafe { directory }
}