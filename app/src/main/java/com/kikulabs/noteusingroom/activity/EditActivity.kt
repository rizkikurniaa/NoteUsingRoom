package com.kikulabs.noteusingroom.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.awesomedialog.*
import com.kikulabs.noteusingroom.R
import com.kikulabs.noteusingroom.dao.NoteDao
import com.kikulabs.noteusingroom.database.NoteRoomDatabase
import com.kikulabs.noteusingroom.databinding.ActivityEditBinding
import com.kikulabs.noteusingroom.entity.Note

class EditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditBinding
    val EDIT_NOTE_EXTRA = "edit_note_extra"
    private lateinit var note: Note
    private var isUpdate = false
    private lateinit var database: NoteRoomDatabase
    private lateinit var dao: NoteDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()

    }

    private fun initView() {
        database = NoteRoomDatabase.getDatabase(applicationContext)
        dao = database.getNoteDao()

        if (intent.getParcelableExtra<Note>(EDIT_NOTE_EXTRA) != null) {

            isUpdate = true
            binding.buttonDelete.visibility = View.VISIBLE
            note = intent.getParcelableExtra(EDIT_NOTE_EXTRA)!!
            binding.editTextTitle.setText(note.title)
            binding.editTextBody.setText(note.body)

            binding.editTextTitle.setSelection(note.title.length)

        }
    }

    private fun initListener() {
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDelete.setOnClickListener(this)
        binding.toolbar.nibBack.setOnClickListener(this)
    }

    private fun saveNote(note: Note) {

        if (dao.getById(note.id).isEmpty()) {
            dao.insert(note)
        } else {
            dao.update(note)
        }

        Toast.makeText(applicationContext, "Note saved", Toast.LENGTH_SHORT).show()

    }

    private fun deleteNote(note: Note) {
        dao.delete(note)
        Toast.makeText(applicationContext, "Note removed", Toast.LENGTH_SHORT).show()
    }

    private fun showDialog() {

        AwesomeDialog.build(this)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .body("The note will be permanently deleted.")
            .icon(R.mipmap.ic_launcher)
            .onPositive(
                "Yes, delete",
                buttonBackgroundColor = android.R.color.white,
                textColor = ContextCompat.getColor(this, android.R.color.black)
            ) {
                deleteNote(note)
                val intent = Intent(this@EditActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            .onNegative(
                "Cancel",
                buttonBackgroundColor = R.drawable.bg_btn_black,
                textColor = ContextCompat.getColor(this, android.R.color.white)
            ) {

            }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
            R.id.button_save -> {
                val title = binding.editTextTitle.text.toString()
                val body = binding.editTextBody.text.toString()
                val label = binding.spLabel.selectedItem.toString()

                if (title.isEmpty() && body.isEmpty()) {
                    Toast.makeText(applicationContext, "Note cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isUpdate) {
                        saveNote(Note(id = note.id, title = title, label = label, body = body))
                    } else {
                        saveNote(Note(title = title, label = label, body = body))
                    }
                }
                val intent = Intent(this@EditActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            R.id.button_delete -> {
                showDialog()
            }
        }
    }
}