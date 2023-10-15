package venediktts.backuper.model

import java.io.File
import java.time.LocalDateTime

data class NewCopy(
    val timestamp: LocalDateTime,
    val sourceDirectory: File,
    val checksumValue: String
)
