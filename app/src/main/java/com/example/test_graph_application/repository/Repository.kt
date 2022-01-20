package com.example.test_graph_application.repository

import android.content.Context
import com.example.test_graph_application.api.ApiService
import com.example.test_graph_application.api.DatasetRequest
import com.example.test_graph_application.utils.FileSystemHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*

class Repository(private val api: ApiService, private val context: Context, private val gson: Gson) {

    companion object {
        private const val MAX_CACHED_FILES_INFO = 13
        private const val NAME_DIRECTORY_INFORMATION = "info"
        private const val FILE_NAME = "data"
    }

    suspend fun getFromOrLoadFileToCacheDir() = flow {
        val file: File

        val fileFolder = File(getFolderCacheInfo())
        if (!fileFolder.exists()) fileFolder.mkdir()

        file = File(fileFolder, FILE_NAME)

        if (!file.exists()) {
            try {
                val response: ResponseBody =
                    withContext(Dispatchers.IO) { api.downloadFile() }
                val inputStream: InputStream = response.byteStream()

                inputStream.use { stream ->
                    FileSystemHelper.writeFile(fileName = file.path, stream)
                    FileSystemHelper.checkAndDeleteOldFile(
                        path = file.path,
                        maxCount = MAX_CACHED_FILES_INFO
                    )
                }

                emit(parseFileToList(file.path))

            } catch (ex: Exception) {
                Timber.e(ex)
            }
        } else {
            emit(parseFileToList(file.path))
        }
    }

    private fun getFolderCacheInfo(): String {
        return FileSystemHelper.getTmpFolder(context) + NAME_DIRECTORY_INFORMATION
    }

    private fun parseFileToList(filePath: String): List<DatasetRequest> {
        val file = File(filePath)

        val inputStream = FileInputStream(file)
        val resultString = inputStream.use {
            convertStreamToString(it)
        }

        val listOfDataSet = resultString.split("\n")

        return listOfDataSet.map { gson.fromJson(it, DatasetRequest::class.java) }
    }

    private fun convertStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }
}