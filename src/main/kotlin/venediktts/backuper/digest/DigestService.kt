package venediktts.backuper.digest

import java.io.InputStream
import java.security.DigestInputStream
import java.security.MessageDigest

class DigestService {
    fun hash(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val digestInputStream = DigestInputStream(inputStream, digest)
        digestInputStream.use {
            while (it.read() != -1) { }
        }
        return toHex(digest.digest())
    }

    fun hash(str: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return toHex(digest.digest(str.toByteArray()))
    }

    private fun toHex(digest: ByteArray) = digest.joinToString(separator = "") { "%02x".format(it) }
}