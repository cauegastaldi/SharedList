package br.edu.ifsp.sharedlist.model

import java.util.UUID

interface TaskDao {

    fun createTask(task: Task)

    fun retrieveTask(id: String): Task?

    fun retrieveTasks(): MutableList<Task>

    fun updateTask(task: Task): Int

    fun deleteTask(task: Task): Int
}