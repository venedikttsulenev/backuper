package venediktts.backuper.config

import venediktts.backuper.backup.BackupService
import venediktts.backuper.backup.CleanupService
import venediktts.backuper.backup.SourceDirService
import venediktts.backuper.backup.TargetDirService

class ServiceProvider(config: Config) {
    public val sourceDirService = SourceDirService(config)
    public val targetDirService = TargetDirService(config)
    public val backupService = BackupService(config, sourceDirService, targetDirService)
    public val cleanupService = CleanupService(config, targetDirService)
}