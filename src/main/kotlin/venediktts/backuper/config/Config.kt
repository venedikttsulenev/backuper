package venediktts.backuper

import java.io.FileReader
import java.util.Properties

object Config {
    private val properties = Properties()

    fun load(path: String) {
        properties.load(FileReader(path))
    }

    fun getSourceDir() = properties.getProperty("backup.source.dir")
    fun getTargetDir() = properties.getProperty("backup.target.dir")
    fun getBackupFilePrefix() = properties.getProperty("backup.file.prefix")
    fun getCopyPeriodSeconds() = properties.getProperty("backup.copy.period").toInt()
    fun getCheckPeriodSeconds() = properties.getProperty("backup.check.period").toInt()

    private fun getConfigProperty()

    enum class ConfigProperty(
        val key: String,
        val required: Boolean,
        val defaultValue: Any? = null
    ) {
        BACKUP_SOURCE_DIR("backup.source.dir", true),
        BACKUP_TARGET_DIR("backup.target.dir", true),
        BACKUP_FILE_PREFIX("backup.file.prefix", false, ""),
        BACKUP_COPY_PERIOD_SECONDS("backup.copy.period", false, 3600),
        BACKUP_CHECK_PERIOD_SECONDS("backup.check.period", false, 60)
    }
}