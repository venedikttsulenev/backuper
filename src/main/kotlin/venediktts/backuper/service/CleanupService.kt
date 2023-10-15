package venediktts.backuper.service

import mu.KotlinLogging
import venediktts.backuper.config.Config
import venediktts.backuper.exception.HandleableException
import java.io.File
import java.time.LocalDateTime
import kotlin.concurrent.timer

class CleanupService(
    private val config: Config,
    private val copyService: CopyService
) {
    private val log = KotlinLogging.logger(this.javaClass.name)

    init {
        timer(
            name = "cleanup-thread",
            daemon = false,
            initialDelay = 0L,
            period = 1000L * config.getCleanupCheckPeriodSeconds()
        ) {
            tryCleanup()
        }
    }

    private fun tryCleanup() {
        try {
            cleanup(LocalDateTime.now())
        } catch (e: HandleableException) {
            log.info(e.message)
        } catch (e: Exception) {
            log.error("Error trying to cleanup: ${e.message}", e)
        }
    }

    private fun cleanup(currentTimestamp: LocalDateTime) {
        val lastCopy = copyService.getLastExistingCopy()
        val minTimestampToRetain = currentTimestamp.minusSeconds(config.getCleanupFileTTLSeconds())
        val filesToRemove = copyService.getCopyFilesByTimestamp()
            .filter { it.first.isBefore(minTimestampToRetain) && !it.first.isEqual(lastCopy.timestamp) }
            .map(Pair<LocalDateTime, File>::second)
        filesToRemove.forEach(File::delete)
        log.info("Removed ${filesToRemove.size} outdated files.")
    }
}