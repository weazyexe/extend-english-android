package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.tools.StringHelper
import java.util.ArrayList

class CategoriesAdapter(private var categories : List<Category>, private var checks : ArrayList<Boolean> = ArrayList()) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_category, parent, false))

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (checks.isEmpty()) {
            categories.forEach { _ ->
                checks.add(false)
            }
        }

        val category = categories[position]

        holder.name.text = StringHelper.upperSnakeToUpperCamel(category.name)

        if (!checks[position]) {
            holder.checked.visibility = View.GONE
        } else {
            holder.checked.visibility = View.VISIBLE
        }

        holder.layout.setOnClickListener {
            if (checks[position]) {
                checks[position] = false
                holder.checked.visibility = View.GONE
            } else {
                checks[position] = true
                holder.checked.visibility = View.VISIBLE
            }
        }
    }

    fun getChecks() = checks

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var checked : ImageView
        var name : TextView
        var layout : FrameLayout

        init {
            super.itemView
            checked = itemView.findViewById(R.id.image_checked)
            name = itemView.findViewById(R.id.text_category)
            layout = itemView.findViewById(R.id.layout_category)
        }
    }
}