package venediktts.backuper.backup

import mu.KotlinLogging
import venediktts.backuper.config.Config
import java.time.LocalDateTime
import kotlin.concurrent.timer

class CleanupService(
    private val config: Config,
    private val targetDirService: TargetDirectoryService
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
            if (!targetDirService.isAvailable()) {
                log.info("Target directory is not available")
                return
            }
            cleanup(LocalDateTime.now())
        } catch (e: Exception) {
            log.error("Error trying to cleanup: ${e.message}", e)
        }
    }

    private fun cleanup(currentTimestamp: LocalDateTime) {
        val minTimestampToRetain = currentTimestamp.minusSeconds(config.getCleanupFileTTLSeconds())
        val copiesToRemove = targetDirService.getCopiesSortedByTimestampDescending()
            .drop(config.getCleanupMinCopies())
            .filter { it.timestamp.isBefore(minTimestampToRetain) }
        copiesToRemove.forEach(targetDirService::removeCopy)
        log.info("Removed ${copiesToRemove.size} outdated copies.")
    }
}