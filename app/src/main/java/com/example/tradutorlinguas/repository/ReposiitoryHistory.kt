package com.example.tradutorlinguas.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.tradutorlinguas.constants.ConstantsHistory
import com.example.tradutorlinguas.dbhistory.DataBaseHistory
import com.example.tradutorlinguas.dataclass.LanguageData

class RepositoryHistory(dataBaseHistory: DataBaseHistory) {

    private val dbWrite = dataBaseHistory.writableDatabase
    private val dbRead = dataBaseHistory.readableDatabase
    private val tableName = ConstantsHistory.HISTORY.TABLE_NAME

    fun saveEmployee(history: LanguageData): Boolean {

        return try {
            val insertValues = ContentValues()
            insertValues.run {
                put(ConstantsHistory.HISTORY.COLUMNS.LANGFROM, history.from)
                put(ConstantsHistory.HISTORY.COLUMNS.LANGTO, history.to)
                put(ConstantsHistory.HISTORY.COLUMNS.TEXTFROM, history.textFrom)
                put(ConstantsHistory.HISTORY.COLUMNS.TEXTTO, history.textTo)
                put(ConstantsHistory.HISTORY.COLUMNS.HOUR, history.hour)
                put(ConstantsHistory.HISTORY.COLUMNS.DATE, history.date)
            }

            dbWrite.insert(tableName, null, insertValues)
            true

        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("Range")
    fun historyList(): ArrayList<LanguageData> {

        val list: ArrayList<LanguageData> = ArrayList()
        try {
            val cursor: Cursor
            val projection = arrayOf(
                    ConstantsHistory.HISTORY.COLUMNS.ID,
                    ConstantsHistory.HISTORY.COLUMNS.LANGFROM,
                    ConstantsHistory.HISTORY.COLUMNS.LANGTO,
                    ConstantsHistory.HISTORY.COLUMNS.TEXTFROM,
                    ConstantsHistory.HISTORY.COLUMNS.TEXTTO,
                    ConstantsHistory.HISTORY.COLUMNS.HOUR,
                    ConstantsHistory.HISTORY.COLUMNS.DATE)

            val orderBy = ConstantsHistory.HISTORY.COLUMNS.ID

            cursor = dbRead.query (tableName, projection, null, null, null, null, orderBy)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val id = cursor.getInt(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.ID))
                    val langFrom = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LANGFROM))
                    val langTo = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LANGTO))
                    val textFrom = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.TEXTFROM))
                    val textTo = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.TEXTTO))
                    val hour = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.HOUR))
                    val date = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.DATE))

                    list.add(LanguageData(id, langFrom, langTo, textFrom, textTo, hour, date))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    @SuppressLint("Range")
    fun historyById(id: String): LanguageData? {

        var list: LanguageData? = null
        try {
            val cursor: Cursor
            val projection = arrayOf(
                    ConstantsHistory.HISTORY.COLUMNS.ID,
                    ConstantsHistory.HISTORY.COLUMNS.LANGFROM,
                    ConstantsHistory.HISTORY.COLUMNS.LANGTO,
                    ConstantsHistory.HISTORY.COLUMNS.TEXTFROM,
                    ConstantsHistory.HISTORY.COLUMNS.TEXTTO,
                    ConstantsHistory.HISTORY.COLUMNS.HOUR,
                    ConstantsHistory.HISTORY.COLUMNS.DATE)

            val selection = ConstantsHistory.HISTORY.COLUMNS.ID + " = ?"
            val args = arrayOf(id)

            cursor = dbRead.query (tableName, projection, selection, args,
                    null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val idInt = cursor.getInt(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.ID))
                    val langFrom = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LANGFROM))
                    val langTo = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LANGTO))
                    val textFrom = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.TEXTFROM))
                    val textTo = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.TEXTTO))
                    val hour = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.HOUR))
                    val date = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.DATE))

                    list = LanguageData(idInt, langFrom, langTo, textFrom, textTo, hour, date)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun removeHistory(id: Int) {

        try {
            val selection = ConstantsHistory.HISTORY.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            dbWrite.delete(tableName, selection, args)

        } catch (e: Exception) {  }
    }

    fun removeAll(id: Int) {

        try {
            val selection = ConstantsHistory.HISTORY.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            dbWrite.delete(tableName, selection, args)

        } catch (e: Exception) {  }
    }
}

