package com.example.streamifymvp.SourceDeDonnees

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HistoriqueDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "historique.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_HISTORIQUE = "historique"
        const val COLUMN_ID = "_id"
        const val COLUMN_RECHERCHE = "recherche"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_HISTORIQUE ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_RECHERCHE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORIQUE")
        onCreate(db)
    }
}