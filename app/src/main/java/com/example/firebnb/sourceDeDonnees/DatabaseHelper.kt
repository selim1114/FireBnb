package com.example.firebnb.sourceDeDonnees

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.firebnb.domaine.entité.Propriete

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Gestion.db"
        private const val TABLE_NAME = "ProprieteFavoris"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITRE = "titre"
        private const val COLUMN_ADRESSE = "adresse"
        private const val COLUMN_PRIX = "prix"
        private const val COLUMN_PHOTO = "photo"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY,
            $COLUMN_TITRE TEXT,
            $COLUMN_ADRESSE TEXT,
            $COLUMN_PRIX DOUBLE,
            $COLUMN_PHOTO TEXT
        )
    """
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            val removeDuplicatesQuery = """
            DELETE FROM $TABLE_NAME
            WHERE rowid NOT IN (
                SELECT MIN(rowid)
                FROM $TABLE_NAME
                GROUP BY $COLUMN_ID
            )
        """
            db?.execSQL(removeDuplicatesQuery)
            val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
            db?.execSQL(DROP_TABLE)
            onCreate(db)
        }
    }
    fun ajouterFavoris(propriete: Propriete) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, propriete.id)
            put(COLUMN_TITRE, propriete.titre)
            put(COLUMN_ADRESSE, propriete.adresse)
            put(COLUMN_PRIX, propriete.prix)
            put(COLUMN_PHOTO, propriete.photo)
        }
        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()
    }
    @SuppressLint("SuspiciousIndentation")
    fun supprimerFavoris(id: Int) {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
    }
    fun obtenirsFavoris(): List<Propriete> {
        val proprieteList = mutableListOf<Propriete>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val titre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITRE))
            val adresse = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADRESSE))
            val prix = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRIX))
            val photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO))

            val propriete = Propriete(id, titre, "", adresse, 0.0, 0.0, 0, 0, 0, prix, photo, ArrayList(), ArrayList(), false)
            proprieteList.add(propriete)
        }
        cursor.close()
        db.close()
        return proprieteList
    }

    fun supprimerLesDoublons() {
        val db = writableDatabase
        val removeDuplicatesQuery = """
        DELETE FROM $TABLE_NAME
        WHERE rowid NOT IN (
            SELECT MIN(rowid)
            FROM $TABLE_NAME
            GROUP BY $COLUMN_ID
        )
    """
        db.execSQL(removeDuplicatesQuery)
        db.close()
    }
}