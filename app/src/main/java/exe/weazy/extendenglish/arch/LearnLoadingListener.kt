package exe.weazy.extendenglish.arch

import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word
import java.util.*
import kotlin.collections.ArrayList

interface LearnLoadingListener {
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