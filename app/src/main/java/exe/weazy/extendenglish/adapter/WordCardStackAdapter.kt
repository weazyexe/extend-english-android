package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.LearnWord

class WordCardStackAdapter(private val words : List<LearnWord>) : RecyclerView.Adapter<WordCardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_word, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = words[position]

        holder.word.text = card.word
        holder.translate.text = card.translate
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var word : TextView
        var translate : TextView

        init {
            super.itemView
            word = itemView.findViewById(R.id.word_english_text)
            translate = itemView.findViewById(R.id.word_translate_text)
        }
    }
}