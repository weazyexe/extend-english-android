package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.LearnWord
import kotlin.random.Random
import kotlin.random.nextInt

class WordCardStackAdapter(private var words : ArrayList<LearnWord>, private var variants : ArrayList<LearnWord>) :
    RecyclerView.Adapter<WordCardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_word, parent, false))

    override fun getItemCount() = words.size

    fun getWords() = words

    fun setWords(words: ArrayList<LearnWord>) {
        this.words = words
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val w = words[position]

        setWordOnHolder(w, holder)
        setVariantsOnHolder(w, holder)
    }



    private fun setWordOnHolder(w : LearnWord, holder : ViewHolder) {
        holder.layoutShow.visibility = View.GONE
        holder.layoutChoose.visibility = View.GONE
        holder.layoutWrite.visibility = View.GONE
        holder.layoutVariant.visibility = View.VISIBLE

        holder.wordShow.text = w.word
        holder.wordWrite.text = w.word
        holder.wordVariant.text = w.word
        holder.wordChoose.text = w.word
        holder.translate.text = w.translate
    }

    private fun setVariantsOnHolder(w : LearnWord, holder : ViewHolder) {
        val variants = getRandomVariants(w)

        // FIXME: word/translate issue
        val random = Random.nextInt(0..3)
        when (random) {
            0 -> {
                holder.choose1Button.text = w.translate
                holder.choose2Button.text = variants[1].translate
                holder.choose3Button.text = variants[2].translate
                holder.choose4Button.text = variants[3].translate
            }

            1 -> {
                holder.choose1Button.text = variants[0].translate
                holder.choose2Button.text = w.translate
                holder.choose3Button.text = variants[2].translate
                holder.choose4Button.text = variants[3].translate
            }

            2 -> {
                holder.choose1Button.text = variants[0].translate
                holder.choose2Button.text = variants[1].translate
                holder.choose3Button.text = w.translate
                holder.choose4Button.text = variants[3].translate
            }

            3 -> {
                holder.choose1Button.text = variants[0].translate
                holder.choose2Button.text = variants[1].translate
                holder.choose3Button.text = variants[2].translate
                holder.choose4Button.text = w.translate
            }
        }
    }

    private fun getRandomVariants(w : LearnWord) : ArrayList<LearnWord> {
        var list = variants
        list.shuffle()
        return list.filter { it.word != w.word && it.category == w.category } as ArrayList<LearnWord>
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

        var layoutVariant : FrameLayout
        var layoutShow : FrameLayout
        var layoutWrite : FrameLayout
        var layoutChoose : FrameLayout

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

            layoutVariant = itemView.findViewById(R.id.layout_variant)
            layoutWrite = itemView.findViewById(R.id.layout_write_word)
            layoutChoose = itemView.findViewById(R.id.layout_choose_word)
            layoutShow = itemView.findViewById(R.id.layout_show_word)
        }
    }
}