package br.edu.ifsp.scl.sharedlist


import br.edu.ifsp.sharedlist.databinding.ActivityTaskBinding
import br.edu.ifsp.sharedlist.model.Task
import br.edu.ifsp.sharedlist.utils.DateConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TaskValidator {
    companion object {
        private const val EMPTY_TITLE_FORM_ERROR = "Título deve ser preenchido"
        private const val TITLE_ALREADY_EXISTS_ERROR = "Título escolhido já existe"
        private const val EMPTY_CONCLUSION_DATE_FORM_ERROR = "Data de conclusão esperada deve ser preenchida"
        private const val EMPTY_DESCRIPTION_FORM_ERROR = "Descrição deve ser preenchida"
        private const val INVALID_CONCLUSION_DATE_FORM_ERROR = "Data de conclusão esperada não pode vir antes da data atual"

        fun getFormErrors(atb: ActivityTaskBinding, taskList: ArrayList<Task>?): String {
            val formErrors = mutableListOf<String>()

            with (atb) {

                val title = titleEt.text.toString()
                val description = descriptionEt.text.toString()
                val date = conclusionDateEt.text.toString()

                if (title.isEmpty()) {
                    formErrors.add(EMPTY_TITLE_FORM_ERROR)
                }
                else if (!taskList.isNullOrEmpty() && !taskTitleIsUnique(taskList, title)) {
                    formErrors.add(TITLE_ALREADY_EXISTS_ERROR)
                }

                if (description.isEmpty()) {
                    formErrors.add(EMPTY_DESCRIPTION_FORM_ERROR)
                }

                if (date.isEmpty()) {
                    formErrors.add(EMPTY_CONCLUSION_DATE_FORM_ERROR)
                }
                else if (isDateBehindNowDate(date)) {
                    formErrors.add(INVALID_CONCLUSION_DATE_FORM_ERROR)
                }

                return formErrors.joinToString(", ")
            }
        }

        private fun isDateBehindNowDate(date: String): Boolean {
            val now = LocalDate.now()
            val formDate = DateConverter.convertDateInBrazilianFormatToLocalDate(date)

            return formDate.isBefore(now)
        }
        private fun taskTitleIsUnique(taskList: ArrayList<Task>, title: String): Boolean {
            return taskList.firstOrNull { task -> task.title.lowercase() == title.trim().lowercase() } == null
        }
    }
}