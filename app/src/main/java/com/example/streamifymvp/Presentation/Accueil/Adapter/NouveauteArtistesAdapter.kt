package com.example.streamifymvp.Presentation.Accueil.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streamifymvp.Domaine.Modeles.Artiste
import com.example.streamifymvp.R

class NouveauxArtistesAdapter(private var artistes: List<Artiste>) :
    RecyclerView.Adapter<NouveauxArtistesAdapter.ArtisteViewHolder>() {

    class ArtisteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pseudoArtisteView: TextView = itemView.findViewById(R.id.textViewArtistName)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtisteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artiste, parent, false)
        return ArtisteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtisteViewHolder, position: Int) {
        val artiste = artistes[position]
        holder.pseudoArtisteView.text = artiste.pseudoArtiste
        holder.imageView.setImageResource(artiste.imageArtiste)
    }

    override fun getItemCount(): Int = artistes.size

    fun updateArtistes(nouveauxArtistes: List<Artiste>) {
        artistes = nouveauxArtistes
        notifyDataSetChanged()
    }
}

