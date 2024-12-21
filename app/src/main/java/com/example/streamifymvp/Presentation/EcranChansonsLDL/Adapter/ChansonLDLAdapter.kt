package com.example.streamifymvp.Presentation.EcranChansonsLDL.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Presentation.EcranChansonsLDL.EcranChansonsLDL
import com.example.streamifymvp.Presentation.EcranChansonsLDL.EcranChansonsLDLPresentateur
import com.example.streamifymvp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChansonLDLAdapter(
    private var chansons: List<Chanson>,
    private val présentateur: EcranChansonsLDLPresentateur,
    private val parentFragment: EcranChansonsLDL,
    private val onChansonClick: (Chanson, List<Chanson>) -> Unit
) : RecyclerView.Adapter<ChansonLDLAdapter.ChansonViewHolder>() {

    inner class ChansonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titreTextView: TextView = itemView.findViewById(R.id.textViewTitre)
        val artisteTextView: TextView = itemView.findViewById(R.id.textViewArtiste)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewChanson)

        fun bind(chanson: Chanson) {
            titreTextView.text = chanson.nom


            parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                val artiste = chanson.artiste
                withContext(Dispatchers.Main) {
                    artisteTextView.text = artiste?.pseudoArtiste
                }
            }


            Glide.with(itemView.context)
                .load(chanson.imageChanson)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            itemView.setOnClickListener {
                onChansonClick(chanson, chansons)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChansonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chanson, parent, false)
        return ChansonViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChansonViewHolder, position: Int) {
        holder.bind(chansons[position])
    }

    override fun getItemCount(): Int = chansons.size

    fun updateChansons(newChansons: List<Chanson>) {
        this.chansons = newChansons
        notifyDataSetChanged()
    }
}
