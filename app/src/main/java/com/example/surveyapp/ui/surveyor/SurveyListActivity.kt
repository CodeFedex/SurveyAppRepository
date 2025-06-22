package com.example.surveyapp.ui.surveyor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import org.json.JSONObject

class SurveyListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SurveyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey_list)

        recyclerView = findViewById(R.id.rvSurveys)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dummySurveys = listOf(
            """
            {
                "title": "Encuesta de Satisfacción",
                "questions": [
                    {"id": "q1", "text": "¿Cómo calificarías el servicio?", "type": "text"},
                    {"id": "q2", "text": "¿Volverías a usar el servicio?", "type": "boolean"}
                ]
            }
            """.trimIndent(),
            """
            {
                "title": "Relevamiento barrial",
                "questions": [
                    {"id": "q1", "text": "¿Hay problemas de iluminación?", "type": "boolean"},
                    {"id": "q2", "text": "¿Cómo describirías el estado de las calles?", "type": "text"}
                ]
            }
            """.trimIndent()
        )

        adapter = SurveyAdapter(dummySurveys) { selectedJson ->
            val intent = Intent(this, FillSurveyActivity::class.java)
            intent.putExtra("survey_json", selectedJson)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}
