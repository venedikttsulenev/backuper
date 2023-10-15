package venediktts.backuper.model

import java.io.File
import java.time.LocalDateTime

data class ExistingCopy(
    val timestamp: LocalDateTime,
    val zipFile: File,
    val checksumFile: File
)
