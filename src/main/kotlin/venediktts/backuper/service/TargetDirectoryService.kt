package venediktts.backuper.service

import venediktts.backuper.config.Config
import java.io.File

class TargetDirectoryService(
    config: Config,
    private val zipService: ZipService
) : DirectoryService(config.getBackupTargetDir(), "Target") {
    private val absolutePath = super.directory.absolutePath

    fun createZipFile(sourceDir: File, zipFileName: String) = directorySafe {
        val zipFilePath = "$absolutePath/$zipFileName"
        zipService.createZipRecursive(sourceDir, zipFilePath)
    }

    fun createTextFile(text: String, fileName: String) = directorySafe {
        File("$absolutePath/$fileName").writeText(text)
    }

    fun listFilesEndingWith(suffix: String): Array<File> = directorySafe {
        super.directory.listFiles { _, name -> name.endsWith(suffix) }!!
    }
}