package venediktts.backuper.config

import venediktts.backuper.service.*
import venediktts.backuper.digest.DigestService
import venediktts.backuper.digest.FileDigestService
import venediktts.backuper.service.ZipService

object ServiceConfigurer {
    fun initServices(config: Config) {
        val digestService = DigestService()
        val fileDigestService = FileDigestService(digestService)
        val sourceDirService = SourceDirectoryService(config, fileDigestService)
        val zipService = ZipService()
        val targetDirService = TargetDirectoryService(config, zipService)
        val copyService = CopyService(config, zipService, targetDirService)
        BackupService(config, sourceDirService, copyService)
        CleanupService(config, copyService)
    }
}