package venediktts.backuper.hash

import java.io.InputStream
import java.security.DigestInputStream
import java.security.MessageDigest

object HashService {
    fun hash(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val digestInputStream = DigestInputStream(inputStream, digest)
        while (digestInputStream.read() != -1) {}
        return toHex(digest.digest())
    }

    private fun toHex(digest: ByteArray) = digest.joinToString(separator = "") { "%02x".format(it) }
}