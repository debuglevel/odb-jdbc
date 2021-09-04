package de.debuglevel.odbjdbc

import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class UnzipUtility {
    private val logger = KotlinLogging.logger {}

    fun unzip(zip: String, destinationDirectory: File) {
        logger.trace { "Unzipping $zip to $destinationDirectory" }

        val buffer = ByteArray(1024)
        val zipInputStream = ZipInputStream(FileInputStream(zip))

        var zipEntry = zipInputStream.nextEntry
        while (zipEntry != null) {
            logger.trace { "Processing ZIP entry $zipEntry..." }

            val destinationFile = newFile(destinationDirectory, zipEntry)
            val fileOutputStream = FileOutputStream(destinationFile)
            var len: Int
            while (zipInputStream.read(buffer).also { len = it } > 0) {
                fileOutputStream.write(buffer, 0, len)
            }
            fileOutputStream.close()

            zipEntry = zipInputStream.nextEntry
        }
        zipInputStream.closeEntry()

        logger.trace { "Closing..." }
        zipInputStream.close()
    }

    companion object {
        /**
         * @see https://snyk.io/research/zip-slip-vulnerability
         */
        @Throws(IOException::class)
        fun newFile(destinationDir: File, zipEntry: ZipEntry): File {
            val destinationFile = File(destinationDir, zipEntry.name)
            val destinationDirectoryPath = destinationDir.canonicalPath
            val destinationFilePath = destinationFile.canonicalPath
            Files.createDirectories(destinationFile.parentFile.toPath())
            if (!destinationFilePath.startsWith(destinationDirectoryPath + File.separator)) {
                throw IOException("Entry is outside of the target dir: " + zipEntry.name)
            }
            return destinationFile
        }
    }
}