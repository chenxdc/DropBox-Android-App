package com.teamwork.librarybox

import android.annotation.SuppressLint
import android.content.Intent
import retrofit2.Callback
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.teamwork.librarybox.models.File
import com.teamwork.librarybox.models.FileResponse
import com.teamwork.librarybox.models.ServiceGenerator
import com.teamwork.librarybox.models.services.FileApiInterface
import com.teamwork.librarybox.models.services.FileApiService
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search_result.*
import retrofit2.Call
import retrofit2.Response

class SearchResultActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,MySearchResultRecyclerViewAdapter.MyItemClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)


        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, searchResultAct, toolbar, R.string.open_nav, R.string.close_nav)
        searchResultAct.addDrawerListener(toggle)
        toggle.syncState()
        /*
        if(toggle != null){
            if(auth.currentUser != null){
                auth.currentUser?.let{
                    val email = findViewById<TextView>(R.id.login_user_email)
                    email.text = it.email
                }
            }
        }

         */

        navView.setNavigationItemSelectedListener(this)

        bt_button_search_return.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }


        val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
        val call = serviceGenerator.getFiles()

        val recyclerView = findViewById<RecyclerView>(R.id.rview1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        getFileData{files : List<ListModel> ->
            rview1.adapter = ListAdapter(this, files)

        }
        //recyclerView.adapter = myAdapter
      /*  call.enqueue(object : Callback<MutableList<ListModel>> {
            override fun onResponse(call: Call<MutableList<ListModel>>, response: Response<MutableList<ListModel>>) {
                if(response.isSuccessful){
                    Log.e("success", response.toString())
                    Log.e("success", response.body().toString())
                         recyclerView.apply{
                             adapter = ListAdapter(response.body()!!)
                         }


                }
            }

            override fun onFailure(call: Call<MutableList<ListModel>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        }) */

    }

    private fun getFileData(callback: (List<ListModel>) -> Unit ){
        val apiService = ServiceGenerator.buildService(ApiService::class.java)
        apiService.getFiles().enqueue(object : Callback<MutableList<ListModel>>{
            override fun onResponse(call: Call<MutableList<ListModel>>, response: Response<MutableList<ListModel>>) {
                if(response.isSuccessful){
                    Log.e("success", response.toString())
                    Log.e("success", response.body().toString())
                    return callback(response.body()!!)
                }


                }


            override fun onFailure(call: Call<MutableList<ListModel>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean { // handler navigation menu item selection!
        when(item.itemId){
            R.id.nav_menu ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_search ->{
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_users ->{
                val intent = Intent(this, UsersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_metadata ->{
                val intent = Intent(this, MetadataActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_permission ->{
                val intent = Intent(this, PermissionsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    baseContext, "Logout Success",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        searchResultAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(searchResultAct.isDrawerOpen(GravityCompat.START)){
            searchResultAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }


    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        return super.onCreateOptionsMenu(menu)
    }


    override fun onItemClickedFromAdapter(file: FileData) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClickedFromAdapter(position: Int) {
    }


    @SuppressLint("RestrictedApi")
    override fun onOverflowMenuClickedFromAdapter(view: View, position: Int) {
    }
}