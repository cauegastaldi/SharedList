package br.edu.ifsp.sharedlist.adapter

import android.content.Context
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.sharedlist.R
import br.edu.ifsp.sharedlist.databinding.TileTaskBinding
import br.edu.ifsp.sharedlist.model.Task

class TaskRvAdapter(
    private val context: Context,
    private val taskList: MutableList<Task>, // data source
    private val onTaskClickListener: OnTaskClickListener // objeto que implementa as ações de clique
): RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(tileTaskBinding: TileTaskBinding) :
        RecyclerView.ViewHolder(tileTaskBinding.root), OnCreateContextMenuListener {
        val titleTv: TextView = tileTaskBinding.titleTv
        val descriptionTv: TextView = tileTaskBinding.descriptionTv
        val conclusionDateTv: TextView = tileTaskBinding.conclusionDateTv
        val completedTv: TextView = tileTaskBinding.completedTv
        var taskPosition = -1
        init {
            tileTaskBinding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {

            menu?.add(Menu.NONE, 0, 0, "Editar")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onEditMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Remover")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onRemoveMenuItemClick(taskPosition)
                }
                true
            }
            menu?.add(Menu.NONE, 1, 1, "Completar")?.setOnMenuItemClickListener {
                if (taskPosition != -1) {
                    onTaskClickListener.onCompleteMenuItemClick(taskPosition)
                }
                true
            }
        }


    }


    // Chamada pela LayoutManager para buscar a quantidade de dados e preparar a quantidade de células.
    override fun getItemCount(): Int = taskList.size

    // Chamada pelo LayoutManager para criar uma nova célula (e consequentemente um novo ViewHolder).
    // Essa função só será chamada se a célula precisar ser criada mesmo.
    // se a célula existir (for reciclada) ela não será chamada.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Criar uma nova célula
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))

        // Cria um ViewHolder usando a célula
        val taskViewHolder = TaskViewHolder(tileTaskBinding)

        // Retorna o ViewHolder
        return taskViewHolder
    }

    // Chamada pelo LayoutManager para alterar os valores de uma célula
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Busca o contato pela posição no data source
        val task = taskList[position]
        val taskCompletedText = if (task.isCompleted) {"Sim"} else {"Não"}

        // Altera os valores da célula
        holder.titleTv.text = task.title
        holder.descriptionTv.text = context.getString(R.string.description_tile_task, task.description)
        holder.conclusionDateTv.text = context.getString(R.string.expected_conclusion_date_tile_task, task.conclusionDate.toString())
        holder.completedTv.text = context.getString(R.string.completed, taskCompletedText)
        holder.taskPosition = position

        holder.itemView.setOnClickListener {
            onTaskClickListener.onTileTaskClick(position)
        }

    }


}