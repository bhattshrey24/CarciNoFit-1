package com.example.carcinofit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carcinofit.R


class ResultAdapterClass: RecyclerView.Adapter<ResultAdapterClass.MyViewHolder>() {
        var title = arrayOf("Acetaldehyde", "Caffeine", "Diethylene glycol", "Acetamide", "Ketoprofen")
        // var subTitle = arrayOf("subtit1", "subtit2", "subtit3", "subtit4", "subtit5", "subtit6", "subtit7", "subtit8", "subtit9", "subtit10")



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultAdapterClass.MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.result_recycler_item, parent, false)// simply telling recycler view our list item layout
            return MyViewHolder(view) // bascially passing the list item layout to holder so that it knows how the 'view object' that it will hold will look like
        }

        override fun onBindViewHolder(holder: ResultAdapterClass.MyViewHolder, position: Int) {// giving the data to the list item view which will then be converted in view object and given to the viewholder

            // the position is given to us by recycler view , its basically telling konsa konsa object show hora on the screen

            holder.titleText.text = title[position]
            // holder.subTitle.text = subTitle[position]
        }

        override fun getItemCount(): Int {
            return title.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleText: TextView
            // var subTitle: TextView

            init {// this is similar to constructor of the class , The code inside the init block is the first to be executed when the class is instantiated.
                titleText = itemView.findViewById(R.id.titleText)
                //subTitle = itemView.findViewById(R.id.subTitleText)
            }
        }
    }
