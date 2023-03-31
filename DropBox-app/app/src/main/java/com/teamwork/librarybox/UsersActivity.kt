package com.teamwork.librarybox

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.navView
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        auth = FirebaseAuth.getInstance()
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, usersAct, toolbar, R.string.open_nav, R.string.close_nav)
        usersAct.addDrawerListener(toggle)
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

        users_add_button.setOnClickListener {
            val intent = Intent(this, UserAddActivity::class.java)
            startActivity(intent)
        }
        users_delete_button.setOnClickListener {
            val intent = Intent(this, UserDeleteActivity::class.java)
            startActivity(intent)
        }
        users_list_button.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
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
        usersAct.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onBackPressed() {
        if(usersAct.isDrawerOpen(GravityCompat.START)){
            usersAct.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}