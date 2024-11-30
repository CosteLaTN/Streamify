package com.example.streamifymvp.Presentation.showDates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.ShowDate
import com.example.streamifymvp.R

class ShowDatesAdapter(
    private var showDates: List<ShowDate>,
    private val onDateClick: (ShowDate) -> Unit
) : RecyclerView.Adapter<ShowDatesAdapter.ShowDateViewHolder>() {

    inner class ShowDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showDateTitle: TextView = itemView.findViewById(R.id.showDateTitle)
        val showDateDetails: TextView = itemView.findViewById(R.id.showDateDetails)

        fun bind(showDate: ShowDate) {
            showDateTitle.text = showDate.title
            showDateDetails.text = showDate.details
            itemView.setOnClickListener {
                onDateClick(showDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowDateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_date, parent, false)
        return ShowDateViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowDateViewHolder, position: Int) {
        holder.bind(showDates[position])
    }

    override fun getItemCount(): Int = showDates.size

    fun updateDates(newDates: List<ShowDate>) {
        this.showDates = newDates
        notifyDataSetChanged()
    }
}
