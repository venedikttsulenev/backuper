package venediktts.backuper.backup

import java.io.File

open class DirectoryService(path: String) {
    val directory = File(path)

    fun isAvailable() = directory.exists() && directory.isDirectory
}