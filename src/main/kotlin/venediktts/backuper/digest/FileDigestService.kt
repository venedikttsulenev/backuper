package venediktts.backuper.digest

import java.io.File
import java.io.FileInputStream

class FileDigestService(
    private val digestService: DigestService
) {
    fun digest(file: File) =
        if (file.isDirectory) processDirectory(file)!!
        else processStandaloneFile(file)

    private fun processStandaloneFile(file: File): String {
        return digestService.hash(FileInputStream(file))
    }

    private fun processDirectory(dir: File): String? {
        val hashConcat = dir.listFiles()?.mapNotNull {
            if (it.isHidden) null
            else if (it.isDirectory) processDirectory(it)
            else processStandaloneFile(it)
        }
        ?.joinToString(separator = "")
        return hashConcat?.let { digestService.hash(hashConcat) }
    }
}