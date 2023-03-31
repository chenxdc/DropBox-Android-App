package com.teamwork.librarybox

import android.content.AsyncQueryHandler
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_metadata.*
import kotlinx.android.synthetic.main.activity_user_list.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MetadataActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    lateinit var fileName: EditText
    lateinit var fileMetadataList: ArrayList<String>
    lateinit var fileLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metadata)

        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, metadataAct, toolbar, R.string.open_nav, R.string.close_nav)
        metadataAct.addDrawerListener(toggle)
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

        fileLV = findViewById(R.id.lvMetadata)
        fileMetadataList = ArrayList()
        fileName = findViewById(R.id.editText_file_name)
        val btn = findViewById<Button>(R.id.bt_metadata)

        btn.setOnClickListener(){
            fileMetadataList = ArrayList()
            val file = fileName.text.toString()
            val urlBase = "http:/10.0.2.2:8080/metadata/getMetadataByFileName?fileName=${file}"
            ListFileMetadata().execute(urlBase)
        }

        navView.setNavigationItemSelectedListener(this)


    }

    inner class ListFileMetadata(): AsyncTask<String, String, String>(){
        override fun onPreExecute(){
            super.onPreExecute()
        }
        override fun doInBackground(vararg urlOld: String): String{
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
            val keyList = listOf("fileId",  "fileName",  "importDate",  "lastAccessDate", "fileUri",  "fileType")
            for(key in keyList){
              val value = jsonObject.getString(key)
             Log.d("JSON", value)
                fileMetadataList.add("${key}: $value")
            }

            item++
        }
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(
            this,
            android.R.layout.simple_list_item_1,
            fileMetadataList as List<String?>
        )
        fileLV.adapter = adapter
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
        metadataAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(metadataAct.isDrawerOpen(GravityCompat.START)){
            metadataAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}