package exe.weazy.extendenglish.adapter

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.CardWord
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class CardStackAdapter(private var words : MutableList<CardWord>)
    : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private lateinit var tts : TextToSpeech

    private var isEng = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_word, parent, false))

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        isEng = Random.nextBoolean()

        holder.bind(words[position])

        holder.soundButton.setOnClickListener {
            if (!::tts.isInitialized) {
                val context = holder.category.context
                tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.SUCCESS) {
                        tts.language = Locale.UK
                        tts.speak(words[position].word.word, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                })
            } else {
                tts.speak(words[position].word.word, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
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

        var soundButton : ImageButton

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

            soundButton = itemView.findViewById(R.id.button_sound)

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
            when (Random.nextInt(0..3)) {
                0 -> {
                    if (isEng) {
                        choose1Button.text = cardWord.word.translate
                        choose2Button.text = cardWord.variants[1].translate
                        choose3Button.text = cardWord.variants[2].translate
                        choose4Button.text = cardWord.variants[3].translate
                    } else {
                        choose1Button.text = cardWord.word.word
                        choose2Button.text = cardWord.variants[1].word
                        choose3Button.text = cardWord.variants[2].word
                        choose4Button.text = cardWord.variants[3].word
                    }
                }

                1 -> {
                    if (isEng) {
                        choose1Button.text = cardWord.variants[0].translate
                        choose2Button.text = cardWord.word.translate
                        choose3Button.text = cardWord.variants[2].translate
                        choose4Button.text = cardWord.variants[3].translate
                    } else {
                        choose1Button.text = cardWord.variants[0].word
                        choose2Button.text = cardWord.word.word
                        choose3Button.text = cardWord.variants[2].word
                        choose4Button.text = cardWord.variants[3].word
                    }

                }

                2 -> {
                    if (isEng) {
                        choose1Button.text = cardWord.variants[0].translate
                        choose2Button.text = cardWord.variants[1].translate
                        choose3Button.text = cardWord.word.translate
                        choose4Button.text = cardWord.variants[3].translate
                    } else {
                        choose1Button.text = cardWord.variants[0].word
                        choose2Button.text = cardWord.variants[1].word
                        choose3Button.text = cardWord.word.word
                        choose4Button.text = cardWord.variants[3].word
                    }

                }

                3 -> {
                    if (isEng) {
                        choose1Button.text = cardWord.variants[0].translate
                        choose2Button.text = cardWord.variants[1].translate
                        choose3Button.text = cardWord.variants[2].translate
                        choose4Button.text = cardWord.word.translate

                        soundButton.visibility = View.VISIBLE
                    } else {
                        choose1Button.text = cardWord.variants[0].word
                        choose2Button.text = cardWord.variants[1].word
                        choose3Button.text = cardWord.variants[2].word
                        choose4Button.text = cardWord.word.word
                    }

                }
            }
        }

        private fun setWordOnHolder(cardWord : CardWord) {
            layoutShow.visibility = View.GONE
            layoutChoose.visibility = View.GONE
            layoutWrite.visibility = View.GONE
            layoutVariant.visibility = View.GONE

            if (isEng) {
                wordWrite.text = cardWord.word.word
                wordVariant.text = cardWord.word.word
                wordChoose.text = cardWord.word.word

                transcriptionWrite.text = cardWord.word.transcription
                transcriptionVariant.text = cardWord.word.transcription
                transcriptionChoose.text = cardWord.word.transcription
            } else {
                wordWrite.text = cardWord.word.translate
                wordVariant.text = cardWord.word.translate
                wordChoose.text = cardWord.word.translate

                transcriptionWrite.text = ""
                transcriptionVariant.text = ""
                transcriptionChoose.text = ""
            }

            wordShow.text = cardWord.word.word
            translate.text = cardWord.word.translate

            transcriptionShow.text = cardWord.word.transcription
            category.text = cardWord.word.category.name
        }
    }
}