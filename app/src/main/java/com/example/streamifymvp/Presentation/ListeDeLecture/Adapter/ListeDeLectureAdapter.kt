package com.example.streamifymvp.Presentation.ListeDeLecture.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture
import com.example.streamifymvp.R

class ListeDeLectureAdapter(
    private var listesDeLecture: List<ListeDeLecture>,
    private val onPlaylistClick: (ListeDeLecture) -> Unit
) : RecyclerView.Adapter<ListeDeLectureAdapter.ListeDeLectureViewHolder>() {

    inner class ListeDeLectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titreTextView: TextView = itemView.findViewById(R.id.textViewTitre)

        fun bind(listeDeLecture: ListeDeLecture) {
            titreTextView.text = listeDeLecture.nom
            itemView.setOnClickListener {
                onPlaylistClick(listeDeLecture)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeDeLectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return ListeDeLectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListeDeLectureViewHolder, position: Int) {
        holder.bind(listesDeLecture[position])
    }

    override fun getItemCount(): Int = listesDeLecture.size

    fun updateData(newListesDeLecture: List<ListeDeLecture>) {
        listesDeLecture = newListesDeLecture
        notifyDataSetChanged()
    }
}
