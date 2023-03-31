package com.teamwork.librarybox

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.with
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.with
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_result_card.view.*
import java.net.URI


class ListAdapter(private val context: Context, private val files: List<ListModel>
   // val listModel: MutableList<ListModel>
    ) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.fileTextName)
        var fileTypeImage: ImageView = itemView.findViewById(R.id.fileImage)

        fun bindFile(listModel: ListModel) {
            val stringName = listModel.fileName.toString()
            val index = stringName.lastIndexOf("/")
            val nameOfFile = stringName.substring(index + 1)
            val fileType = nameOfFile.substring(nameOfFile.lastIndexOf(".") + 1).uppercase()
            Log.d("LOL", fileType)
            tvTitle.text = nameOfFile
            if (fileType == "PDF") {
                fileTypeImage.setImageResource(R.drawable.pdffile)
            }
            if (fileType == "TXT") {
                fileTypeImage.setImageResource(R.drawable.textfile)
            }
            if (fileType == "JPG") {
                fileTypeImage.setImageResource(R.drawable.jpgfile)
            }
            if (fileType == "PNG") {
                fileTypeImage.setImageResource(R.drawable.pngfile)
            }
        }

        fun bindView(listModel: ListModel) {

            val stringName = listModel.fileName.toString()
            val index = stringName.lastIndexOf("/")
            val nameOfFile = stringName.substring(index + 1)
            val fileType = nameOfFile.substring(nameOfFile.lastIndexOf(".") + 1).uppercase()
            Log.d("LOL", fileType)
            tvTitle.text = nameOfFile
            if (fileType == "PDF") {
                fileTypeImage.setImageResource(R.drawable.pdffile)
            }
            if (fileType == "TXT") {
                fileTypeImage.setImageResource(R.drawable.textfile)
            }
            if (fileType == "JPG") {
                fileTypeImage.setImageResource(R.drawable.jpgfile)
            }
            if (fileType == "PNG") {
                fileTypeImage.setImageResource(R.drawable.pngfile)
            }

        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            return ListViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_result_card, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val file = files[position]

            holder.bindFile(files[position])
        }

        override fun getItemCount(): Int = files.size

}

/*class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById(R.id.fileTextName)
     var fileTypeImage: ImageView = itemView.findViewById(R.id.fileImage)

    fun bindFile(file: ListModel){

    }
    fun bindView(listModel: ListModel){

        val stringName = listModel.fileName.toString()
        val index = stringName.lastIndexOf("/")
        val nameOfFile = stringName.substring(index+1)
        val fileType = nameOfFile.substring(nameOfFile.lastIndexOf(".")+1).uppercase()
        Log.d("LOL", fileType)
        tvTitle.text = nameOfFile
        if(fileType == "PDF")
        {
            fileTypeImage.setImageResource(R.drawable.pdffile)
        }
        if(fileType == "TXT"){
            fileTypeImage.setImageResource(R.drawable.textfile)
        }
        if(fileType == "JPG"){
            //Picasso.with(context).load(stringName).into(fileTypeImage)
            Log.d("LOL", stringName)
            Glide.with(itemView).load(stringName).into(fileTypeImage.fileImage)
            //Picasso.get().load(stringName).into(fileTypeImage)
        }

}



}*/
