package id.app.wawetech.testgrid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.error.ANError
import org.json.JSONArray
import org.json.JSONObject
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.androidnetworking.common.ANResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class MainActivity : AppCompatActivity(), ListImageFragment.SubmitListener {


    override fun onSubmit() {
        if (viewPager != null) {
            if (adapter != null) {
                val fragment = adapter.getItem(tabLayout.selectedTabPosition)
                if (fragment != null) {
                    val requestFragment = fragment as ListImageFragment
                    requestFragment!!.setRecyclerView(listimageurl)
                }
            }
        }
    }

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var imgprofil: CircleImageView
    var listimageurl: MutableList<String> = ArrayList()

    private lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgprofil = findViewById(R.id.imgprofil)
        Picasso.get()
            .load("https://st3.depositphotos.com/7863750/17911/i/1600/depositphotos_179111430-stock-photo-bad-cat-stole-bitcoins.jpg")
            .into(imgprofil)
        viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)

        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
        getAllTag()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        var fragment1: ListImageFragment = ListImageFragment()
        var fragment2: ListImageFragment = ListImageFragment()
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

    private fun getAllTag() {
        AndroidNetworking.initialize(this)
        AndroidNetworking.enableLogging();
        AndroidNetworking.get("https://api.imgur.com/3/tags")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    var list: MutableList<String> = ArrayList()
                    var jsonarray: JSONArray = response?.getJSONObject("data")?.getJSONArray("tags")!!
                    var data: Int = jsonarray.length() - 100
                    for (i in 1..data) {
                        var name = jsonarray.getJSONObject(i).getString("name")
                        list.add(name)
                    }
                    var tagging = object : TagAdapter.ItemListener {
                        override fun onClick(tag: String) {
                            showImageBaseOnTag(tag)
                        }
                    }
                    val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    val taglist = findViewById(R.id.listtag) as RecyclerView
                    taglist.layoutManager = layoutManager
                    taglist.adapter = TagAdapter(list, tagging)
                }

                override fun onError(anError: ANError?) {
                    anError?.stackTrace
                }
            })
    }


    fun showImageBaseOnTag(tag: String) {
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://api.imgur.com/3/gallery/t/$tag")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    listimageurl.clear()
                    var jsonarray: JSONArray = response?.getJSONObject("data")?.getJSONArray("items")!!
                    var data: Int = jsonarray.length()

                    for (i in 0..data - 1) {
                        var listimage: JSONArray
                        try {
                            listimage = jsonarray.getJSONObject(i).getJSONArray("images")
                        } catch (ex: Exception) {
                            continue
                        }
                        var img = listimage.length()
                        if (listimage != null) {
                            for (j in 0..img - 1) {
                                var link = listimage.getJSONObject(j).getString("link")
                                if (link.contains(".jpg")) {
                                    listimageurl.add(link)
                                }
                            }
                        }
                    }

                    onSubmit()
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext, "Data not available on server", Toast.LENGTH_LONG)
                }

            })

    }


}
