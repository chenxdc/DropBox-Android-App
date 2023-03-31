package com.teamwork.librarybox

import android.content.Intent
import android.graphics.Color
import android.net.VpnService.prepare
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar

import kotlinx.android.synthetic.main.activity_add_user.*
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class UserAddActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    lateinit var emailName: EditText
    lateinit var userName: EditText
    lateinit var firstName: EditText
    lateinit var lastName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle =
            ActionBarDrawerToggle(this, addUsersAct, toolbar, R.string.open_nav, R.string.close_nav)
        addUsersAct.addDrawerListener(toggle)
        toggle.syncState()


    emailName = findViewById(R.id.add_user_email_text)
    userName = findViewById(R.id.add_user_username)
    firstName = findViewById(R.id.add_user_first_name)
    lastName = findViewById(R.id.add_user_last_name)

    val btn = findViewById<Button>(R.id.btn_users_adduser)
    btn.setOnClickListener() {
        val email = emailName.text.toString()
        val userNombre = userName.text.toString()
        val firstNombre = firstName.text.toString()
        val lastNombre = lastName.text.toString()
        val functionality = "/user/addUser?"
        val urlBase =
            "http://10.0.2.2:8080" + functionality + "userName=" + userNombre + "&firstName=" + firstNombre + "&lastName=" + lastNombre + "&email=" + email
        //Toast.makeText(this, "User ${userNombre} successfully added", Toast.LENGTH_SHORT)

        val url = URL(urlBase)
        addUser(url).start()



        Log.d("URL", urlBase)
    }
    navView.setNavigationItemSelectedListener(this)

    }

    private fun addUser(getRequest: URL): Thread{
        return Thread{
            val connection = getRequest.openConnection() as HttpURLConnection
            if(connection.responseCode == 200){
                val request = connection.inputStream
                runOnUiThread { Toast.makeText(this, "User was successfully added", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Log.d("TOAST", "FAILED")
                runOnUiThread {
                    Toast.makeText(this, "User was not added", Toast.LENGTH_SHORT).show()
                }
                }
        }
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
        addUsersAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(addUsersAct.isDrawerOpen(GravityCompat.START)){
            addUsersAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}
