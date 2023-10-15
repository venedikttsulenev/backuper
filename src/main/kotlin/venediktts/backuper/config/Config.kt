package venediktts.backuper.config

import mu.KotlinLogging
import venediktts.backuper.config.ConfigProperty.*
import java.io.FileReader
import java.lang.RuntimeException
import java.util.Properties

class Config(path: String) {
    private val log = KotlinLogging.logger(this.javaClass.name)
    private val properties = Properties()

    init {
        properties.load(FileReader(path))
        ConfigProperty.values().forEach {
            if (it.required && !properties.containsKey(it.key)) {
                throw RuntimeException("Missing required config property: ${it.key}")
            }
            log.info("${it.key}=${getConfigPropertyString(it)}")
        }
    }

    fun getBackupSourceDir(): String = getConfigPropertyString(BACKUP_SOURCE_DIR)
    fun getBackupTargetDir(): String = getConfigPropertyString(BACKUP_TARGET_DIR)
    fun getBackupFilePrefix(): String = getConfigPropertyString(BACKUP_FILE_PREFIX)
    fun getBackupCopyPeriodSeconds() = getConfigPropertyLong(BACKUP_COPY_PERIOD_SECONDS)
    fun getBackupCheckPeriodSeconds() = getConfigPropertyLong(BACKUP_CHECK_PERIOD_SECONDS)
    fun getCleanupFileTTLSeconds() = getConfigPropertyLong(CLEANUP_FILE_TTL_SECONDS)
    fun getCleanupCheckPeriodSeconds() = getConfigPropertyLong(CLEANUP_CHECK_PERIOD_SECONDS)

    private fun getConfigPropertyString(prop: ConfigProperty) = properties.getProperty(prop.key, prop.defaultValue)
    private fun getConfigPropertyLong(prop: ConfigProperty) = getConfigPropertyString(prop).toLong()
}