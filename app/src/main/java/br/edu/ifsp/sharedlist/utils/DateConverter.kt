package br.edu.ifsp.sharedlist.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateConverter {

    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        fun convertDateInBrazilianFormatToLocalDate(date: String): LocalDate {
            val formattedDate = date.format(formatter)
            return LocalDate.parse(formattedDate, formatter)
        }

        fun convertLocalDateToDateInBrazilianFormat(date: LocalDate): String {
            return date.format(formatter)
        }
    }
}