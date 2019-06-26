package exe.weazy.extendenglish.arch

import android.view.View
import com.yuyakaido.android.cardstackview.Direction
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word
import java.io.File

interface LearnContract {

    interface View {
        fun updateCardStack(words : ArrayList<Word>)
        fun showLoading()
        fun showEnd()
        fun showError()
        fun showCardStack()
        fun showWrongAnswerToast()
        fun showWordCard(cardView : android.view.View)
        fun showVariantCard(cardView : android.view.View)
        fun showWriteCard(cardView : android.view.View)
        fun showChooseCard(cardView: android.view.View)
        fun initializeCardStackAdapter(words : ArrayList<Word>, variants : ArrayList<Word>)
        fun setupCardStack()
    }

    interface Presenter {
        fun cardSwiped(direction : Direction)
        fun cardAppeared(position : Int, cardView : android.view.View?)
        fun checkWord(word : String, cardView : android.view.View)
        fun helpWord(word : String, cardView : android.view.View)
        fun getAllData(file : File)
        fun setCardStackByProgress()
    }

    interface Model {
        fun loadAllWords(file : File)
        fun loadKnowWords()
        fun loadLearnedWords()
        fun loadRepeatYesterdayWords()
        fun loadRepeatTwoDaysWords()
        fun loadRepeatThreeDaysWords()
        fun loadRepeatFourDaysWords()
        fun loadProgress()
        fun loadCategories()
        fun loadLastActivity()

        fun writeLearned(words: ArrayList<Word>, learnedCount: Int)
        fun writeWordsByProgress(words: ArrayList<Word>, p : Progress)
        fun writeProgress(p : Progress)
        fun writeKnown(newKnow : ArrayList<Word>, knowCount : Int)
    }
}