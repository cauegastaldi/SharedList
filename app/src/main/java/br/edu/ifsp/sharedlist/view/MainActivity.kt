package br.edu.ifsp.sharedlist.view

import android.content.Intent
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.sharedlist.R
import br.edu.ifsp.sharedlist.adapter.OnTaskClickListener
import br.edu.ifsp.sharedlist.adapter.TaskRvAdapter
import br.edu.ifsp.sharedlist.controller.TaskController
import br.edu.ifsp.sharedlist.databinding.ActivityMainBinding
import br.edu.ifsp.sharedlist.model.Task
import br.edu.ifsp.sharedlist.utils.DateConverter
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(this, taskList, this)
    }

    private lateinit var tarl: ActivityResultLauncher<Intent>

    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.task_list)

        taskController.getTasks()
        amb.tasksRv.layoutManager = LinearLayoutManager(this)
        amb.tasksRv.adapter = taskAdapter

        updateViewsHandler = Handler(Looper.myLooper()!!) { msg ->
            taskController.getTasks()
            true
        }

        tarl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra<Task>(EXTRA_TASK)
                }
                task?.let{_task ->
                    val position = taskList.indexOfFirst { it.id == _task.id }
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        taskAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Tarefa editada!", Toast.LENGTH_SHORT).show()
                    } else {
                        taskController.insertTask(_task)
                        Toast.makeText(this, "Tarefa adicionada!", Toast.LENGTH_SHORT).show()
                        updateViewsHandler.sendMessageDelayed(Message(), 3000)
                    }
                }
            }
        }

        updateViewsHandler.sendMessageDelayed(Message(), 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTaskMi -> {
                val bundle = Bundle()
                bundle.putParcelableArrayList(BUNDLE_LIST_TASK, ArrayList<Task>(taskList))
                val taskIntent = Intent(this, TaskActivity::class.java)
                taskIntent.putExtras(bundle)
                tarl.launch(taskIntent)
                true
            }

            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish() // Sai da main activity e volta para a ativdade que a chamou (no caso a login activity)
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        taskIntent.putExtra(EXTRA_VIEW_TASK, true)
        startActivity(taskIntent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        val taskIntent = Intent(this@MainActivity, TaskActivity::class.java)
        taskIntent.putExtra(EXTRA_TASK, task)
        tarl.launch(taskIntent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa removida!", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleteMenuItemClick(position: Int) {
        val task = taskList[position]
        task.isCompleted = true
        task.userWhoCompleted = FirebaseAuth.getInstance().currentUser!!.email
        task.conclusionDate = DateConverter.convertLocalDateToDateInBrazilianFormat(LocalDate.now())
        taskController.editTask(task)
        taskList[position] = task
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Tarefa completada!", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val email = FirebaseAuth.getInstance().currentUser?.email
            Toast.makeText(this, "Bem-vindo, ${email}", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Não há usuário autenticado!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}