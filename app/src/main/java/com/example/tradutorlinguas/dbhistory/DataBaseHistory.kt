package com.example.tradutorlinguas.dbhistory

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tradutorlinguas.constants.ConstantsHistory

class DataBaseHistory(context: Context?) : SQLiteOpenHelper(context, DATA_NAME, null, DATA_VERSION) {

    companion object {
        private const val DATA_NAME: String = "history.db"
        private const val DATA_VERSION: Int = 1
    }

    private val createTableHistory = """ CREATE TABLE 
            ${ConstantsHistory.HISTORY.TABLE_NAME} (
            ${ConstantsHistory.HISTORY.COLUMNS.ID} integer primary key autoincrement ,
            ${ConstantsHistory.HISTORY.COLUMNS.LANGFROM} text ,
            ${ConstantsHistory.HISTORY.COLUMNS.LANGTO} text ,
            ${ConstantsHistory.HISTORY.COLUMNS.TEXTFROM} text ,
            ${ConstantsHistory.HISTORY.COLUMNS.TEXTTO} text 
    );"""

    private val removeTableHistory = "drop table if exists ${ConstantsHistory.HISTORY.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createTableHistory)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(removeTableHistory)
        db.execSQL(createTableHistory)
    }
}