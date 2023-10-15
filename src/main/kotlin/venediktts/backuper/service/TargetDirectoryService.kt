package venediktts.backuper.backup

import venediktts.backuper.TimestampUtil
import venediktts.backuper.config.Config
import venediktts.backuper.model.ExistingCopy
import venediktts.backuper.model.NewCopy
import venediktts.backuper.zip.ZipService
import java.io.File
import java.time.LocalDateTime

class TargetDirectoryService(
    config: Config,
    private val zipService: ZipService
) : DirectoryService(config.getBackupTargetDir()) {
    private val absolutePath = super.directory.absolutePath
    private val filePrefix = config.getBackupFilePrefix()

    fun getLastCopyDateTime() = listZipFiles()
        ?.maxOfOrNull { getFileTimestamp(it) }

    fun getLastCopyChecksum() = listChecksumFiles()
        ?.maxByOrNull { getFileTimestamp(it) }
        ?.readText()

    fun getCopiesSortedByTimestampDescending(): List<ExistingCopy> {
        val zipFilesByTimestamp = listZipFiles()
            ?.associate { Pair(getFileTimestamp(it), it) }
            ?: emptyMap()
        val checksumFilesByTimestamp = listChecksumFiles()
            ?.associate { Pair(getFileTimestamp(it), it) }
            ?: emptyMap()
        return zipFilesByTimestamp.mapNotNull { entry ->
            val timestamp = entry.key
            val zipFile = entry.value
            val checksumFile = checksumFilesByTimestamp[timestamp]
            return@mapNotNull if (checksumFile != null) ExistingCopy(timestamp, zipFile, checksumFile) else null
        }.sortedByDescending(ExistingCopy::timestamp)
    }

    fun createBackupZipFile(sourceDir: File, timestamp: LocalDateTime) : String {
        val zipName = "${filePrefix}_backup_${TimestampUtil.formatTimestamp(timestamp)}.zip"
        val zipFilePath = "$absolutePath/$zipName"
        zipService.createZipRecursive(sourceDir, zipFilePath)
        return zipName
    }

    fun createBackupChecksumFile(timestamp: LocalDateTime, text: String) : String {
        val fileName = "${filePrefix}_checksum_${TimestampUtil.formatTimestamp(timestamp)}.checksum"
        File("$absolutePath/$fileName").writeText(text)
        return fileName
    }

    fun createNewCopy(newCopy: NewCopy): String {
        val fileName = "${filePrefix}_${TimestampUtil.formatTimestamp(newCopy.timestamp)}"
        zipService.createZipRecursive(newCopy.sourceDirectory, "$absolutePath/$fileName.zip")
        File("$absolutePath/$fileName.checksum").writeText(newCopy.checksumValue);
        return fileName
    }

    fun removeCopy(existingCopy: ExistingCopy) {
        existingCopy.zipFile.delete()
        existingCopy.checksumFile.delete()
    }

    private fun listZipFiles(): Array<File>? = super.directory.listFiles { _, name -> name.endsWith(".zip") }

    private fun listChecksumFiles(): Array<File>? = super.directory.listFiles { _, name -> name.endsWith(".checksum") }

    private fun getFileTimestamp(file: File): LocalDateTime = TimestampUtil.parseTimestamp(file.nameWithoutExtension.substringAfterLast('_'))
}