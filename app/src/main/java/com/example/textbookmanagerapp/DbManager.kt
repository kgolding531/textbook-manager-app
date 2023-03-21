package com.example.textbookmanagerapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {
    //  database name
    var dbName = "TEXTBOOKS"

    // table name
    var dbTable = "Textbooks"

    // columns
    private var colID = "ID"
    private var colISBN = "ISBN"
    private var colTitle = "Title"
    private var colAuthor = "Author"
    private var colCourse = "Course"

    // database version
    var dbVersion = 1

    // Create the table if it does not exist
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colID + " INTEGER PRIMARY KEY, " +
                colISBN + " TEXT, " + colTitle + " TEXT, " + colAuthor + " TEXT, " +
                colCourse + " TEXT)"

    // initialise the the SQLiteDatabase object (a class in Android that represents a SQLite database)
    private var sqlDB: SQLiteDatabase? = null

    /*
    * Context is a class that represents the current state of an application. It provides access
    * to application-specific resources, such as system services, activities, and the application's
    * package and resource files.
    *
    * It is used to get information about the application environment and to perform a variety of
    * operations, such as starting activities, accessing databases, and accessing the file system.
    *
    * There are different types of Context in Android depending on the context in which they are
    * used, such as Activity, Service, BroadcastReceiver, and ContentProvider.
    * */

    constructor(context: Context) {
        // create instance of the DatabaseHelperTextbooks class and pass the context argument to
        // its constructor, and assign it to the variable db
        val db = DatabaseHelperTextbooks(context)

        /*
        * the writableDatabase method is called on the db object, which returns an instance of the
        * SQLiteDatabase class representing a writable database. In Android development, a
        * writable database is a database that can be modified, that is, it allows users to add,
        * update, and delete data from the database
        * */
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperTextbooks: SQLiteOpenHelper {
        private var context: Context? = null

        constructor(context: Context): super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database created...", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table if Exists" + dbTable)
        }
    }

    // Create the 4 different functions for the 4 different operations

    fun insert(values: ContentValues): Long {
        val ID = sqlDB!!.insert(dbTable, "", values)
        return ID
    }

    fun query(projection: Array<String>, selection: String, selectionArgs: Array<String>,
              sorOrder: String): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null,
            null, sorOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }
}