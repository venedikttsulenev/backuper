package venediktts.backuper.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimestampUtil {
    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

    fun parseTimestamp(str: String): LocalDateTime = LocalDateTime.parse(str, timestampFormatter)

    fun formatTimestamp(localDateTime: LocalDateTime): String = localDateTime.format(timestampFormatter);
}