package com.teamwork.librarybox

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.homework_5.MovieData
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MySearchResultRecyclerViewAdapter(val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MySearchResultRecyclerViewAdapter.SearchResultHolder>() {

    var items: ArrayList<FileData>

    init {
        val urlApi = "http://10.0.2.2:8080/search/searchAll?userId=448fb6c3-7ec8-478d-97fc-7e879e6b8827"

        items = ArrayList()
        getAllFileUris().execute(urlApi)
        items=this.items
        Log.d("TEST", "We hit this " + items.size.toString())

    }

    var myListener: MyItemClickListener? = null
    var lastPosition = -1 // for animation
    // Adapter Listener!!!
    interface MyItemClickListener {
        fun onItemClickedFromAdapter(file : FileData)
        fun onItemLongClickedFromAdapter(position : Int)
        fun onOverflowMenuClickedFromAdapter(view: View, position: Int)
    }
    // This is for host activity or host fragment to call
    fun setMyItemClickListener (listener: MySearchResultRecyclerViewAdapter.MyItemClickListener?){
        this.myListener = listener
    }

    var posterTable:MutableMap<String, Int> = mutableMapOf()

    // MUST DO!!
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchResultHolder {
        val layoutInflater = LayoutInflater.from(p0.context) // p0 is parent
        val view : View

        // p1 -> View Type, check getItemViewType function!!

        view = layoutInflater.inflate(R.layout.search_result_card, p0, false)

        return SearchResultHolder(view)

        //val view = LayoutInflater.from(p0.context).inflate(R.layout.card_view_item, p0,false)
        //return MovieViewHolder(view)
    }

    private fun setAnimation(view: View, position: Int){
        if(position != lastPosition){
            when(getItemViewType(position)){
                1 -> {
                    val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
                    animation.duration = 700
                    animation.startOffset = position * 100L
                    view.startAnimation(animation)
                }
                2 -> {
                    val animation = AlphaAnimation(0.0f, 1.0f)
                    animation.duration = 1000
                    animation.startOffset = position * 50L
                    view.startAnimation(animation)
                }
                3 -> {
                    val animation = AlphaAnimation(0.0f, 1.0f)
                    animation.duration = 1000
                    animation.startOffset = position * 50L
                    view.startAnimation(animation)
                }
                4 -> {
                    val animation = AlphaAnimation(0.0f, 1.0f)
                    animation.duration = 1000
                    animation.startOffset = position * 50L
                    view.startAnimation(animation)
                }
                else -> {
                    val animation = ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f)
                    animation.duration = 500
                    animation.startOffset = position * 200L
                    view.startAnimation(animation)
                }
            }
            //animation.startOffset = position * 100L
            lastPosition = position
        }
    }

    // MUST DO!!
    override fun getItemCount(): Int {
        return this.items.size

    }

    // MUST DO!!
    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        Log.d("TEST", "We hit this function")
        val file = this.items[position]

        val fileName = file.fileName
        val filePath = file.fileImagePath
       // val url = "https://image.tmdb.org/t/p/w342/" + movie.poster_path!!

        Picasso.get()
            .load(file.fileImagePath)
            .into(holder.fileImage)
        Log.d("TEST", holder.fileImage.toString())
       // Picasso.get().load(url).into(holder.moviePoster)

        holder.fileName.text = fileName

        /*
        //holder.moviePoster.setImageResource(posterTable[movie.title]!!)
        holder.movieTitle.text = movie.title
        holder.rating.rating = movie!!.vote_average?.toFloat()?.div(2) ?: 8.0.toFloat()
        var length = movie.overview!!.length
        length = if (length > 100) 100 else length
        holder.movieOverview.text = movie.overview.substring(0, length - 1) + " ..."
        holder.movieSelect.isChecked = movie.checked!!
*/
        setAnimation(holder.fileImage, position)
    }

    override fun getItemViewType(position: Int): Int {
        //return position % 6
        return position
    }

    inner class SearchResultHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val fileImage = view.findViewById<ImageView>(R.id.fileImage)
        val fileName = view.findViewById<TextView>(R.id.fileTextName)
        //val overflow = view.findViewById<ImageView>(R.id.overflow)
        init{
            view.setOnClickListener {
                if(myListener != null){
                    if(adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION){
                        myListener!!.onItemClickedFromAdapter(items[adapterPosition])
                    }
                }
            }
            view.setOnLongClickListener {
                if(myListener != null){
                    if(adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION){
                        //myListener!!.onItemLongClickedFromAdapter(adapterPosition)
                        myListener!!.onOverflowMenuClickedFromAdapter(it, adapterPosition)
                    }
                }
                true
            }
        }
    }

    inner class getAllFileUris() : AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg urlOld: String): String{
            var holderJson = ""
            val url = URL(urlOld[0])
            val connection = url.openConnection() as HttpURLConnection
            if (connection.responseCode == 200) {
                var text = url.readText()
                holderJson = text
            } else {
                Log.d("FAILURE", "Connection failed")
            }
            return holderJson
        }

        override fun onPostExecute(result: String?){
            super.onPostExecute(result)
            if(result!=null){
                handleJson(result)
            }
        }

    }

    private fun handleJson(jsonString: String) {
        val jsonArray = JSONArray(jsonString)
        var item = 0
        var newItems = ArrayList<FileData>()
        Log.d("JSON", jsonArray.toString())
        while(item < jsonArray.length()){
            var currentFile = jsonArray.get(item).toString()
            var index = currentFile.lastIndexOf("/")
            var fileDataName = currentFile.substring(index+1)
            var fileUri = jsonArray.get(item).toString()
            val jsonObject = FileData(fileDataName, fileUri)
            this.items.add(jsonObject)
            newItems.add(jsonObject)
            item++
        }
        Log.d("JSON", this.items.toString())

    }


}