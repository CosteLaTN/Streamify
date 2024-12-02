package com.example.streamifymvp.Presentation.Accueil.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streamifymvp.Domaine.entitees.Artiste
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.R

class ChansonAdapter(
    private var chansons: List<Chanson>,
    private val artistes: List<Artiste>,
    private val onChansonClick: (Chanson) -> Unit
) : RecyclerView.Adapter<ChansonAdapter.ChansonViewHolder>() {

    class ChansonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titreView: TextView = itemView.findViewById(R.id.textViewTitre)
        val artisteText: TextView = itemView.findViewById(R.id.textViewArtiste)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewChanson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChansonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chanson, parent, false)
        return ChansonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChansonViewHolder, position: Int) {
        val chanson = chansons[position]
        val artiste = artistes.find { it.id == chanson.artisteId }
        if (artiste == null) {
            Log.d("ChansonAdapter", "Artiste non trouvé pour chanson: ${chanson.nom} avec artisteId: ${chanson.artisteId}")
        } else {
            Log.d("ChansonAdapter", "Artiste trouvé: ${artiste.pseudoArtiste} pour chanson: ${chanson.nom}")
        }

        holder.titreView.text = chanson.nom
        holder.artisteText.text = artiste?.pseudoArtiste ?: "Artiste inconnu"

        // Utilisation de Glide pour charger l'image depuis l'API
        Glide.with(holder.itemView.context)
            .load(chanson.imageChanson)
            //.placeholder(R.drawable.placeholder_image) // Image par défaut
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            onChansonClick(chanson)
        }
    }

    override fun getItemCount(): Int = chansons.size

    fun updateChansons(nouvellesChansons: List<Chanson>) {
        chansons = nouvellesChansons
        notifyDataSetChanged()
    }
}
