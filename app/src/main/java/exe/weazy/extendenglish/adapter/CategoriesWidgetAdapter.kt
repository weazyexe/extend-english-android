package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.tools.StringHelper

class CategoriesWidgetAdapter(private var categories : List<Category>, private var mOnClickListener: View.OnClickListener?) : RecyclerView.Adapter<CategoriesWidgetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.element_category_widget, parent, false)
        view.setOnClickListener(mOnClickListener)
        return ViewHolder(view)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        val text = StringHelper.upperSnakeToUpperCamel(category.name)

        holder.name.text = text
        holder.backName.text = text
    }

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var name : TextView
        var backName : TextView

        init {
            super.itemView
            name = itemView.findViewById(R.id.text_category)
            backName = itemView.findViewById(R.id.text_category_back)
        }
    }
}