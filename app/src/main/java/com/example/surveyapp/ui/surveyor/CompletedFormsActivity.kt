package com.example.surveyapp.ui.surveyor

import android.os.Bundle
import android.widget.Toast
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
    }

    private fun setupRecyclerView() {
        // Convertimos la lista de archivos en lista de nombres de archivos
        val allForms = FormsStorage.getAllSavedForms(this).map { it.name }

        adapter = CompletedFormsAdapter(allForms, selectedForms)
        binding.completedFormsList.layoutManager = LinearLayoutManager(this)
        binding.completedFormsList.adapter = adapter
    }

    private fun setupSendButton() {
        binding.btnSendToDatabase.setOnClickListener {
            if (selectedForms.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un formulario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            for (formFileName in selectedForms) {
                val file = File(filesDir.absolutePath + "/completed_forms/$formFileName")

                val formContent = FormsStorage.readFormContent(file)

                // Simulación de envío a una base de datos
                println("Enviando formulario: $formFileName")
                println(formContent)

                // Si el envío es exitoso, lo eliminamos
                FormsStorage.deleteForm(file)
            }

            Toast.makeText(this, "Formularios enviados correctamente", Toast.LENGTH_SHORT).show()

            selectedForms.clear()
            setupRecyclerView()
        }
    }
}
