package venediktts.backuper.backup

import venediktts.backuper.config.Config
import venediktts.backuper.digest.FileDigestService

class SourceDirectoryService(
    config: Config,
    private val fileDigestService: FileDigestService
) : DirectoryService(config.getBackupSourceDir()) {

    fun calculateCheckSum(): String = fileDigestService.digest(super.directory)
}