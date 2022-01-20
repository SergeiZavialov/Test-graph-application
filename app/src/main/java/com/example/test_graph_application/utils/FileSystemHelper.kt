package com.example.test_graph_application.utils

import android.content.Context
import android.os.Environment
import timber.log.Timber
import java.io.DataInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

object FileSystemHelper {

    fun getTmpFolder(context: Context): String {
        var fileDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (fileDir == null) fileDir = context.cacheDir
        return fileDir?.absolutePath ?: ""
    }

    fun writeFile(fileName: String, stream: InputStream?) {
        val fos = FileOutputStream(File(fileName))
        val buffer = ByteArray(1024)
        var length: Int
        var dis: DataInputStream? = null
        try {
            dis = DataInputStream(stream)
            while (dis.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            fos.close()
            dis?.close()
        }
    }

    fun checkAndDeleteOldFile(path: String, maxCount: Int) {
        val directory = File(path)
        val files: Array<File> = directory.listFiles() ?: arrayOf()
        val listFiles: ArrayList<Pair<Date, File>> = ArrayList()
        if (files.size < maxCount) return
        for (i in files.indices) {
            val file: File = files[i]
            val lastModDate = Date(file.lastModified())
            listFiles.add(Pair(lastModDate, file))
        }
        listFiles.sortWith { o1, o2 -> o1.first.compareTo(o2.first) }
        var count: Int = listFiles.size - (maxCount - 1)
        for ((_, second) in listFiles) {
            if (count < 1) break
            second.delete()
            count--
        }
    }
}