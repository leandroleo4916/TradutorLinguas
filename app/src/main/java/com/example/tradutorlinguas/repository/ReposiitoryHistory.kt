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

    /*

    private fun editEmployee(employee: EmployeeEntity): String {

        return try {
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(employee.id.toString())

            val contentValues = ContentValues()
            contentValues.run {
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, employee.id)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS, employee.status)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, employee.photo)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1, employee.horario1)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2, employee.horario2)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3, employee.horario3)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4, employee.horario4)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, employee.nameEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, employee.cargoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, employee.emailEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, employee.phoneEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, employee.admissaoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, employee.aniversarioEmployee)
            }

            dbWrite.update(tableName, contentValues, projection, args)
            "editado"

        } catch (e: Exception) {
            "não editado"
        }
    }

    fun consultFullEmployee(): ArrayList<EmployeeEntityFull> {

        val listData: ArrayList<EmployeeEntityFull> = arrayListOf()
        try {
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.RG,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CPF,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CTPS,
                ConstantsEmployee.EMPLOYEE.COLUMNS.SALARIO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ESTADOCIVIL)

            val cursor = dbRead.query (tableName, projection,
                null, null, null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val status = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val hora1 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                    val hora2 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                    val hora3 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                    val hora4 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))
                    val work = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                    val email = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                    val cargo = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                    val phone = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                    val admissao = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                    val niver = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))
                    val rg = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.RG))
                    val cpf = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CPF))
                    val ctps = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CTPS))
                    val salario = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.SALARIO))
                    val estadocivil = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ESTADOCIVIL))

                    listData.add(EmployeeEntityFull(id, status, photo, hora1, hora2, hora3,
                        hora4, work, name, email, cargo, phone, admissao, niver, rg, cpf,
                        ctps, salario, estadocivil)
                    )
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultEmployeeNameAndPhoto(): ArrayList<EmployeeNameAndPhoto> {

        val listData: ArrayList<EmployeeNameAndPhoto> = arrayListOf()
        try {
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME)

            val cursor = dbRead.query (tableName, projection,
                null, null, null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))

                    listData.add(EmployeeNameAndPhoto(id, photo,  name)
                    )
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultEmployee(): ArrayList<Employee> {

        val listData: ArrayList<Employee> = arrayListOf()
        try {
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION)

            val cursor = dbRead.query (tableName, projection,
                null, null, null, null, null)

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                    val cargo = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                    val admission = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))

                    listData.add(Employee(id, photo, name, cargo, admission))
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultDataEmployeeId(id: Int): EmployeeEntityFull? {

        var employee: EmployeeEntityFull? = null
        try {
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.RG,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CPF,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CTPS,
                ConstantsEmployee.EMPLOYEE.COLUMNS.SALARIO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ESTADOCIVIL)

            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = dbRead.query (tableName, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()

                val active = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS))
                val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                val hora1 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                val hora2 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                val hora3 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                val hora4 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))
                val work = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
                val name = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                val email = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                val cargo = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                val phone = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                val admissao = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                val niver = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))
                val rg = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.RG))
                val cpf = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.CPF))
                val ctps = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.CTPS))
                val salario = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.SALARIO))
                val estadocivil = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ESTADOCIVIL))

                employee = EmployeeEntityFull(id, active, photo, hora1, hora2, hora3,
                    hora4, work, name, email, cargo, phone, admissao, niver, rg, cpf,
                    ctps, salario, estadocivil)

            }
            cursor?.close()
            return employee!!

        } catch (e: Exception) {
            return employee!!
        }
    }

    fun consultPhoto(id: Int): ByteArray? {

        try {
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = dbRead.query (tableName, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()

                return cursor.getBlob(
                    cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))

            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

    fun consultIdEmployeeByName(name: String): Int? {

        try {
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.ID)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(name)

            val cursor = dbRead.query (tableName, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()

                return cursor.getInt(
                    cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))

            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

    fun consultNameEmployeeById(id: Int): String? {

        try {
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = dbRead.query (tableName, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()

                return cursor.getString(
                    cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))

            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

    fun consultCargaHoraria(id: Int): Int {

        try {
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = dbRead.query (tableName, projection, selection, args,
                null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                return cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
            }
            cursor?.close()
            return 0

        } catch (e: Exception) {
            return 0
        }
    }

    fun consultHorarioInAndOut(id: Int): EmployeePointsTime? {

        var employee: EmployeePointsTime? = null
        try {
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4)

            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = dbRead.query (tableName, projection,
                selection, args, null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                val hora1 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                val hora2 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                val hora3 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                val hora4 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))

                employee = EmployeePointsTime(hora1, hora2, hora3, hora4)

            }
            cursor?.close()
            return employee

        } catch (e: Exception) {
            return employee
        }
    }

     */
}

