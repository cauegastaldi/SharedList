package br.edu.ifsp.sharedlist.adapter

interface OnTaskClickListener {
    fun onTileTaskClick(position: Int)

    fun onEditMenuItemClick(position: Int)

    fun onRemoveMenuItemClick(position: Int)

    fun onCompleteMenuItemClick(position: Int)
}