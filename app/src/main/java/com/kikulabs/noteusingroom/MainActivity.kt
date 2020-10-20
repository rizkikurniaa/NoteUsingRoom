package com.kikulabs.noteusingroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kikulabs.noteusingroom.adapter.NoteAdapter
import com.kikulabs.noteusingroom.database.NoteRoomDatabase
import com.kikulabs.noteusingroom.entity.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getNotesData()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun getNotesData(){
        val database = NoteRoomDatabase.getDatabase(applicationContext)
        val dao = database.getNoteDao()
        val listItems = arrayListOf<Note>()
        listItems.addAll(dao.getAll())
        setupRecyclerView(listItems)
        if (listItems.isNotEmpty()){
            text_view_note_empty.visibility = View.GONE
        }
        else{
            text_view_note_empty.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(listItems: ArrayList<Note>){
        recycler_view_main.apply {
            adapter = NoteAdapter(listItems, object : NoteAdapter.NoteListener{
                override fun OnItemClicked(note: Note) {
                    val intent = Intent(this@MainActivity, EditActivity::class.java)
                    intent.putExtra(EditActivity().EDIT_NOTE_EXTRA, note)
                    startActivity(intent)
                }
            })

            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        getNotesData()
    }
}