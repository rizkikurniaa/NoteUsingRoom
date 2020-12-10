package com.kikulabs.noteusingroom.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kikulabs.noteusingroom.R
import com.kikulabs.noteusingroom.adapter.NoteAdapter
import com.kikulabs.noteusingroom.dao.NoteDao
import com.kikulabs.noteusingroom.database.NoteRoomDatabase
import com.kikulabs.noteusingroom.entity.Note
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_search.*

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener, View.OnClickListener {
    private lateinit var dao: NoteDao
    private val listItems = arrayListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        initListener()
        getNotesData()
    }

    private fun initView() {
        val searchPlateId: Int = search_notes.context.resources
            .getIdentifier("android:id/search_plate", null, null)
        val searchPlate: View =
            search_notes.findViewById(searchPlateId)
        searchPlate.setBackgroundColor(Color.TRANSPARENT)

        val linearLayout1 = search_notes.getChildAt(0) as LinearLayout
        val linearLayout2 = linearLayout1.getChildAt(2) as LinearLayout
        val linearLayout3 = linearLayout2.getChildAt(1) as LinearLayout
        val autoComplete = linearLayout3.getChildAt(0) as AutoCompleteTextView
        autoComplete.textSize = 14f

        search_notes.setOnQueryTextListener(this)
        search_notes.isFocusable = false

        val database = NoteRoomDatabase.getDatabase(applicationContext)
        dao = database.getNoteDao()
    }

    private fun getNotesData() {
        listItems.addAll(dao.getAll())
        setupRecyclerView(listItems)

        if (listItems.isNotEmpty()) {
            tv_note_empty.visibility = View.GONE
        } else {
            tv_note_empty.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(listItems: ArrayList<Note>) {
        rv_notes.apply {
            adapter = NoteAdapter(listItems, object : NoteAdapter.NoteListener {
                override fun onItemClicked(note: Note) {
                    val intent = Intent(this@SearchActivity, EditActivity::class.java)
                    intent.putExtra(EditActivity().EDIT_NOTE_EXTRA, note)
                    startActivity(intent)
                }
            })

            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private fun initListener() {
        nib_back.setOnClickListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(keyWord: String?): Boolean {
        listItems.clear()
        listItems.addAll(dao.getByTitle("%${keyWord}%"))
        setupRecyclerView(listItems)

        if (listItems.isNotEmpty()) {
            tv_note_empty.visibility = View.GONE
        } else {
            tv_note_empty.visibility = View.VISIBLE
            tv_note_empty.text = "Note not found"
        }

        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
        }
    }
}