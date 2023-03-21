package com.example.textbookmanagerapp

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTextbook: AppCompatActivity() {

    // val dbTable = "Textbooks"
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_textbook)

        val addBtn: Button = findViewById(R.id.addBtn)
        val etISBN: EditText = findViewById(R.id.etISBN)
        val etTitle: EditText = findViewById(R.id.etTitle)
        val etAuthor: EditText = findViewById(R.id.etAuthor)
        val etCourse: EditText = findViewById(R.id.etCourse)

        try {
            /*
            * Bundle provides a simple and flexible way to pass data of different types such as
            * integers or even arrays between components in an Android application.
            * */
            val bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)

            if (id != 0) {
                // update the textbook
                // change actionbar title
                supportActionBar!!.title = "Update Textbook"

                // change button text
                addBtn.text = getString(R.string.addBtn_text)
                etISBN.setText(bundle.getString("isbn"))
                etTitle.setText(bundle.getString("title"))
                etAuthor.setText(bundle.getString("author"))
                etCourse.setText(bundle.getString("course"))
            }
        } catch (ex: Exception) {
            // no code
        }
    }

    fun addFunc(view: View) {
        val dbManager = DbManager(this)

        val values = ContentValues()

        val etISBN: EditText = findViewById(R.id.etISBN)
        val etTitle: EditText = findViewById(R.id.etTitle)
        val etAuthor: EditText = findViewById(R.id.etAuthor)
        val etCourse: EditText = findViewById(R.id.etCourse)

        values.put("ISBN", etISBN.text.toString())
        values.put("Title", etTitle.text.toString())
        values.put("Author", etAuthor.text.toString())
        values.put("Course", etCourse.text.toString())

        val isbn = etISBN.text.toString()
        val title = etTitle.text.toString()
        val author = etAuthor.text.toString()
        val course = etCourse.text.toString()

        if (id == 0) {
            val ID = dbManager.insert(values)

            if (ID > 0) {

                if (validateFields(isbn, title, author, course)) {
                    Toast.makeText(this, "Textbook added", Toast.LENGTH_SHORT).show()
                    finish()
                }
                    else {
                        Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                }
            }
                else {
                    Toast.makeText(this, "Error adding textbook...", Toast.LENGTH_SHORT).show()
                }
        }
            else {
                val selectionArgs = arrayOf(id.toString())
                val ID = dbManager.update(values, "ID=?", selectionArgs)

                if (ID > 0) {
                    Toast.makeText(this, "Textbook updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
                    else {
                        Toast.makeText(this, "Error updating textbook...", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    // TODO: TRIM TEXT FOR SORTING PURPOSES, SEE WHY ISBN FIELD DOESNT RETAIN INFO WHEN UPDATING IT
    //  ALLOW PASTING AND SHARING TO SPECIFY THE ISBN, TITLE, ETC... AND SEE WHY UPDATING/EDITING DOESNT
    //  BIG ISSUE: THE APP JUST STOPPED WORKING ALL OF A SUDDEN... NOTHING IS POPPING UP!! :sob:
    //  WORK ALL THE TIME. CHECK OUT THE SEARCHING PART OF THE APP TOO


    private fun validateFields(isbn: String, title: String, author: String, course: String): Boolean {
        return !isbn.isNullOrEmpty() && !title.isNullOrEmpty() && !author.isNullOrEmpty()
                && !course.isNullOrEmpty()
    }
}