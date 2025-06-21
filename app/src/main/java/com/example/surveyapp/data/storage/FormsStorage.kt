package com.example.surveyapp.data.storage
import android.content.Context
import java.io.File


    object FormsStorage {

    private const val FORMS_FOLDER_NAME = "completed_forms"

    fun saveFormJson(context: Context, fileName: String, jsonContent: String) {
        val dir = File(context.filesDir, FORMS_FOLDER_NAME)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, "$fileName.json")
        file.writeText(jsonContent)
    }

    fun getAllSavedForms(context: Context): List<File> {
        val dir = File(context.filesDir, FORMS_FOLDER_NAME)
        if (!dir.exists()) return emptyList()
        return dir.listFiles()?.toList() ?: emptyList()
    }

    fun readFormContent(file: File): String {
        return file.readText()
    }

    fun deleteForm(file: File) {
        file.delete()
    }
}
