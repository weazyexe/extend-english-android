package exe.weazy.extendenglish.arch

import com.yuyakaido.android.cardstackview.Direction
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word
import java.io.File
import java.util.*

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
        fun attach(view : View)
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

    interface LoadingListener {
        fun onLoadAllWordsFinished(words : ArrayList<Word>)
        fun onLoadAllWordsFailure(exception : Exception?)

        fun onLoadKnowWordsFinished(words : ArrayList<Word>)
        fun onLoadKnowWordsFailure(exception : Exception?)

        fun onLoadLearnedWordsFinished(words : ArrayList<Word>)
        fun onLoadLearnedWordsFailure(exception : Exception?)

        fun onLoadRepeatYesterdayWordsFinished(words : ArrayList<Word>)
        fun onLoadRepeatYesterdayWordsFailure(exception : Exception?)

        fun onLoadRepeatTwoDaysWordsFinished(words : ArrayList<Word>)
        fun onLoadRepeatTwoDaysWordsFailure(exception : Exception?)

        fun onLoadRepeatThreeDaysWordsFinished(words : ArrayList<Word>)
        fun onLoadRepeatThreeDaysWordsFailure(exception : Exception?)

        fun onLoadRepeatFourDaysWordsFinished(words : ArrayList<Word>)
        fun onLoadRepeatFourDaysWordsFailure(exception : Exception?)

        fun onLoadProgressFinished(progress : Progress)
        fun onLoadProgressFailure(exception : Exception?)

        fun onLoadCategoriesFinished(categories : ArrayList<Category>)
        fun onLoadCategoriesFailure(exception : Exception?)

        fun onLoadLastActivityFinished(lastActivity : Date)
        fun onLoadLastActivityFailure(exception : Exception?)
    }
}