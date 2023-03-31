package com.teamwork.librarybox

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.*
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

//import kotlinx.android.synthetic.main.animal_list_item.view.*

//class MyMovieRecyclerViewAdapter(val items : ArrayList<MovieData>, val context: Context) :
class MyMovieRecyclerViewAdapter(val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MyMovieRecyclerViewAdapter.MovieViewHolder>() {

    val myDB = DatabaseHelper(context)
    var items: ArrayList<MovieData>

    init {

        myDB.initializeTables()
        items = myDB.getAllMovies()
    }

    var myListener: MyItemClickListener? = null
    var lastPosition = -1 // for animation
    // Adapter Listener!!!
    interface MyItemClickListener {
        fun onItemClickedFromAdapter(movie : MovieData)
        fun onItemLongClickedFromAdapter(position : Int)
        fun onOverflowMenuClickedFromAdapter(view: View, position: Int)
    }
    // This is for host activity or host fragment to call
    fun setMyItemClickListener ( listener: MyItemClickListener){
        this.myListener = listener
    }

    var posterTable:MutableMap<String, Int> = mutableMapOf()

    // MUST DO!!
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context) // p0 is parent
        val view : View

        // p1 -> View Type, check getItemViewType function!!

        view = layoutInflater.inflate(R.layout.card_view_item, p0, false)

        return MovieViewHolder(view)

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

        return items.size
    }

    // MUST DO!!
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = items[position]

        val url = "https://image.tmdb.org/t/p/w342/" + movie.poster_path!!
        Picasso.get().load(url).into(holder.moviePoster)


        //holder.moviePoster.setImageResource(posterTable[movie.title]!!)
        holder.movieTitle.text = movie.title
        holder.rating.rating = movie!!.vote_average?.toFloat()?.div(2) ?: 8.0.toFloat()
        var length = movie.overview!!.length
        length = if (length > 100) 100 else length
        holder.movieOverview.text = movie.overview.substring(0, length - 1) + " ..."
        holder.movieSelect.isChecked = movie.checked!!

        setAnimation(holder.moviePoster, position)
    }

    override fun getItemViewType(position: Int): Int {
        //return position % 6
        return position
    }

    fun findFirst(query : String): Int {
        var i = -1
        for( i in items.indices){
            if(items[i].title == query){
                return i
            }
        }
        return i
    }


    fun getItem(index: Int) : Any{
        return items[index]
    }

    fun setClearAll() {
        for( i in items.indices){
            items[i].checked = false
        }
        notifyDataSetChanged()
    }

    fun setSelectAll() {
        for( i in items.indices){
            items[i].checked = true
        }
        notifyDataSetChanged()
    }

    fun addMovies() {
        for( i in items.indices ) {
            var movie = items[i].copy()
            if(movie.checked!!){
                items[i].checked = false
                movie.checked = false
                movie.title = "Copy of " + movie.title
                items.add(i+1, movie)
                notifyItemInserted(i+1)
            }
        }
    }

    fun deleteMovies() {
        var cnt = 0
        for(i in 0 until items.size)
            if(items[i].checked!!)
                cnt += 1

        for(i in 0 until cnt){
            for(j in items.indices){
                if(items[j].checked!!){
                    items.removeAt(j)
                    notifyItemRemoved(j)
                    break
                }
            }
        }
        notifyDataSetChanged()
    }

    fun deleteMovie(position: Int){
        var name = items[position].db_id
        items.removeAt(position)
        myDB.deleteMovie(name)
    }

    fun addMovie(position: Int){
        items.add(position + 1, items[position])
        myDB.addMovie(items[position])
    }
    fun sortMovies() {

        items.sortWith(object: Comparator<MovieData>{
            override fun compare(o1: MovieData?, o2: MovieData?): Int {
                return o1!!.title!!.compareTo(o2!!.title!!,  true)
            }
        })

        notifyDataSetChanged()
    }

    fun sortedByRating() {
        items.sortWith(object: Comparator<MovieData>{
            override fun compare(o1: MovieData?, o2: MovieData?): Int {
                return ((o2!!.vote_average!! - o1!!.vote_average!!) * 101.0).toInt()
            }
        })

        notifyDataSetChanged()
    }


    inner class MovieViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
        val moviePoster = view.findViewById<ImageView>(R.id.poster)
        val movieTitle = view.findViewById<TextView>(R.id.movieTitle)
        val movieOverview = view.findViewById<TextView>(R.id.overview)
        val movieSelect = view.findViewById<CheckBox>(R.id.rvChx)
        val rating = view.findViewById<RatingBar>(R.id.ratingBar)
        //val overflow = view.findViewById<ImageView>(R.id.overflow)
        init{
            movieSelect.setOnCheckedChangeListener { buttonView, isChecked ->
                items[adapterPosition].checked = isChecked
            }
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


}

