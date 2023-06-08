package br.edu.ifsp.sharedlist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.Date
import java.util.UUID

@Parcelize
data class Task(
    val id: String = "",
    val title: String = "",
    var description: String = "",
    var conclusionDate: String = "",
    val userWhoCreated: String = "",
    val creationDate: String = "",
    var userWhoCompleted: String? = null,
    var isCompleted: Boolean = false
): Parcelable
