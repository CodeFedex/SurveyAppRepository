package com.example.surveyapp.ui.surveyor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.surveyapp.databinding.ItemCompletedFormBinding

class CompletedFormsAdapter(
    private val forms: List<String>,
    private val selectedForms: MutableSet<String>
) : RecyclerView.Adapter<CompletedFormsAdapter.FormViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val binding = ItemCompletedFormBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        val formName = forms[position]
        holder.bind(formName)
    }

    override fun getItemCount(): Int = forms.size

    inner class FormViewHolder(private val binding: ItemCompletedFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(formName: String) {
            binding.tvFormName.text = formName
            binding.cbSelectForm.isChecked = selectedForms.contains(formName)

            binding.cbSelectForm.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedForms.add(formName)
                } else {
                    selectedForms.remove(formName)
                }
            }
        }
    }
}
