package com.kikulabs.noteusingroom.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kikulabs.noteusingroom.R
import com.kikulabs.noteusingroom.activity.EditActivity
import com.kikulabs.noteusingroom.adapter.NoteAdapter
import com.kikulabs.noteusingroom.database.NoteRoomDatabase
import com.kikulabs.noteusingroom.entity.Note
import kotlinx.android.synthetic.main.fragment_all_notes.text_view_note_empty
import kotlinx.android.synthetic.main.fragment_personal_notes.*

class PersonalNotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNotesData()
    }

    private fun getNotesData() {
        val database = NoteRoomDatabase.getDatabase(requireContext())
        val dao = database.getNoteDao()
        val listItems = arrayListOf<Note>()
        listItems.addAll(dao.getByLabel("Personal"))
        setupRecyclerView(listItems)
        if (listItems.isNotEmpty()) {
            text_view_note_empty.visibility = View.GONE
        } else {
            text_view_note_empty.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(listItems: ArrayList<Note>) {
        rv_personal.apply {
            adapter = NoteAdapter(listItems, object : NoteAdapter.NoteListener {
                override fun onItemClicked(note: Note) {
                    val intent = Intent(context, EditActivity::class.java)
                    intent.putExtra(EditActivity().EDIT_NOTE_EXTRA, note)
                    startActivity(intent)
                }
            })

            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }
}