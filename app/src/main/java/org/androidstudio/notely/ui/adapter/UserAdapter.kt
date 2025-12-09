package org.androidstudio.notely.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat
import org.androidstudio.notely.R
import org.androidstudio.notely.data.entity.UserEntity

class UserAdapter(
    private val onSelect: (UserEntity) -> Unit,
    private val onDelete: (UserEntity) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val users = mutableListOf<UserEntity>()
    private var activeUserId: Int? = null

    fun submitList(list: List<UserEntity>, activeId: Int?) {
        users.clear()
        users.addAll(list)
        activeUserId = activeId
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.userName)
        val skill: TextView = view.findViewById(R.id.skillText)
        val delete: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        holder.name.text = "${user.emoji} ${user.name}"
        holder.skill.text = user.abilityScore?.let {
            "Skill %.1f/7.5".format(it)
        } ?: ""

        holder.itemView.setOnClickListener { onSelect(user) }
        holder.delete.setOnClickListener { onDelete(user) }
    }

    override fun getItemCount() = users.size
}
