package venediktts.backuper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimestampUtil {
    private val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

    public fun parseTimestamp(str: String) = LocalDateTime.parse(str, timestampFormatter)

    public fun formatTimestamp(localDateTime: LocalDateTime) = localDateTime.format(timestampFormatter);
}