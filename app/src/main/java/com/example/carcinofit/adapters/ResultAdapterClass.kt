package com.example.carcinofit.adapters

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.carcinofit.R
import com.example.carcinofit.database.models.DummyApiModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext


class ResultAdapterClass(private val myContext: LifecycleOwner, var title:Array<String>): RecyclerView.Adapter<ResultAdapterClass.MyViewHolder>() {

        //var title = arrayOf("Acetaldehyde", "Caffeine", "Diethylene glycol", "Acetamide", "Ketoprofen")

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
            init {// this is similar to constructor of the class , The code inside the init block is the first to be executed when the class is instantiated.
                titleText = itemView.findViewById(R.id.titleText)
            }
        }
    }
