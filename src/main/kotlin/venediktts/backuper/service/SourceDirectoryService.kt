package venediktts.backuper.service

import venediktts.backuper.config.Config
import venediktts.backuper.digest.FileDigestService

class SourceDirectoryService(
    config: Config,
    private val fileDigestService: FileDigestService
) : DirectoryService(config.getBackupSourceDir(), "Source") {

    fun getDigest(): String = directorySafe {
        fileDigestService.digest(super.directory)
    }

}