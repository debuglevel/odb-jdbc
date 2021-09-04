package de.debuglevel.odbjdbc

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class UnzipUtility {
    @Throws(IOException::class)
    fun unzip(zip: String, destinationDir: String?) {
        val destDir = File(destinationDir)
        val buffer = ByteArray(1024)
        val zis = ZipInputStream(FileInputStream(zip))
        var zipEntry = zis.nextEntry
        while (zipEntry != null) {
            val newFile = newFile(destDir, zipEntry)
            val fos = FileOutputStream(newFile)
            var len: Int
            while (zis.read(buffer).also { len = it } > 0) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()
    }

    companion object {
        /**
         * @see https://snyk.io/research/zip-slip-vulnerability
         */
        @Throws(IOException::class)
        fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
            val destFile = File(destinationDir, zipEntry.name)
            val destDirPath = destinationDir.canonicalPath
            val destFilePath = destFile.canonicalPath
            Files.createDirectories(destFile.parentFile.toPath())
            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw IOException("Entry is outside of the target dir: " + zipEntry.name)
            }
            return destFile
        }
    }
}