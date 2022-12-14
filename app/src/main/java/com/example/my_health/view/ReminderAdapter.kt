package com.example.my_health.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.my_health.R
import com.example.my_health.databinding.ReminderItemLayoutBinding
import com.example.my_health.model.Reminder
import com.example.my_health.util.ReminderDeleteClickListener
import com.example.my_health.util.ReminderEditClickListener
//import kotlinx.android.synthetic.main.reminder_item_layout.view.*
import java.util.ArrayList

class ReminderAdapter(val reminderList:ArrayList<Reminder>,val adapterOnClick:(Any)->Unit): RecyclerView.Adapter<ReminderAdapter.ReminderListViewHolder>(),
ReminderDeleteClickListener,ReminderEditClickListener{

    class ReminderListViewHolder(var view:ReminderItemLayoutBinding):RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderListViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        //val view=inflater.inflate(R.layout.reminder_item_layout,parent,false)
        val view= DataBindingUtil.inflate<ReminderItemLayoutBinding>(inflater,
            R.layout.reminder_item_layout,parent,false)
        return ReminderListViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReminderList(newReminderList: List<Reminder>){
        reminderList.clear()
        reminderList.addAll(newReminderList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ReminderListViewHolder, position: Int) {
        holder.view.reminder=reminderList[position]
        holder.view.editlistener=this
        holder.view.deletelistener=this

    }

    override fun getItemCount(): Int {
        return reminderList.size
    }

    override fun onReminderEditClick(v: View) {
        val action= ReminderListFragmentDirections.actionEditReminderFragment(v.tag.toString().toInt())
        Navigation.findNavController(v).navigate(action)
    }

    override fun onReminderDeleteClick(reminder: Reminder) {
        adapterOnClick(reminder)
    }
}