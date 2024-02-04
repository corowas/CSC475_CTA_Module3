// TaskDatabaseHelper.kt
package com.example.ctamodule3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDatabase"
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IS_DONE = "is_done"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_IS_DONE INTEGER DEFAULT 0
            )
        """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(task: Task) {
        val values = ContentValues()
        values.put(COLUMN_NAME, task.name)
        values.put(COLUMN_IS_DONE, if (task.isDone) 1 else 0)

        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun getAllTasks(): MutableList<Task> {
        val tasks = mutableListOf<Task>()

        val cursor: Cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val isDone = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DONE)) == 1

            tasks.add(Task(name, isDone, id))
        }

        cursor.close()
        return tasks
    }

    fun updateTask(task: Task) {
        val values = ContentValues()
        values.put(COLUMN_NAME, task.name)
        values.put(COLUMN_IS_DONE, if (task.isDone) 1 else 0)

        writableDatabase.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(task.id.toString()))
    }

    fun deleteTask(task: Task) {
        writableDatabase.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(task.id.toString()))
    }
}
