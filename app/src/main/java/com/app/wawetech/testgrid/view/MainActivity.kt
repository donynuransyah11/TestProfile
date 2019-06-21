package com.app.wawetech.testgrid.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import org.json.JSONArray
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList
import android.arch.lifecycle.ViewModelProviders
import com.app.wawetech.testgrid.ListImageFragment
import com.app.wawetech.testgrid.R
import com.app.wawetech.testgrid.adapter.TagAdapter
import org.json.JSONObject


class MainActivity : AppCompatActivity(), ListImageFragment.SubmitListener {


    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var imgprofil: CircleImageView
    private var listImageurl: MutableList<String> = ArrayList()
    private lateinit var viewModel: ImageDataVM
    private lateinit var adapter: ViewPagerAdapter
    private var list: MutableList<String> = ArrayList()


    override fun onSubmit() {
        val fragment = adapter.getItem(tabLayout.selectedTabPosition)
        val requestFragment = fragment as ListImageFragment
        requestFragment.setRecyclerView(listImageurl)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Inisialisasi ViewModel
         * ViewModelProviders.of(activity).get(Class ViewModelmu)
         */
        viewModel = ViewModelProviders.of(this).get(ImageDataVM::class.java)

        setContentView(R.layout.activity_main)
        initView()
        loadProfileImage()

        setupViewPager(viewPager)

        /**
         * Trigger Request API{get_list_gambar} ke ViewModel
         */
        viewModel.getListImage()

        /**
         * Observe value yang ada pada ViewModel
         */
        viewModel.allImage.observe(this, android.arch.lifecycle.Observer { response ->
            response.processAllImage()
        })

        /**
         * Observe value yang ada pada ViewModel
         */
        viewModel.tagImage.observe(this, android.arch.lifecycle.Observer { response ->
            response.processTagImage()
        })
    }


    private fun initView() {
        imgprofil = findViewById(R.id.imgprofil)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun loadProfileImage() {
        Picasso.get()
            .load("https://st3.depositphotos.com/7863750/17911/i/1600/depositphotos_179111430-stock-photo-bad-cat-stole-bitcoins.jpg")
            .into(imgprofil)
    }


    /**
     * Trigger Request API ke ViewModel get list gambar berdasarkan tag
     */
    fun showImageBaseOnTag(tag: String) {
        viewModel.getImageByTag(tag)
    }


    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        var fragment1 = ListImageFragment()
        var fragment2 = ListImageFragment()
        adapter.addFragment(fragment1, "Image 1")
        adapter.addFragment(fragment2, "Image 2")
        viewPager.adapter = adapter
        fragment1.setSubmitListener(this)
        fragment2.setSubmitListener(this)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }


    /**
     * Kotlin Extension
     * @receiver JSONObject?
     */
    private fun JSONObject?.processAllImage() {
        this?.let { response ->
            list.clear()
            var jsonarray: JSONArray = response.getJSONObject("data")?.getJSONArray("tags")!!
            var data: Int = jsonarray.length() - 100
            for (i in 1 until data) {
                list.add(jsonarray.getJSONObject(i).getString("name"))
            }
            var tagging = object : TagAdapter.ItemListener {
                override fun onClick(tag: String) {
                    showImageBaseOnTag(tag)
                }
            }
            val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            val taglist = findViewById<RecyclerView>(R.id.listtag)
            taglist.layoutManager = layoutManager
            taglist.adapter = TagAdapter(list, tagging)

        }
    }


    /**
     * Kotlin extension
     * @receiver JSONObject?
     */
    private fun JSONObject?.processTagImage() {
        this?.let { response ->
            listImageurl.clear()
            var jsonarray: JSONArray = response?.getJSONObject("data")?.getJSONArray("items")!!
            var data: Int = jsonarray.length()

            for (i in 0 until data - 1) {
                var listimage: JSONArray
                try {
                    listimage = jsonarray.getJSONObject(i).getJSONArray("images")
                } catch (ex: Exception) {
                    continue
                }
                var img = listimage.length()
                if (listimage != null) {
                    for (j in 0 until img - 1) {
                        var link = listimage.getJSONObject(j).getString("link")
                        if (link.contains(".jpg")) {
                            listImageurl.add(link)
                        }
                    }
                }
            }

            onSubmit()
        }
    }


}
