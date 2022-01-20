package com.example.test_graph_application.repository

import android.content.Context
import com.example.test_graph_application.api.ApiService
import com.example.test_graph_application.api.Dataset
import com.example.test_graph_application.utils.DouglasPeukerSimplification
import com.example.test_graph_application.utils.FileSystemHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*

class Repository(
    private val api: ApiService,
    private val context: Context,
    private val gson: Gson
) {

    companion object {
        private const val MAX_CACHED_FILES_INFO = 13
        private const val NAME_DIRECTORY_INFORMATION = "info"
        private const val FILE_NAME = "data"
        private const val EPSILON = 280.0
    }

    suspend fun getFromOrLoadFileToCacheDir() = flow {
        val file: File

        val resultList = mutableListOf<Dataset>()

        val fileFolder = File(getFolderCacheInfo())
        if (!fileFolder.exists()) fileFolder.mkdir()

        file = File(fileFolder, FILE_NAME)

        if (!file.exists()) {
            try {
                val response: ResponseBody = api.downloadFile()

                val inputStream: InputStream = response.byteStream()

                inputStream.use { stream ->
                    FileSystemHelper.writeFile(fileName = file.path, stream)
                    FileSystemHelper.checkAndDeleteOldFile(
                        path = file.path,
                        maxCount = MAX_CACHED_FILES_INFO
                    )
                }

                withContext(Dispatchers.Default) {
                    DouglasPeukerSimplification.ramerDouglasPeucker(
                        parseFileToList(file.path),
                        EPSILON,
                        resultList
                    )
                }

                emit(resultList)

            } catch (ex: Exception) {
                Timber.e(ex)
            }
        } else {
            withContext(Dispatchers.Default) {
                DouglasPeukerSimplification.ramerDouglasPeucker(
                    parseFileToList(file.path),
                    EPSILON,
                    resultList
                )
            }

            emit(resultList)
        }
    }.flowOn(Dispatchers.IO)

    private fun getFolderCacheInfo(): String {
        return FileSystemHelper.getTmpFolder(context) + NAME_DIRECTORY_INFORMATION
    }

    private fun parseFileToList(filePath: String): List<Dataset> {
        val file = File(filePath)

        val inputStream = FileInputStream(file)
        val resultString = inputStream.use {
            convertStreamToString(it)
        }

        val listOfDataSet = resultString.split("\n")

        return listOfDataSet.mapNotNull { gson.fromJson(it, Dataset::class.java) }
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