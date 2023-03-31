package com.teamwork.librarybox

import android.content.Intent
import android.os.AsyncTask
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar

import kotlinx.android.synthetic.main.activity_permission_list.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class PermissionListActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    lateinit var permList: ArrayList<String>
    lateinit var permLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_list)

        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, listPermissionsAct, toolbar, R.string.open_nav, R.string.close_nav)
        listPermissionsAct.addDrawerListener(toggle)
        toggle.syncState()
        permLV = findViewById(R.id.lvpermissions)
        permList = ArrayList()
        val url = "http://10.0.2.2:8080/permission/getUserPermissions?userId=448fb6c3-7ec8-478d-97fc-7e879e6b8827"
        val btn = findViewById<Button>(R.id.permissions_list_user_now)
        btn.setOnClickListener(){
            DownloadUserPermission().execute(url)
        }

        navView.setNavigationItemSelectedListener(this)


    }

    inner class DownloadUserPermission() : AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg urlOld: String): String {
            var holderJson = ""
            val url = URL(urlOld[0])
            val connection = url.openConnection() as HttpURLConnection
            if (connection.responseCode == 200) {
                val gson = Gson()
                var text = url.readText()
                holderJson = text
            } else {
                Log.d("FAILURE", "Connection failed")
            }
            return holderJson
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(result!=null){
                handleJson(result)
            }
        }


    }
    private fun handleJson(jsonString: String){
        val jsonArray = JSONArray(jsonString)
        var item = 0
        while(item < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(item)
            Log.d("JSON", jsonObject.toString())
            var lookup = "permizzion"
            permList.add(jsonObject.getString("$lookup"))
            item++
        }
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this,
            android.R.layout.simple_list_item_1,
            permList as List<String?>
        )
        permLV.adapter = adapter
        adapter.notifyDataSetChanged()
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
        listPermissionsAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(listPermissionsAct.isDrawerOpen(GravityCompat.START)){
            listPermissionsAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}