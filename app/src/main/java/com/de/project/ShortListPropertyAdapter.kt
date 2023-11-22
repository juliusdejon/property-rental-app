package com.de.project
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.de.project.models.ShortListProperty


class ShortListPropertyAdapter(var items:List<ShortListProperty>,
   private val deleteBtnClickHandler: (Int) -> Unit
) : RecyclerView.Adapter<ShortListPropertyAdapter.PropertyViewHolder>(){
    inner class PropertyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        init {
            val btnDelete =
                itemView.findViewById<Button>(R.id.btnRemoveFromShortList)
            btnDelete.setOnClickListener {
                deleteBtnClickHandler(adapterPosition)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_short_list_property,parent,false)
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
        }
        else
        {
            tvAvailable.setText("Unavailable")
        }


    }
}