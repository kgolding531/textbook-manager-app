package com.example.textbookmanagerapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var listTextbooks = ArrayList<Textbook>()

    /*
    *  an Android framework class that provides a way to store and retrieve key-value pairs
    * of primitive data types (such as boolean, int, float, and string) in a persistent store.
    *
    * provides several methods for retrieving and modifying preference values.
    *
    * By storing preference data using SharedPreferences, an app can provide a personalized
    * experience for each user, as the app can remember and apply the user's settings across
    * multiple sessions. This can help to improve user engagement and satisfaction with the app.
    * */
    private var mSharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSharedPref = this.getSharedPreferences("My_Data", Context.MODE_PRIVATE)

        // load sorting technique as selected before, default setting will be newest
        val mSorting = mSharedPref!!.getString("Sort", "newest")

        when(mSorting) {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }
    }

    override fun onResume() {
        super.onResume()

        // load the sorting technique as selected before, default setting will be newest
        val mSorting = mSharedPref!!.getString("Sort", "newest")

        when (mSorting) {
            "newest" -> loadQueryNewest("%")
            "oldest" -> loadQueryOldest("%")
            "ascending" -> loadQueryAscending("%")
            "descending" -> loadQueryDescending("%")
        }
    }

    @SuppressLint("Range")
    private fun loadQueryAscending(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)

        // sort by title
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listTextbooks.clear()

        // ascending
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val ISBN = cursor.getString(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listTextbooks.add(Textbook(ID, ISBN, Title, Author, Course))

            } while (cursor.moveToNext())
        }

        //adapter
        val myTextbooksAdapter = MyTextbooksAdapter(this, listTextbooks)

        //set adapter
        val lvTextbook: ListView = findViewById(R.id.lvTextbooks)
        lvTextbook.adapter = myTextbooksAdapter

        //get total number of tasks from List
        val total = lvTextbook.count

        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null) {

            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total textbook(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun loadQueryDescending(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)

        //sort by title
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listTextbooks.clear()

        //descending
        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val ISBN = cursor.getString(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listTextbooks.add(Textbook(ID, ISBN, Title, Author, Course))

            } while (cursor.moveToPrevious())
        }

        //adapter
        val myTextbooksAdapter = MyTextbooksAdapter(this, listTextbooks)

        //set adapter
        val lvTextbooks: ListView = findViewById(R.id.lvTextbooks)
        lvTextbooks.adapter = myTextbooksAdapter

        //get total number of tasks from List
        val total = lvTextbooks.count

        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total textbook(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun loadQueryNewest(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)

        //sort by ID
        val cursor = dbManager.query(projections, "ID like ?", selectionArgs, "ID")
        listTextbooks.clear()

        // Newest first (the record will be entered at the bottom of previous records and
        // has larger ID then previous records)

        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val ISBN = cursor.getString(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listTextbooks.add(Textbook(ID, ISBN, Title, Author, Course))

            } while (cursor.moveToPrevious())
        }

        //adapter
        val myTextbooksAdapter = MyTextbooksAdapter(this, listTextbooks)

        //set adapter
        val lvTextbooks: ListView = findViewById(R.id.lvTextbooks)
        lvTextbooks.adapter = myTextbooksAdapter

        //get total number of tasks from List
        val total = lvTextbooks.count

        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total textbook(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun loadQueryOldest(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "ISBN", "Title", "Author", "Course")
        val selectionArgs = arrayOf(title)

        //sort by ID
        val cursor = dbManager.query(projections, "ID like ?", selectionArgs, "ID")
        listTextbooks.clear()

        // oldest first (the record will be entered at the bottom of previous records and has
        // larger ID then previous records, so lesser the ID is the oldest the record is)

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val ISBN = cursor.getString(cursor.getColumnIndex("ISBN"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Author = cursor.getString(cursor.getColumnIndex("Author"))
                val Course = cursor.getString(cursor.getColumnIndex("Course"))

                listTextbooks.add(Textbook(ID, ISBN, Title, Author, Course))

            } while (cursor.moveToNext())
        }

        //adapter
        val myTextbooksAdapter = MyTextbooksAdapter(this, listTextbooks)

        //set adapter
        val lvTextbooks: ListView = findViewById(R.id.lvTextbooks)
        lvTextbooks.adapter = myTextbooksAdapter

        //get total number of tasks from List
        val total = lvTextbooks.count

        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQueryAscending("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQueryAscending("%"+newText+"%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {

            when(item.itemId) {
                R.id.addTextbook -> {
                    startActivity(Intent(this, AddTextbook::class.java))
                }

                R.id.action_sort -> {
                    //show sorting dialog
                    showSortDialog()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val sortOption = arrayOf("Newest", "Oldest", "Title (Ascending)", "Title (Descending)")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Sort by")
        mBuilder.setIcon(R.drawable.ic_sort_black_24dp)
        mBuilder.setSingleChoiceItems(sortOption, -1) {
                dialogInterface, i ->

            if (i==0) {
                //newest first
                Toast.makeText(this,"Newest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "newest")
                editor.apply()
                loadQueryNewest("%")
            }

            if (i==1) {
                //older first
                Toast.makeText(this,"Oldest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "oldest")
                editor.apply()
                loadQueryOldest("%")
            }

            if (i==2) {
                //title ascending
                Toast.makeText(this,"Title (Ascending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "ascending")
                editor.apply()
                loadQueryAscending("%")
            }

            if (i==3) {
                //title descending
                Toast.makeText(this,"Title (Descending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "Descending")
                editor.apply()
                loadQueryDescending("%")
            }

            dialogInterface.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    inner class MyTextbooksAdapter : BaseAdapter {
        private var listTextbooksAdapter = ArrayList<Textbook>()
        private var context: Context? = null

        constructor(context: Context, listTextbooksAdapter: ArrayList<Textbook>) : super() {
            this.listTextbooksAdapter = listTextbooksAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            val myView = layoutInflater.inflate(R.layout.row, null)
            val myTextbook = listTextbooksAdapter[position]

            val tvISBN: TextView = myView.findViewById(R.id.tvISBN)
            val tvTitle: TextView = myView.findViewById(R.id.tvTitle)
            val tvAuthor: TextView = myView.findViewById(R.id.tvAuthor)
            val tvCourse: TextView = myView.findViewById(R.id.tvCourse)

            val ibDelete: ImageButton = myView.findViewById(R.id.ibDelete)
            val ibEdit: ImageButton = myView.findViewById(R.id.ibEdit)
            val ibCopy: ImageButton = myView.findViewById(R.id.ibCopy)
            val ibShare: ImageButton = myView.findViewById(R.id.ibShare)

            tvISBN.text = myTextbook.textbookISBN
            tvTitle.text = myTextbook.textbookTitle
            tvAuthor.text = myTextbook.textbookAuthor
            tvCourse.text = myTextbook.textbookCourse

            //delete btn click
            ibDelete.setOnClickListener {
                val dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myTextbook.textbookID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQueryAscending("%")
            }

            // edit
            // update button click
            ibEdit.setOnClickListener {
                goToUpdateFun(myTextbook)
            }

            //copy btn click
            ibCopy.setOnClickListener {
                //get ISBN
                val ISBN = tvISBN.text.toString()

                //get title
                val title = tvTitle.text.toString()

                //get author
                val author = tvAuthor.text.toString()

                //get course
                val course = tvCourse.text.toString()

                //concatenate
                val s = ISBN + "\n" + title + "\n" + author + "\n" + course
                val cb = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s // add to clipboard
                Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_SHORT).show()
            }

            //share btn click
            ibShare.setOnClickListener {

                //get ISBN
                val ISBN = tvISBN.text.toString()

                //get title
                val title = tvTitle.text.toString()

                //get author
                val author = tvAuthor.text.toString()

                //get course
                val course = tvCourse.text.toString()

                //concatenate
                val s = ISBN + "\n" + title + "\n" + author + "\n" + course

                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return listTextbooksAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listTextbooksAdapter.size
        }
    }

    private fun goToUpdateFun(myTextbook: Textbook) {
        val intent = Intent(this, AddTextbook::class.java)
        intent.putExtra("ID", myTextbook.textbookID)
        intent.putExtra("ISBN", myTextbook.textbookISBN)
        intent.putExtra("title", myTextbook.textbookTitle)
        intent.putExtra("author", myTextbook.textbookAuthor)
        intent.putExtra("course", myTextbook.textbookCourse)

        startActivity(intent)
    }
}