package venediktts.backuper.zip

import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipService {
    fun createZipRecursive(sourceDirectory: File, targetFilePath: String) {
        val zipOutputStream = ZipOutputStream(FileOutputStream(targetFilePath))
        processDirectory(sourceDirectory, sourceDirectory, zipOutputStream)
        zipOutputStream.close()
    }

    private fun processDirectory(baseDir: File, dir: File, zipOutputStream: ZipOutputStream) {
        dir.listFiles()?.forEach {
            if (it.isDirectory) processDirectory(baseDir, it, zipOutputStream)
            else {
                val filePath = it.relativeTo(baseDir).path
                val zipEntry = ZipEntry(filePath)
                zipOutputStream.putNextEntry(zipEntry)
                zipOutputStream.write(it.readBytes())
                zipOutputStream.closeEntry()
            }
        }
    }
}