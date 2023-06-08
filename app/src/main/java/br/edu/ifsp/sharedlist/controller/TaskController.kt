package br.edu.ifsp.sharedlist.controller

import br.edu.ifsp.sharedlist.model.Task
import br.edu.ifsp.sharedlist.model.TaskDao
import br.edu.ifsp.sharedlist.model.TaskDaoRtDbFb
import br.edu.ifsp.sharedlist.view.MainActivity
import java.util.UUID

class TaskController(private val mainActivity: MainActivity) {

    private val taskDaoImpl: TaskDao = TaskDaoRtDbFb()

    fun insertTask(task: Task) {
        Thread {
            taskDaoImpl.createTask(task)
        }.start()
    }

    fun getTasks() {
        Thread {
            val list = taskDaoImpl.retrieveTasks()
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(list)
            }
        }.start()
    }
    fun editTask(task: Task) {
        Thread {
            taskDaoImpl.updateTask(task)
        }.start()
    }
    fun removeTask(task: Task) {
        Thread {
           taskDaoImpl.deleteTask(task)
        }.start()
    }
}