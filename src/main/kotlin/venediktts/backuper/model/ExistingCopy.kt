package venediktts.backuper.model

import java.io.File
import java.time.LocalDateTime

data class Copy(
    val timestamp: LocalDateTime,
    val zipFile: File,
    val checksumFile: File
)
