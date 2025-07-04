package com.example.surveyapp.ui.surveyor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.surveyapp.databinding.ActivityCompletedFormsBinding
import com.example.surveyapp.data.storage.FormsStorage
import java.io.File

class CompletedFormsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedFormsBinding
    private lateinit var adapter: CompletedFormsAdapter
    private val selectedForms = mutableSetOf<String>() // Nombres de archivos seleccionados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedFormsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSendButton()
        setupDeleteButton()
    }

    private fun setupRecyclerView() {
        val allForms = FormsStorage.getAllSavedForms(this).map { it.name }
        adapter = CompletedFormsAdapter(allForms, selectedForms)
        binding.completedFormsList.layoutManager = LinearLayoutManager(this)
        binding.completedFormsList.adapter = adapter
    }

    private fun refreshFormsList() {
        val allForms = FormsStorage.getAllSavedForms(this).map { it.name }
        adapter.updateData(allForms)
    }

    private fun setupSendButton() {
        binding.btnSendToDatabase.setOnClickListener {
            if (selectedForms.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un formulario para enviar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSendToDatabase.isEnabled = false

            for (formFileName in selectedForms) {
                val file = File(filesDir, "completed_forms/$formFileName")

                val formContent = FormsStorage.readFormContent(file)

                // Simulación de envío a base de datos
                println("Enviando formulario: $formFileName")
                println(formContent)

                // Eliminar si el envío fue exitoso
                FormsStorage.deleteForm(file)
            }

            Toast.makeText(this, "Formularios enviados correctamente", Toast.LENGTH_SHORT).show()

            selectedForms.clear()
            refreshFormsList()

            binding.btnSendToDatabase.isEnabled = true
        }
    }

    private fun setupDeleteButton() {
        binding.btnDeleteSelected.setOnClickListener {
            if (selectedForms.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un formulario para eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que querés eliminar los formularios seleccionados? Esta acción no se puede deshacer.")
                .setPositiveButton("Sí") { _, _ ->
                    for (formFileName in selectedForms) {
                        val file = File(filesDir, "completed_forms/$formFileName")
                        FormsStorage.deleteForm(file)
                    }

                    Toast.makeText(this, "Formularios eliminados correctamente", Toast.LENGTH_SHORT).show()
                    selectedForms.clear()
                    refreshFormsList()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
