package com.example.streamifymvp.Presentation.Accueil.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.R

class NouveauteAdapter(private var chansons: List<Chanson>) :
    RecyclerView.Adapter<NouveauteAdapter.NouveauteViewHolder>() {

    class NouveauteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titreView: TextView = itemView.findViewById(R.id.textViewTitreChanson)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewChanson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NouveauteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nouveaute_accueil, parent, false)
        return NouveauteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NouveauteViewHolder, position: Int) {
        val chanson = chansons[position]
        holder.titreView.text = chanson.nom
        Glide.with(holder.itemView.context)
            .load(chanson.imageChanson)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = chansons.size

    fun updateChansons(nouvellesChansons: List<Chanson>) {
        chansons = nouvellesChansons
        notifyDataSetChanged()
    }
}
