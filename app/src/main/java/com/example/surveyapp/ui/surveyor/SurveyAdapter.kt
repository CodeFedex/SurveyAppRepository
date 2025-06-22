package com.example.surveyapp.ui.surveyor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.R
import org.json.JSONObject

class SurveyAdapter(
    private val surveyJsonList: List<String>,
    private val onSurveyClick: (String) -> Unit
) : RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder>() {

    inner class SurveyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSurveyTitle: TextView = itemView.findViewById(R.id.tvSurveyTitle)

        fun bind(json: String) {
            val title = JSONObject(json).getString("title")
            tvSurveyTitle.text = title

            itemView.setOnClickListener {
                onSurveyClick(json)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey, parent, false)
        return SurveyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.bind(surveyJsonList[position])
    }

    override fun getItemCount(): Int = surveyJsonList.size
}
