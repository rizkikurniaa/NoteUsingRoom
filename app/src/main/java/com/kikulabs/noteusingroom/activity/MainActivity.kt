package com.kikulabs.noteusingroom.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kikulabs.noteusingroom.R
import com.kikulabs.noteusingroom.adapter.NoteAdapter
import com.kikulabs.noteusingroom.adapter.SectionsPagerAdapter
import com.kikulabs.noteusingroom.database.NoteRoomDatabase
import com.kikulabs.noteusingroom.entity.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_home.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initListener()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun initView() {

        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

    }

    private fun initListener() {
        ib_search.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ib_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }
}