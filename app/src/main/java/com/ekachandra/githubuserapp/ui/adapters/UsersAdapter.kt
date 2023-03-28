package com.ekachandra.githubuserapp.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekachandra.githubuserapp.data.local.entity.User
import com.ekachandra.githubuserapp.databinding.ItemUsersBinding
import com.ekachandra.githubuserapp.ui.main.DetailActivity

class UsersAdapter(private val listUsers: ArrayList<User>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (username, photo) = listUsers[position]

        holder.binding.tvItemName.text = username
        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.binding.imgItemUser)

        holder.itemView.setOnClickListener {
            val userData = listUsers[position]
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            detailIntent.putExtra(DetailActivity.USER, userData)
            holder.itemView.context.startActivity(detailIntent)
        }
    }
}
