package com.example.surveyapp.data.storage

import android.content.Context
import java.io.File

object FormsStorage {

    private const val FORMS_FOLDER_NAME = "completed_forms"

    fun saveFormJson(context: Context, fileName: String, jsonContent: String) {
        val dir = File(context.filesDir, FORMS_FOLDER_NAME)
        if (!dir.exists()) {
            dir.mkdirs()  // mkdirs para crear toda la estructura
        }
        val file = File(dir, "$fileName.json")
        try {
            file.writeText(jsonContent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Aquí podrías agregar manejo de error más elaborado si querés
        }
    }

    fun getAllSavedForms(context: Context): List<File> {
        val dir = File(context.filesDir, FORMS_FOLDER_NAME)
        if (!dir.exists()) return emptyList()
        return dir.listFiles()?.toList() ?: emptyList()
    }

    fun readFormContent(file: File): String {
        return try {
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun deleteForm(file: File) {
        try {
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
