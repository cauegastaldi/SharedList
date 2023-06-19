package br.edu.ifsp.sharedlist.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.edu.ifsp.scl.sharedlist.TaskValidator
import br.edu.ifsp.sharedlist.R
import br.edu.ifsp.sharedlist.databinding.ActivityTaskBinding
import br.edu.ifsp.sharedlist.model.Task
import br.edu.ifsp.sharedlist.utils.DateConverter
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.*

class TaskActivity: BaseActivity() {

    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)
        supportActionBar?.subtitle = getString(R.string.task_info)

        atb.conclusionDateEt.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            DatePickerDialog(this@TaskActivity, { view, selectedYear, selectedMonth, selectedDay ->
                val date =
                    DateConverter.convertLocalDateToDateInBrazilianFormat(
                        LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    )
                atb.conclusionDateEt.setText(date)
            }, year, month, day).show()
        }

        val receivedTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }

        receivedTask?.let { _receivedTask ->
            with(atb) {
                with(_receivedTask) {
                    titleEt.setText(title)
                    descriptionEt.setText(description)
                    conclusionDateEt.setText(conclusionDate)
                    userWhoCreatedTaskEt.setText(_receivedTask.userWhoCreated)
                    userWhoCompletedTaskEt.setText(_receivedTask.userWhoCompleted)
                    creationDateEt.setText(_receivedTask.creationDate)
                }
            }
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)

            with(atb) {
                titleEt.isEnabled = false
                descriptionEt.isEnabled = !viewTask
                conclusionDateEt.isEnabled = !viewTask
                userWhoCreatedTaskEt.isEnabled = false
                creationDateEt.isEnabled = false

                userWhoCreatedTaskTv.visibility = View.VISIBLE
                userWhoCreatedTaskEt.visibility = View.VISIBLE
                creationDateTv.visibility = View.VISIBLE
                creationDateEt.visibility = View.VISIBLE

                if (_receivedTask.isCompleted) {
                    userWhoCompletedTaskTv.visibility = View.VISIBLE
                    userWhoCompletedTaskEt.visibility = View.VISIBLE
                }

                saveBt.visibility = if (viewTask) View.GONE else View.VISIBLE
            }
        }

        atb.saveBt.setOnClickListener {
            val taskList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras?.getParcelableArrayList(BUNDLE_LIST_TASK, Task::class.java)
            } else {
                intent.extras?.getParcelableArrayList<Task>(BUNDLE_LIST_TASK)
            }
            val formErrors = TaskValidator.getFormErrors(atb, taskList)

            if (formErrors.isEmpty()) {
                with (atb) {
                    val id = receivedTask?.id ?: generateId()
                    val title = titleEt.text.toString().trim()
                    val description = descriptionEt.text.toString().trim()
                    val conclusionDate = conclusionDateEt.text.toString()

                    val creationDate =
                        receivedTask?.creationDate ?: DateConverter.convertLocalDateToDateInBrazilianFormat(LocalDate.now())
                    val user = receivedTask?.userWhoCreated ?: FirebaseAuth.getInstance().currentUser!!.email!!
                    val task = Task(
                        id = id,
                        title = title,
                        description = description,
                        conclusionDate = conclusionDate,
                        userWhoCreated = user,
                        creationDate = creationDate
                    )

                    val resultIntent = Intent()
                    resultIntent.putExtra(EXTRA_TASK, task)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            } else {
                Toast.makeText(this, formErrors, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}