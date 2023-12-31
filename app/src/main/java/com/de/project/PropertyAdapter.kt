package com.de.project

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.de.project.models.Property
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.InvocationHandler

class PropertyAdapter(var items:List<Property>,private val rowClickHandler: (Int) -> Unit) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>(){
    inner class PropertyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        init {
            itemView.setOnClickListener{
                rowClickHandler(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_property,parent,false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = items.get(position)

        val tvAddress = holder.itemView.findViewById<TextView>(R.id.tvAddress)
        val tvCity = holder.itemView.findViewById<TextView>(R.id.tvCity)
        val tvType = holder.itemView.findViewById<TextView>(R.id.tvType)
        val tvAvailable = holder.itemView.findViewById<TextView>(R.id.tvAvailability)

        tvAddress.setText(property.address)
        tvCity.setText(property.city)
        tvType.setText(property.type)
        if (property.available)
        {
            tvAvailable.setText("Available")
            tvAvailable.setTextColor(Color.rgb(1,100,32))
        }
        else
        {
            tvAvailable.setText("Unavailable")
            tvAvailable.setTextColor(Color.rgb(255,0,0))
        }


    }
}