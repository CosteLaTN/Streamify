package com.example.streamifymvp.Domaine.Service

import android.content.ContentValues
import android.content.Context
import com.example.streamifymvp.SourceDeDonnees.HistoriqueDatabaseHelper

class HistoriqueService(context: Context) {

    private val dbHelper = HistoriqueDatabaseHelper(context)

    fun ajouterRecherche(recherche: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(HistoriqueDatabaseHelper.COLUMN_RECHERCHE, recherche)
        }
        db.insert(HistoriqueDatabaseHelper.TABLE_HISTORIQUE, null, values)
        db.close()
    }

    fun obtenirHistorique(): List<String> {
        val historique = mutableListOf<String>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            HistoriqueDatabaseHelper.TABLE_HISTORIQUE,
            arrayOf(HistoriqueDatabaseHelper.COLUMN_RECHERCHE),
            null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val recherche = getString(getColumnIndexOrThrow(HistoriqueDatabaseHelper.COLUMN_RECHERCHE))
                historique.add(recherche)
            }
        }
        cursor.close()
        db.close()
        return historique
    }
}