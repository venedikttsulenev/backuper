package venediktts.backuper.service

import venediktts.backuper.util.TimestampUtil
import venediktts.backuper.config.Config
import venediktts.backuper.model.ExistingCopy
import venediktts.backuper.model.NewCopy
import java.io.File
import java.time.LocalDateTime

class CopyService(
    config: Config,
    private val targetDirectoryService: TargetDirectoryService
) {
    private val filePrefix = config.getBackupFilePrefix()

    fun getLastExistingCopy(): ExistingCopy? {
        val zipFiles = getZipFilesByTimestamp()
        val checksumFiles = getChecksumFilesByTimestamp()
        val maxExistingTimestamp = zipFiles.keys.intersect(checksumFiles.keys).maxOrNull()
        return maxExistingTimestamp?.let {
            ExistingCopy(
                maxExistingTimestamp,
                zipFiles[maxExistingTimestamp]!!,
                checksumFiles[maxExistingTimestamp]!!
            )
        }
    }

    fun getCopyFilesByTimestamp(): List<Pair<LocalDateTime, File>> =
        getZipFilesByTimestamp().toList() + getChecksumFilesByTimestamp().toList()

    fun createNewCopy(newCopy: NewCopy): String {
        val fileName = "${filePrefix}_${TimestampUtil.formatTimestamp(newCopy.timestamp)}"
        targetDirectoryService.createZipFile(newCopy.sourceDirectory, "$fileName.zip")
        targetDirectoryService.createTextFile(newCopy.checksumValue, "$fileName.checksum")
        return fileName
    }

    private fun getZipFilesByTimestamp() =
        targetDirectoryService.listFilesEndingWith(".zip")
            .associateBy(this::getFileTimestamp)

    private fun getChecksumFilesByTimestamp() =
        targetDirectoryService.listFilesEndingWith(".checksum")
            .associateBy(this::getFileTimestamp)

    private fun getFileTimestamp(file: File): LocalDateTime =
        TimestampUtil.parseTimestamp(file.nameWithoutExtension.substringAfterLast('_'))
}