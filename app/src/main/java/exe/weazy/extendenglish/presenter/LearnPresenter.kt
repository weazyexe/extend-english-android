package exe.weazy.extendenglish.presenter

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.view.View
import com.yuyakaido.android.cardstackview.Direction
import exe.weazy.extendenglish.arch.LearnContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word
import exe.weazy.extendenglish.model.LearnModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class LearnPresenter : LearnContract.Presenter, LearnContract.LoadingListener {

    private lateinit var view : LearnContract.View
    private val model = LearnModel(this)

    private val LIMIT_TIME = 86_400_000

    private lateinit var learnToday: ArrayList<Word>
    private lateinit var repeatYesterday: ArrayList<Word>
    private lateinit var repeatTwoDays: ArrayList<Word>
    private lateinit var repeatThreeDays: ArrayList<Word>
    private lateinit var repeatFourDays: ArrayList<Word>
    private lateinit var repeatLong: ArrayList<Word>
    private lateinit var learned : ArrayList<Word>
    private lateinit var allWords: ArrayList<Word>
    private lateinit var know : ArrayList<Word>

    private lateinit var progress: Progress
    private lateinit var categories : ArrayList<Category>
    private lateinit var lastActivity : Date

    private var newKnow = ArrayList<Word>()
    private var learnedToRepeat = ArrayList<Word>()
    private var again = ArrayList<Word>()
    private var current = ArrayList<Word>()
    private var currentWord = Word()

    private var remain = 0
    private var toLearn = 0

    private var isAllWordsLoaded = false
    private var isCategoriesLoaded = false
    private var isRepeatYesterdayLoaded = false
    private var isRepeatTwoDaysLoaded = false
    private var isRepeatThreeDaysLoaded = false
    private var isRepeatFourDaysLoaded = false
    private var isLearnedLoaded = false
    private var isKnowLoaded = false
    private var isProgressLoaded = false
    private var isLastActivityLoaded = false



    override fun attach(view: LearnContract.View) {
        this.view = view
    }

    override fun cardSwiped(direction: Direction) {
        remain--

        when (direction) {
            Direction.Right -> {
                again.add(currentWord)

                if (progress == Progress.LEARN_TODAY && toLearn < 7) {
                    toLearn++
                    learnedToRepeat.add(currentWord)
                }
            }

            Direction.Left -> {
                if (progress == Progress.LEARN_TODAY && toLearn < 7) {
                    newKnow.add(currentWord)
                }
            }

            Direction.Bottom -> {

            }
        }

        updateCardStack()
    }

    override fun cardAppeared(position : Int, cardView: View?) {
        currentWord = current[position]

        if (cardView != null) {
            when (progress) {
                Progress.LEARN_TODAY -> {
                    // If learning right now, show word
                    // Else show variants to repeat
                    if (toLearn < 7) {
                        view.showWordCard(cardView)
                    } else {
                        view.showVariantCard(cardView)
                    }
                }

                else -> {
                    view.showVariantCard(cardView)
                }
            }
        }
    }

    override fun checkWord(word: String, cardView: View) {
        val translate = currentWord.translate
        val words = translate.split(", ")

        if (words.contains(word)) {
            view.showWordCard(cardView)
        } else {
            if (translate == word) {
                // Right answer handle
                view.showWordCard(cardView)
            } else {
                // Wrong answer handle
                view.showWrongAnswerToast()
            }
        }
    }

    override fun helpWord(word: String, cardView: View) {
        // TODO: did it, ya?
    }

    override fun setCardStackByProgress() {
        when (progress) {

            Progress.LEARNED -> {
                view.showEnd()
            }

            Progress.LEARN_TODAY -> {
                generateLearnTodayWords()
                view.updateCardStack(learnToday)
                current = ArrayList(learnToday)
                view.showCardStack()
            }

            Progress.REPEAT_YESTERDAY -> {
                current = ArrayList(repeatYesterday)
                view.showCardStack()
            }

            Progress.REPEAT_TWO_DAYS -> {
                current = ArrayList(repeatTwoDays)
                view.showCardStack()
            }

            Progress.REPEAT_THREE_DAYS -> {
                current = ArrayList(repeatThreeDays)
                view.showCardStack()
            }

            Progress.REPEAT_FOUR_DAYS -> {
                current = ArrayList(repeatFourDays)
                view.showCardStack()
            }

            Progress.REPEAT_LONG -> {
                generateRepeatLongWords()
                view.updateCardStack(repeatLong)
                current = ArrayList(repeatLong)
                view.showCardStack()
            }

        }

        this.allWords.shuffle()
        val variants = this.allWords.filter { categories.contains(it.category) } as ArrayList
        view.initializeCardStackAdapter(current, variants)
        remain = current.size
    }

    override fun getAllData(file : File) {
        view.showLoading()
        preLoad()

        model.loadAllWords(file)
        model.loadCategories()
        model.loadKnowWords()
        model.loadLastActivity()
        model.loadLearnedWords()
        model.loadRepeatFourDaysWords()
        model.loadRepeatThreeDaysWords()
        model.loadRepeatTwoDaysWords()
        model.loadRepeatYesterdayWords()
        model.loadProgress()
    }

    override fun onLoadAllWordsFinished(words: ArrayList<Word>) {
        isAllWordsLoaded = true
        allWords = words
        afterLoad()
    }

    override fun onLoadAllWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadKnowWordsFinished(words: ArrayList<Word>) {
        isKnowLoaded = true
        know = words
        afterLoad()
    }

    override fun onLoadKnowWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadLearnedWordsFinished(words: ArrayList<Word>) {
        isLearnedLoaded = true
        learned = words
        afterLoad()
    }

    override fun onLoadLearnedWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadRepeatYesterdayWordsFinished(words: ArrayList<Word>) {
        isRepeatYesterdayLoaded = true
        repeatYesterday = words
        afterLoad()
    }

    override fun onLoadRepeatYesterdayWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadRepeatTwoDaysWordsFinished(words: ArrayList<Word>) {
        isRepeatTwoDaysLoaded = true
        repeatTwoDays = words
        afterLoad()
    }

    override fun onLoadRepeatTwoDaysWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadRepeatThreeDaysWordsFinished(words: ArrayList<Word>) {
        isRepeatThreeDaysLoaded = true
        repeatThreeDays = words
        afterLoad()
    }

    override fun onLoadRepeatThreeDaysWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadRepeatFourDaysWordsFinished(words: ArrayList<Word>) {
        isRepeatFourDaysLoaded = true
        repeatFourDays = words
        afterLoad()
    }

    override fun onLoadRepeatFourDaysWordsFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadProgressFinished(progress : Progress) {
        isProgressLoaded = true
        this.progress = progress
        afterLoad()
    }

    override fun onLoadProgressFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadCategoriesFinished(categories : ArrayList<Category>) {
        isCategoriesLoaded = true
        this.categories = categories
        afterLoad()
    }

    override fun onLoadCategoriesFailure(exception : Exception?) {
        view.showError()
    }

    override fun onLoadLastActivityFinished(lastActivity : Date) {
        isLastActivityLoaded = true
        this.lastActivity = lastActivity
        afterLoad()
    }

    override fun onLoadLastActivityFailure(exception : Exception?) {
        view.showError()
    }




    private fun updateCardStack() {
        // If card stack are empty, but learned not 7 words, then generate new learn words
        if (progress == Progress.LEARN_TODAY && toLearn < 7 && remain == 0) {
            view.showLoading()

            generateLearnTodayWords()
            setCurrentWordList(learnToday)

            view.updateCardStack(learnToday)
            view.showCardStack()
        } else if (toLearn == 7) {  // If learned 7 words, let's repeat them
            view.showLoading()

            view.updateCardStack(learnedToRepeat)
            setCurrentWordList(learnedToRepeat)

            toLearn++
            view.showCardStack()
        } else if (remain == 0 && again.isEmpty()) { // If day is repeated, come to next day
            view.showLoading()

            comeNextDay()

            model.writeProgress(progress)
            view.showCardStack()
        }

        if (again.isNotEmpty() && remain == 0) {
            view.showLoading()
            view.updateCardStack(again)
            setCurrentWordList(again)
            view.showCardStack()
        }
    }

    private fun generateLearnTodayWords() {
        val repeatWords = learned + repeatFourDays + repeatThreeDays + repeatTwoDays + repeatYesterday
        var index = 0

        val toLearnWordList = allWords.filter { categories.contains(it.category) } as ArrayList<Word>
        toLearnWordList.shuffle()

        learnToday = ArrayList()
        while (learnToday.size < 7) {
            val word = toLearnWordList[index++]
            if (!repeatWords.contains(word) && !learnedToRepeat.contains(word)) {
                learnToday.add(word)
            }
        }
    }

    private fun comeNextDay() {
        when (progress) {
            Progress.REPEAT_LONG -> {
                progress = Progress.REPEAT_FOUR_DAYS

                view.updateCardStack(repeatFourDays)
                setCurrentWordList(repeatFourDays)
            }

            Progress.REPEAT_FOUR_DAYS -> {
                model.writeLearned(repeatFourDays, learned.size)

                progress = Progress.REPEAT_THREE_DAYS

                view.updateCardStack(repeatThreeDays)
                setCurrentWordList(repeatThreeDays)
            }

            Progress.REPEAT_THREE_DAYS -> {
                model.writeWordsByProgress(repeatThreeDays, Progress.REPEAT_FOUR_DAYS)

                progress = Progress.REPEAT_TWO_DAYS

                view.updateCardStack(repeatTwoDays)
                setCurrentWordList(repeatTwoDays)
            }

            Progress.REPEAT_TWO_DAYS -> {
                model.writeWordsByProgress(repeatTwoDays, Progress.REPEAT_THREE_DAYS)

                progress = Progress.REPEAT_YESTERDAY

                view.updateCardStack(repeatYesterday)
                setCurrentWordList(repeatYesterday)
            }

            Progress.REPEAT_YESTERDAY -> {
                model.writeWordsByProgress(repeatYesterday, Progress.REPEAT_TWO_DAYS)

                progress = Progress.LEARN_TODAY

                generateLearnTodayWords()
                view.updateCardStack(learnToday)
                setCurrentWordList(learnToday)
            }

            Progress.LEARN_TODAY -> {
                // TODO: learn today allWords
                model.writeWordsByProgress(learnedToRepeat, Progress.REPEAT_YESTERDAY)
                model.writeKnown(newKnow, know.size)

                progress = Progress.LEARNED

                view.showEnd()
            }
            else -> {
                view.showError()
            }
        }
    }

    private fun generateRepeatLongWords() {
        learned.shuffle()

        repeatLong = ArrayList()

        if (learned.size > 7) {
            for (i in 0..6) {
                repeatLong.add(learned[i])
            }
        } else {
            learned.forEach {
                repeatLong.add(it)
            }
        }
    }

    private fun afterLoad() {
        if (isAllWordsLoaded && isLearnedLoaded && isKnowLoaded && isProgressLoaded &&
            isCategoriesLoaded && isRepeatFourDaysLoaded && isRepeatThreeDaysLoaded &&
            isRepeatTwoDaysLoaded && isRepeatYesterdayLoaded && isLastActivityLoaded) {

            val now = Calendar.getInstance(TimeZone.GMT_ZONE).time
            val diff = now.time - lastActivity.time

            if (diff > LIMIT_TIME && progress == Progress.LEARNED) {
                progress = if (!repeatFourDays.isNullOrEmpty()) {
                    Progress.REPEAT_FOUR_DAYS
                } else if (!repeatThreeDays.isNullOrEmpty()) {
                    Progress.REPEAT_THREE_DAYS
                } else if (!repeatTwoDays.isNullOrEmpty()) {
                    Progress.REPEAT_TWO_DAYS
                } else if (!repeatYesterday.isNullOrEmpty()) {
                    Progress.REPEAT_YESTERDAY
                } else if (progress != Progress.LEARN_TODAY) {
                    Progress.REPEAT_LONG
                } else {
                    Progress.LEARN_TODAY
                }
            }

            if (diff > LIMIT_TIME && progress == Progress.LEARNED) {
                view.showError()
            } else if (progress != Progress.LEARNED) {
                view.setupCardStack()
            } else {
                view.showEnd()
            }
        }
    }

    private fun preLoad() {
        isAllWordsLoaded = false
        isCategoriesLoaded = false
        isRepeatYesterdayLoaded = false
        isRepeatTwoDaysLoaded = false
        isRepeatThreeDaysLoaded = false
        isRepeatFourDaysLoaded = false
        isLearnedLoaded = false
        isKnowLoaded = false
        isProgressLoaded = false
        isLastActivityLoaded = false
    }

    private fun setCurrentWordList(words : ArrayList<Word>) {
        current = ArrayList(words)
        again = ArrayList()
        remain = current.size
    }
}