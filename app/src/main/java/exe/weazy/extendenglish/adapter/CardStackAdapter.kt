package exe.weazy.extendenglish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.CardWord
import kotlin.random.Random
import kotlin.random.nextInt

class CardStackAdapter(private var words : MutableList<CardWord>)
    : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_word, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(words[position])
    }


    fun setWords(words : MutableList<CardWord>) {
        this.words.clear()
        this.words.addAll(words)

        notifyDataSetChanged()
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var wordShow : TextView
        var wordChoose : TextView
        var wordWrite : TextView
        var wordVariant : TextView

        var translate : TextView

        var category : TextView

        var transcriptionShow : TextView
        var transcriptionChoose : TextView
        var transcriptionWrite : TextView
        var transcriptionVariant : TextView

        var choose1Button : Button
        var choose2Button : Button
        var choose3Button : Button
        var choose4Button : Button

        var layoutVariant : ViewGroup
        var layoutShow : ViewGroup
        var layoutWrite : ViewGroup
        var layoutChoose : ViewGroup

        init {
            super.itemView
            wordChoose = itemView.findViewById(R.id.word_choose_english_text)
            wordWrite = itemView.findViewById(R.id.word_write_english_text)
            wordShow = itemView.findViewById(R.id.word_show_english_text)
            wordVariant = itemView.findViewById(R.id.word_variant_english_text)

            transcriptionChoose = itemView.findViewById(R.id.transcription_choose_english_text)
            transcriptionWrite = itemView.findViewById(R.id.transcription_write_english_text)
            transcriptionShow = itemView.findViewById(R.id.transcription_show_english_text)
            transcriptionVariant = itemView.findViewById(R.id.transcription_variant_english_text)

            translate = itemView.findViewById(R.id.word_show_translate_text)

            category = itemView.findViewById(R.id.category_text)

            choose1Button = itemView.findViewById(R.id.choose_one_button)
            choose2Button = itemView.findViewById(R.id.choose_two_button)
            choose3Button = itemView.findViewById(R.id.choose_three_button)
            choose4Button = itemView.findViewById(R.id.choose_four_button)

            layoutVariant = itemView.findViewById(R.id.layout_variant)
            layoutWrite = itemView.findViewById(R.id.layout_write_word)
            layoutChoose = itemView.findViewById(R.id.layout_choose_word)
            layoutShow = itemView.findViewById(R.id.layout_show_word)
        }

        fun bind(cardWord : CardWord) {
            setVariantsOnHolder(cardWord)
            setWordOnHolder(cardWord)
        }

        private fun setVariantsOnHolder(cardWord: CardWord) {
            // FIXME: word/translate issue
            when (Random.nextInt(0..3)) {
                0 -> {
                    choose1Button.text = cardWord.word.translate
                    choose2Button.text = cardWord.variants[1].translate
                    choose3Button.text = cardWord.variants[2].translate
                    choose4Button.text = cardWord.variants[3].translate
                }

                1 -> {
                    choose1Button.text = cardWord.variants[0].translate
                    choose2Button.text = cardWord.word.translate
                    choose3Button.text = cardWord.variants[2].translate
                    choose4Button.text = cardWord.variants[3].translate
                }

                2 -> {
                    choose1Button.text = cardWord.variants[0].translate
                    choose2Button.text = cardWord.variants[1].translate
                    choose3Button.text = cardWord.word.translate
                    choose4Button.text = cardWord.variants[3].translate
                }

                3 -> {
                    choose1Button.text = cardWord.variants[0].translate
                    choose2Button.text = cardWord.variants[1].translate
                    choose3Button.text = cardWord.variants[2].translate
                    choose4Button.text = cardWord.word.translate
                }
            }
        }

        private fun setWordOnHolder(cardWord : CardWord) {
            layoutShow.visibility = View.GONE
            layoutChoose.visibility = View.GONE
            layoutWrite.visibility = View.GONE
            layoutVariant.visibility = View.GONE

            wordShow.text = cardWord.word.word
            wordWrite.text = cardWord.word.word
            wordVariant.text = cardWord.word.word
            wordChoose.text = cardWord.word.word

            transcriptionShow.text = cardWord.word.transcription
            transcriptionWrite.text = cardWord.word.transcription
            transcriptionVariant.text = cardWord.word.transcription
            transcriptionChoose.text = cardWord.word.transcription

            translate.text = cardWord.word.translate
            category.text = cardWord.word.category.name
        }
    }
}