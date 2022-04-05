package com.example.bandana

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var recyclerArray: ArrayList<Phone> =ArrayList()):
    RecyclerView.Adapter<Adapter.AdapterHolder>() {
        class AdapterHolder(view: View): RecyclerView.ViewHolder(view) {
            var name:TextView = view.findViewById(R.id.name)
            var phoneno:TextView = view.findViewById(R.id.phoneno)
       }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.phonecall,parent,false)
        return AdapterHolder(itemView)

    }

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        holder.name.setText("${recyclerArray.get(position).name}")
        holder.phoneno.setText("${recyclerArray.get(position).phoneno}")

    }

    override fun getItemCount(): Int {
        return recyclerArray.size
    }

}