package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category

class CategoriesRecyclerViewAdapter(private var categories : List<Category>) : RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_category, parent, false))

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        when (category) {
            Category.BASICS -> {
                holder.icon.setImageResource(R.drawable.ic_star_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.basics)
            }
            Category.HOUSE -> {
                holder.icon.setImageResource(R.drawable.ic_home_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.house)
            }
            Category.CLOTHES -> {
                holder.icon.setImageResource(R.drawable.ic_clothes_yellow_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.clothes)
            }
            Category.FAMILY -> {
                holder.icon.setImageResource(R.drawable.ic_people_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.family)
            }
            Category.CHARACTER -> {
                holder.icon.setImageResource(R.drawable.ic_person_pin_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.character)
            }
            Category.COMPUTER -> {
                holder.icon.setImageResource(R.drawable.ic_computer_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.computer)
            }
            Category.ANIMALS -> {
                holder.icon.setImageResource(R.drawable.ic_pets_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.animals)
            }
            Category.TIME -> {
                holder.icon.setImageResource(R.drawable.ic_access_time_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.time)
            }
            Category.CITY -> {
                holder.icon.setImageResource(R.drawable.ic_location_city_black_24dp)
                holder.name.text = holder.itemView.resources.getString(R.string.city)
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var icon : ImageView
        var name : TextView

        init {
            super.itemView
            icon = itemView.findViewById(R.id.category_logo)
            name = itemView.findViewById(R.id.category_name)
        }
    }
}