package venediktts.backuper.config

enum class ConfigProperty(
    val key: String,
    val required: Boolean,
    val defaultValue: String = ""
) {
    BACKUP_SOURCE_DIR("backup.source.dir", true),
    BACKUP_TARGET_DIR("backup.target.dir", true),
    BACKUP_FILE_PREFIX("backup.file.prefix", false, ""),
    BACKUP_COPY_PERIOD_SECONDS("backup.copy.period", false, "3600"),
    BACKUP_CHECK_PERIOD_SECONDS("backup.check.period", false, "60"),
    CLEANUP_FILE_TTL_SECONDS("cleanup.file.ttl", false, "2592000"),
    CLEANUP_CHECK_PERIOD_SECONDS("cleanup.check.period", false, "300");
}