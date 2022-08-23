package com.example.my_health.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.my_health.R
import com.example.my_health.model.Reminder
import kotlinx.android.synthetic.main.reminder_item_layout.view.*
import java.util.ArrayList

class ReminderAdapter(val reminderList:ArrayList<Reminder>,val adapterOnClick:(Any)->Unit): RecyclerView.Adapter<ReminderAdapter.ReminderListViewHolder>() {

    class ReminderListViewHolder(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderListViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.reminder_item_layout,parent,false)
        return ReminderListViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReminderList(newReminderList: List<Reminder>){
        reminderList.clear()
        reminderList.addAll(newReminderList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ReminderListViewHolder, position: Int) {
        /*holder.itemView.uputnicaTitle.text=reminderList[position].title
        holder.itemView.uputnicaOpis.text=reminderList[position].description
        holder.itemView.imgDeleteReminder.setOnClickListener {
            adapterOnClick(reminderList[position])
        }
        holder.itemView.imgUpdateReminder.setOnClickListener {
            val action= ReminderListFragmentDirections.actionEditReminderFragment(reminderList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }

         */
    }

    override fun getItemCount(): Int {
        return reminderList.size
    }
}