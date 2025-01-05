package com.kikunote.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kikunote.R
import com.kikunote.adapter.NoteAdapter
import com.kikunote.databinding.ActivitySearchBinding
import com.kikunote.entity.Note
import com.kikunote.viewModel.NotesViewModel

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener, View.OnClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var listNoteAdapter: NoteAdapter
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()
    }

    private fun initView() {
        val searchPlateId: Int = binding.toolbar.searchNotes.context.resources
            .getIdentifier("android:id/search_plate", null, null)
        val searchPlate: View =
            binding.toolbar.searchNotes.findViewById(searchPlateId)
        searchPlate.setBackgroundColor(Color.TRANSPARENT)

        val linearLayout1 = binding.toolbar.searchNotes.getChildAt(0) as LinearLayout
        val linearLayout2 = linearLayout1.getChildAt(2) as LinearLayout
        val linearLayout3 = linearLayout2.getChildAt(1) as LinearLayout
        val autoComplete = linearLayout3.getChildAt(0) as AutoCompleteTextView
        autoComplete.textSize = 14f

        binding.toolbar.searchNotes.setOnQueryTextListener(this)
        binding.toolbar.searchNotes.isFocusable = false

        binding.rvNotes.setHasFixedSize(true)
        listNoteAdapter = NoteAdapter()
        listNoteAdapter.notifyDataSetChanged()

        binding.rvNotes.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvNotes.adapter = listNoteAdapter

        listNoteAdapter.setOnClicked(object : NoteAdapter.NoteListener {
            override fun onItemClicked(note: Note) {
                val intent = Intent(this@SearchActivity, DetailNoteActivity::class.java)
                intent.putExtra(DetailNoteActivity().editNoteExtra, note)
                startActivity(intent)
            }

        })

    }

    private fun initViewModel() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        notesViewModel.getNotes().observe(this, Observer { notes ->
            if (notes.isNotEmpty()) {
                binding.tvNoteEmpty.visibility = View.GONE
            } else {
                binding.tvNoteEmpty.visibility = View.VISIBLE
            }

            listNoteAdapter.setData(notes)
        })

    }

    private fun initListener() {
        notesViewModel.setNotes()
        binding.toolbar.nibBack.setOnClickListener(this)
    }

    override fun onQueryTextSubmit(keyWord: String?): Boolean {
        if (keyWord != null) {
            notesViewModel.setNotesByTitle("%${keyWord}%")
        }

        return true
    }

    override fun onQueryTextChange(keyWord: String?): Boolean {
        if (keyWord != null) {
            notesViewModel.setNotesByTitle("%${keyWord}%")
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