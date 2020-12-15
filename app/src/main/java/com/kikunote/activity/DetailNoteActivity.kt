package com.kikunote.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.awesomedialog.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kikunote.R
import com.kikunote.databinding.ActivityDetailNoteBinding
import com.kikunote.databinding.BottomsheetNoteBinding
import com.kikunote.entity.Note
import com.kikunote.method.DateChange
import com.kikunote.viewModel.NotesViewModel

class DetailNoteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailNoteBinding
    val editNoteExtra = "edit_note_extra"
    private lateinit var note: Note
    private lateinit var notesViewModel: NotesViewModel
    private val dateChange = DateChange()
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()

    }

    private fun initView() {

        if (intent.getParcelableExtra<Note>(editNoteExtra) != null) {

            note = intent.getParcelableExtra(editNoteExtra)!!
            binding.tvTitle.text = note.title
            binding.tvDate.text = dateChange.changeFormatDate(note.date)
            binding.tvBody.text = note.body

            if (note.updatedDate.isNotEmpty()) {
                if (dateChange.getToday() == note.updatedDate) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.updatedTime)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.updatedDate)}"
                }
            } else {
                if (dateChange.getToday() == note.date) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.time)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.date)}"
                }
            }

        }

    }

    private fun initViewModel() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private fun initListener() {
        binding.toolbar.nibBack.setOnClickListener(this)
        binding.toolbar.nibEdit.setOnClickListener(this)
        binding.ibMenu.setOnClickListener(this)
    }

    private fun deleteNote(note: Note) {
        notesViewModel.deleteNote(note)
        Toast.makeText(this@DetailNoteActivity, "Note removed", Toast.LENGTH_SHORT).show()
    }

    private fun showBottomSheet() {
        val views: View =
            LayoutInflater.from(this).inflate(R.layout.bottomsheet_note, null)

        val bindingBottom = BottomsheetNoteBinding.bind(views)

        dialog = BottomSheetDialog(this)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(views)
        dialog.show()

        bindingBottom.clDelete.setOnClickListener(this)
        bindingBottom.clShare.setOnClickListener(this)
    }

    private fun showDialog() {

        AwesomeDialog.build(this)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .body(
                "The note will be permanently deleted.",
                color = ContextCompat.getColor(this, R.color.colorTitle)
            )
            .background(R.drawable.background_dialog)
            .icon(R.mipmap.ic_launcher)
            .onPositive(
                "Yes, delete",
                buttonBackgroundColor = android.R.color.transparent,
                textColor = ContextCompat.getColor(this, R.color.colorTitle)
            ) {
                deleteNote(note)
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
            .onNegative(
                "Cancel",
                buttonBackgroundColor = R.drawable.bg_btn_black,
                textColor = ContextCompat.getColor(this, R.color.background)
            ) {

            }

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
            R.id.ib_menu -> {
                showBottomSheet()
            }
            R.id.cl_delete -> {
                showDialog()
            }
            R.id.cl_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.body}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }
}