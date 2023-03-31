package com.teamwork.librarybox

import android.content.Intent
import android.graphics.Color
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
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar

import kotlinx.android.synthetic.main.activity_permission_add.*
import java.net.HttpURLConnection
import java.net.URL

class PermissionAddActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    lateinit var emailName: EditText
    lateinit var permName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_add)

        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, addPermissionsAct, toolbar, R.string.open_nav, R.string.close_nav)
        addPermissionsAct.addDrawerListener(toggle)
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
        emailName = findViewById(R.id.editText_email_add_permission)
        permName = findViewById(R.id.editText_password_add_permission)
        val btn = findViewById<Button>(R.id.users_add_user_permission_now)

        btn.setOnClickListener(){
            val email = emailName.text.toString()
            val perm = permName.text.toString().uppercase()
            val urlBase = "http:/10.0.2.2:8080/permission/assignUserPermission?email=${email}&permission=${perm}"

            val url = URL(urlBase)
            assignUserPermission(url).start()



            Log.d("URL", urlBase)
        }
        navView.setNavigationItemSelectedListener(this)

    }

    private fun assignUserPermission(getRequest: URL): Thread{
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
                    Toast.makeText(this, "User was not added", Toast.LENGTH_SHORT)
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
        addPermissionsAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(addPermissionsAct.isDrawerOpen(GravityCompat.START)){
            addPermissionsAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}