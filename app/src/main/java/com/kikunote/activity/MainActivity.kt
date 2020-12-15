package com.kikunote.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kikunote.R
import com.kikunote.adapter.SectionsPagerAdapter
import com.kikunote.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()

    }

    private fun initView() {

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(view_pager)

    }

    private fun initListener() {
        binding.toolbar.ibSearch.setOnClickListener(this)
        binding.floatingActionButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ib_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.floatingActionButton -> {
                startActivity(Intent(this, EditActivity::class.java))
            }
        }
    }
}