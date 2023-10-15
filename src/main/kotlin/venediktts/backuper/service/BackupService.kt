package venediktts.backuper.backup

import mu.KotlinLogging
import venediktts.backuper.config.Config
import venediktts.backuper.model.NewCopy
import java.lang.Exception
import java.time.LocalDateTime
import kotlin.concurrent.timer

class BackupService(
    config: Config,
    private val sourceDirectoryService: SourceDirectoryService,
    private val targetDirectoryService: TargetDirectoryService
) {
    private val log = KotlinLogging.logger(this.javaClass.name)
    private val checkPeriodMillis = 1000L * config.getBackupCheckPeriodSeconds()
    private val copyPeriodSeconds = config.getBackupCopyPeriodSeconds()

    init {
        timer(
            name = "backup-thread",
            daemon = false,
            initialDelay = 0L,
            period = checkPeriodMillis
        ) {
            tryBackup()
        }
    }

    private fun tryBackup() {
        try {
            if (!sourceDirectoryService.isAvailable()) { log.info("Source directory is not available"); return }
            val currentDateTime = LocalDateTime.now()
            if (tooEarlyForNewCopy(currentDateTime)) { log.info("Last copy is relevant"); return }
            val checksum = sourceDirectoryService.calculateCheckSum()
            if (checksumNotChanged(checksum)) { log.info("No changes detected ($checksum)"); return }
            createBackup(currentDateTime, checksum)
        } catch (e: Exception) {
            log.error("Error trying to backup: ${e.message}", e)
        }
    }

    private fun createBackup(timestamp: LocalDateTime, checksum: String) {
        val newCopy = NewCopy(timestamp, sourceDirectoryService.directory, checksum)
        targetDirectoryService.createNewCopy(newCopy)
            .also { log.info("Created new copy at target directory") }
        targetDirectoryService.createBackupZipFile(sourceDirectoryService.directory, timestamp)
            .also { log.info("Created '$it' at target directory") }
        targetDirectoryService.createBackupChecksumFile(timestamp, checksum!!)
            .also { log.info("Created '$it' at target directory") }
    }

    private fun tooEarlyForNewCopy(currentDateTime: LocalDateTime): Boolean {
        val lastCopyDateTime = targetDirectoryService.getLastCopyDateTime()
        return lastCopyDateTime != null && lastCopyDateTime.plusSeconds(copyPeriodSeconds).isAfter(currentDateTime)
    }

    private fun checksumNotChanged(currentChecksum: String?): Boolean {
        val lastCopyChecksum = targetDirectoryService.getLastCopyChecksum()
        return lastCopyChecksum.equals(currentChecksum)
    }
}