package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.LearnWord
import kotlin.random.Random
import kotlin.random.nextInt

class WordCardStackAdapter(private val words : ArrayList<LearnWord>, private var variants : ArrayList<LearnWord>) :
    RecyclerView.Adapter<WordCardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_word, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val w = words[position]

        holder.wordShow.text = w.word
        holder.wordWrite.text = w.word
        holder.wordVariant.text = w.word
        holder.wordChoose.text = w.word
        holder.translate.text = w.translate


        var list = variants
        list.shuffle()
        list = list.filter { it.word != w.word && it.category == w.category } as ArrayList<LearnWord>

        val random = Random.nextInt(0..3)
        when (random) {
            0 -> {
                holder.choose1Button.text = w.translate
                holder.choose2Button.text = list[1].translate
                holder.choose3Button.text = list[2].translate
                holder.choose4Button.text = list[3].translate
            }

            1 -> {
                holder.choose1Button.text = list[0].translate
                holder.choose2Button.text = w.translate
                holder.choose3Button.text = list[2].translate
                holder.choose4Button.text = list[3].translate
            }

            2 -> {
                holder.choose1Button.text = list[0].translate
                holder.choose2Button.text = list[1].translate
                holder.choose3Button.text = w.translate
                holder.choose4Button.text = list[3].translate
            }

            3 -> {
                holder.choose1Button.text = list[0].translate
                holder.choose2Button.text = list[1].translate
                holder.choose3Button.text = list[2].translate
                holder.choose4Button.text = w.translate
            }
        }

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var wordShow : TextView
        var wordChoose : TextView
        var wordWrite : TextView
        var wordVariant : TextView
        var translate : TextView
        var choose1Button : Button
        var choose2Button : Button
        var choose3Button : Button
        var choose4Button : Button

        init {
            super.itemView
            wordChoose = itemView.findViewById(R.id.word_choose_english_text)
            wordWrite = itemView.findViewById(R.id.word_write_english_text)
            wordShow = itemView.findViewById(R.id.word_show_english_text)
            wordVariant = itemView.findViewById(R.id.word_variant_english_text)

            translate = itemView.findViewById(R.id.word_show_translate_text)

            choose1Button = itemView.findViewById(R.id.choose_one_button)
            choose2Button = itemView.findViewById(R.id.choose_two_button)
            choose3Button = itemView.findViewById(R.id.choose_three_button)
            choose4Button = itemView.findViewById(R.id.choose_four_button)
        }
    }
}