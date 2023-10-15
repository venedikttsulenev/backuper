package venediktts.backuper.service

import mu.KotlinLogging
import venediktts.backuper.config.Config
import venediktts.backuper.exception.HandleableException
import venediktts.backuper.model.ExistingCopy
import venediktts.backuper.model.NewCopy
import java.lang.Exception
import java.time.LocalDateTime
import kotlin.concurrent.timer

class BackupService(
    config: Config,
    private val sourceDirectoryService: SourceDirectoryService,
    private val copyService: CopyService
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
            val lastCopy = copyService.getLastExistingCopy()
            val timestamp = LocalDateTime.now()
            val checksum = sourceDirectoryService.getDigest()
            if (tooEarlyForNewCopy(timestamp, lastCopy) || checksumNotChanged(checksum, lastCopy)) {
                return
            }
            createBackup(timestamp, checksum)
        } catch (e: HandleableException) {
            log.info(e.message)
        } catch (e: Exception) {
            log.error("Error trying to backup: ${e.message}", e)
        }
    }

    private fun createBackup(timestamp: LocalDateTime, checksum: String) {
        val newCopy = NewCopy(timestamp, sourceDirectoryService.directory, checksum)
        copyService.createNewCopy(newCopy)
            .also { log.info("Created new copy at target directory (timestamp: $timestamp, checksum: $checksum)") }
    }

    private fun tooEarlyForNewCopy(currentDateTime: LocalDateTime, lastCopy: ExistingCopy?) =
        lastCopy != null && lastCopy.timestamp.plusSeconds(copyPeriodSeconds).isAfter(currentDateTime)

    private fun checksumNotChanged(currentChecksum: String?, lastCopy: ExistingCopy?) =
        lastCopy != null && lastCopy.checksumFile.equals(currentChecksum)
}