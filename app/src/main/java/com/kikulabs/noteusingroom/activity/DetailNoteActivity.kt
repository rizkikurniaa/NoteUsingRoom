package com.kikulabs.noteusingroom.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kikulabs.noteusingroom.R
import com.kikulabs.noteusingroom.databinding.ActivityDetailNoteBinding
import com.kikulabs.noteusingroom.entity.Note
import com.kikulabs.noteusingroom.method.DateChange

class DetailNoteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailNoteBinding
    val editNoteExtra = "edit_note_extra"
    private lateinit var note: Note
    private val dateChange = DateChange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()

    }

    private fun initView() {

        if (intent.getParcelableExtra<Note>(editNoteExtra) != null) {

            note = intent.getParcelableExtra(editNoteExtra)!!
            binding.tvTitle.text = note.title
            binding.tvDate.text =  dateChange.changeFormatDate(note.date)
            binding.tvBody.text = note.body

        }

    }

    private fun initListener() {
        binding.toolbar.nibBack.setOnClickListener(this)
        binding.toolbar.nibEdit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
            R.id.nib_edit -> {
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra(EditActivity().editNoteExtra, note)
                startActivity(intent)
            }
        }
    }
}