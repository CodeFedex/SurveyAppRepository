package com.example.surveyapp.ui.surveyor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.surveyapp.R
import org.json.JSONObject
import java.io.File

class FillSurveyActivity : AppCompatActivity() {

    private lateinit var questionsContainer: LinearLayout
    private lateinit var btnSaveForm: Button

    data class Question(val id: String, val text: String, val type: String)
    data class Survey(val title: String, val questions: List<Question>)

    private lateinit var currentSurvey: Survey
    private val answersMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_survey)

        questionsContainer = findViewById(R.id.questionsContainer)
        btnSaveForm = findViewById(R.id.btnSaveForm)

        val jsonString = intent.getStringExtra("survey_json")
        if (jsonString == null) {
            Toast.makeText(this, "No se pudo cargar la encuesta", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        currentSurvey = parseSurveyFromJson(jsonString)
        renderSurvey(currentSurvey)

        btnSaveForm.setOnClickListener {
            saveForm()
        }
    }

    private fun parseSurveyFromJson(json: String): Survey {
        val jsonObj = JSONObject(json)
        val title = jsonObj.getString("title")
        val questionsJson = jsonObj.getJSONArray("questions")
        val questions = mutableListOf<Question>()

        for (i in 0 until questionsJson.length()) {
            val q = questionsJson.getJSONObject(i)
            questions.add(
                Question(
                    id = q.getString("id"),
                    text = q.getString("text"),
                    type = q.getString("type")
                )
            )
        }

        return Survey(title, questions)
    }

    private fun renderSurvey(survey: Survey) {
        questionsContainer.removeAllViews()

        for (question in survey.questions) {
            val textView = TextView(this).apply {
                text = question.text
                textSize = 16f
            }
            questionsContainer.addView(textView)

            when (question.type) {
                "text" -> {
                    val editText = EditText(this).apply {
                        tag = question.id
                    }
                    questionsContainer.addView(editText)
                }
                "boolean" -> {
                    val switch = SwitchCompat(this).apply {
                        tag = question.id
                    }
                    questionsContainer.addView(switch)
                }
            }
        }
    }

    private fun saveForm() {
        answersMap.clear()

        for (i in 0 until questionsContainer.childCount) {
            when (val view = questionsContainer.getChildAt(i)) {
                is EditText -> {
                    val id = view.tag as? String ?: continue
                    answersMap[id] = view.text.toString()
                }
                is SwitchCompat -> {
                    val id = view.tag as? String ?: continue
                    answersMap[id] = view.isChecked.toString()
                }
            }
        }

        val respuestas = JSONObject()
        respuestas.put("title", currentSurvey.title)
        respuestas.put("answers", JSONObject(answersMap as Map<*, *>))

        val dir = File(filesDir, "completed_forms")
        if (!dir.exists()) dir.mkdirs()

        val fileName = "respuesta_${System.currentTimeMillis()}.json"
        val file = File(dir, fileName)

        try {
            file.writeText(respuestas.toString())
            Toast.makeText(this, "Formulario guardado", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el formulario", Toast.LENGTH_SHORT).show()
        }
    }
}
