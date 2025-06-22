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

    // Modelos básicos
    data class Question(val id: String, val text: String, val type: String)
    data class Survey(val title: String, val questions: List<Question>)

    private lateinit var currentSurvey: Survey
    private val answersMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_survey)

        questionsContainer = findViewById(R.id.questionsContainer)
        btnSaveForm = findViewById(R.id.btnSaveForm)

        // Encuesta de prueba (reemplazá luego por encuesta real recibida por Intent)
        currentSurvey = Survey(
            title = "Encuesta de Satisfacción",
            questions = listOf(
                Question("q1", "¿Cómo calificarías el servicio?", "text"),
                Question("q2", "¿Volverías a usar el servicio?", "boolean")
            )
        )

        renderSurvey(currentSurvey)

        btnSaveForm.setOnClickListener {
            saveForm()
        }
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
                // Podés agregar más tipos aquí, como select, radio, etc.
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

        val respuestasJson = JSONObject(answersMap as Map<*, *>).toString()
        val fileName = "respuesta_${System.currentTimeMillis()}.json"
        val file = File(filesDir, fileName)
        file.writeText(respuestasJson)

        Toast.makeText(this, "Formulario guardado como $fileName", Toast.LENGTH_SHORT).show()

        // Recargar encuesta vacía
        renderSurvey(currentSurvey)
    }
}
