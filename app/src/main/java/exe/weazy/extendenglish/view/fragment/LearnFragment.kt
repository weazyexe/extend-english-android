package exe.weazy.extendenglish.view.fragment

import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CardStackAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.model.Progress
import exe.weazy.extendenglish.model.Word
import exe.weazy.extendenglish.tools.FirebaseHelper
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_learn.*
import java.io.File
import java.util.Date
import kotlin.collections.ArrayList

class LearnFragment : Fragment(), CardStackListener {

    private val firestore = FirebaseFirestore.getInstance()

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

    private var newKnow = ArrayList<Word>()
    private var learnedToRepeat = ArrayList<Word>()
    private var again = ArrayList<Word>()
    private var current = ArrayList<Word>()
    private var currentWord = Word()

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


    private lateinit var progress: Progress
    private lateinit var categories : ArrayList<Category>
    private lateinit var lastActivity : Date

    private var remain = 0
    private var toLearn = 0

    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private lateinit var adapter: CardStackAdapter


    private lateinit var viewModel : MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        initializeObservers()
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            cardstack_words.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn, null)
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
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

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

        // FIXME: word/translate issue
        currentWord = current[position]
        val writeButton = view?.findViewById<Button>(R.id.write_word_button)
        val showButton = view?.findViewById<Button>(R.id.show_word_button)
        val chooseButton = view?.findViewById<Button>(R.id.choose_word_button)

        when (progress) {
            Progress.LEARN_TODAY -> {
                // If learning right now, show word
                // Else show variants to repeat
                if (toLearn < 7) {
                    showWord(view)
                } else {
                    showVariant(view)
                    initializeVariantListeners(view, writeButton, showButton, chooseButton)
                }
            }

            else -> {
                showVariant(view)
                initializeVariantListeners(view, writeButton, showButton, chooseButton)
            }
        }
    }

    override fun onCardRewound() {

    }



    /**
     * Initialize CardStackView: adapter, manager, animation settings and words
     */
    private fun initializeCardStackView() {
        manager.apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(listOf(Direction.Bottom, Direction.Right, Direction.Left))
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }

        cardstack_words.layoutManager = manager
        cardstack_words.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        setCardStackByProgress()
    }



    /**
     * Initialize writeButton, showButton and chooseButton listeners on view
     */
    private fun initializeVariantListeners(view : View?, writeButton : Button?, showButton : Button?, chooseButton : Button?) {
        if (view != null) {
            initializeWriteButton(view, writeButton)
            initializeShowButton(view, showButton)
            initializeChooseButton(view, chooseButton)
        }
    }

    /**
     * Initialize chooseButton
     */
    private fun initializeChooseButton(view : View, chooseButton: Button?) {
        chooseButton?.setOnClickListener {
            showChoose(view)

            val button1 = view.findViewById<Button>(R.id.choose_one_button)
            val button2 = view.findViewById<Button>(R.id.choose_two_button)
            val button3 = view.findViewById<Button>(R.id.choose_three_button)
            val button4 = view.findViewById<Button>(R.id.choose_four_button)

            button1.setOnClickListener {
                checkWord(button1.text.toString(), currentWord.translate, view)
            }

            button2.setOnClickListener {
                checkWord(button2.text.toString(), currentWord.translate, view)
            }

            button3.setOnClickListener {
                checkWord(button3.text.toString(), currentWord.translate, view)
            }

            button4.setOnClickListener {
                checkWord(button4.text.toString(), currentWord.translate, view)
            }
        }
    }

    /**
     * Initialize showButton
     */
    private fun initializeShowButton(view : View, showButton: Button?) {
        showButton?.setOnClickListener {
            showWord(view)
        }
    }

    /**
     * Initialize writeButton
     */
    private fun initializeWriteButton(view: View, writeButton: Button?) {
        writeButton?.setOnClickListener {
            showWrite(view)

            val submitButton = view.findViewById<Button>(R.id.submit_word_button)
            val helpButton = view.findViewById<Button>(R.id.help_word_button)
            val written = view.findViewById<EditText>(R.id.word_written_edit_text)

            submitButton?.setOnClickListener {
                UiHelper.hideKeyboard(view, context)
                checkWord(written?.text.toString(), currentWord.translate, view)
                written?.setText("", TextView.BufferType.EDITABLE)
            }

            helpButton?.setOnClickListener {
                helpWord(written?.text.toString(), currentWord.translate, written)
            }
        }
    }



    /**
     * Set words on CardStackView by progress
     */
    private fun setCardStackByProgress() {
        when (progress) {

            Progress.LEARNED -> {
                cardstack_words.visibility = View.GONE
                layout_learned.visibility = View.VISIBLE
            }

            Progress.LEARN_TODAY -> {
                val liveData = viewModel.getLearnToday()
                liveData.observe(this, Observer {
                    learnToday = it
                    if (learnToday.isNullOrEmpty()) {
                        generateLearnTodayWords()
                    }
                    current = ArrayList(learnToday)
                    initializeCardStackAdapter()
                })

            }

            Progress.REPEAT_YESTERDAY -> {
                current = ArrayList(repeatYesterday)
            }

            Progress.REPEAT_TWO_DAYS -> {
                current = ArrayList(repeatTwoDays)
            }

            Progress.REPEAT_THREE_DAYS -> {
                current = ArrayList(repeatThreeDays)
            }

            Progress.REPEAT_FOUR_DAYS -> {
                current = ArrayList(repeatFourDays)
            }

            Progress.REPEAT_LONG -> {
                val liveData = viewModel.getRepeatLong()
                liveData.observe(this, Observer {
                    repeatLong = it
                    if (repeatLong.isNullOrEmpty()) {
                        generateRepeatLongWords()
                    }
                    current = ArrayList(repeatLong)
                    initializeCardStackAdapter()
                })

            }

        }

        initializeCardStackAdapter()
    }

    /**
     * Initialize CardStackAdapter first time
     */
    private fun initializeCardStackAdapter() {
        this.allWords.shuffle()
        val variants = this.allWords.filter { categories.contains(it.category) }

        adapter = CardStackAdapter(current, ArrayList(variants))
        cardstack_words.adapter = adapter

        remain = current.size
    }

    /**
     * Updates CardStackView data
     */
    private fun updateCardStack() {
        // If card stack are empty, but learned not 7 words, then generate new learn words
        if (progress == Progress.LEARN_TODAY && toLearn < 7 && remain == 0) {
            cardstack_words.visibility = View.GONE

            generateLearnTodayWords()
            setDataAndNotify(learnToday)

            UiHelper.showView(cardstack_words)
        }

        // If learned 7 words, let's repeat them
        if (toLearn == 7) {
            cardstack_words.visibility = View.GONE

            setDataAndNotify(learnedToRepeat)
            current = ArrayList(learnedToRepeat)
            toLearn++

            UiHelper.showView(cardstack_words)
        }

        // If day is repeated, come to next day
        if (remain == 0 && again.isEmpty()) {
            cardstack_words.visibility = View.GONE

            when (progress) {
                Progress.REPEAT_LONG -> {
                    progress = Progress.REPEAT_FOUR_DAYS

                    setDataAndNotify(repeatFourDays)
                }

                Progress.REPEAT_FOUR_DAYS -> {
                    FirebaseHelper.writeWordsByProgress(firestore, repeatFourDays, Progress.LEARNED)
                    progress = Progress.REPEAT_THREE_DAYS

                    setDataAndNotify(repeatThreeDays)
                }

                Progress.REPEAT_THREE_DAYS -> {
                    FirebaseHelper.rewriteWordsByProgress(firestore, repeatThreeDays, Progress.REPEAT_FOUR_DAYS)
                    progress = Progress.REPEAT_TWO_DAYS

                    setDataAndNotify(repeatTwoDays)
                }

                Progress.REPEAT_TWO_DAYS -> {
                    FirebaseHelper.rewriteWordsByProgress(firestore, repeatTwoDays, Progress.REPEAT_THREE_DAYS)
                    progress = Progress.REPEAT_YESTERDAY

                    setDataAndNotify(repeatYesterday)
                }

                Progress.REPEAT_YESTERDAY -> {
                    FirebaseHelper.rewriteWordsByProgress(firestore, repeatYesterday, Progress.REPEAT_TWO_DAYS)
                    progress = Progress.LEARN_TODAY

                    generateLearnTodayWords()
                    setDataAndNotify(learnToday)
                }

                Progress.LEARN_TODAY -> {
                    // TODO: learn today allWords
                    FirebaseHelper.rewriteWordsByProgress(firestore, learnedToRepeat, Progress.REPEAT_YESTERDAY)
                    FirebaseHelper.writeKnown(firestore, newKnow, know.size)
                    progress = Progress.LEARNED

                    UiHelper.hideView(cardstack_words)
                    UiHelper.showView(layout_learned)
                }
                else -> {

                }
            }

            FirebaseHelper.writeProgress(firestore, progress)
            UiHelper.showView(cardstack_words)
        }

        if (again.isNotEmpty() && remain == 0) {
            cardstack_words.visibility = View.GONE
            setDataAndNotify(again)
            UiHelper.showView(cardstack_words)
        }
    }

    /**
     * Set words ArrayList and notify changes to adapter
     */
    private fun setDataAndNotify(words : ArrayList<Word>) {
        current = ArrayList(words)
        adapter.words = words
        adapter.notifyDataSetChanged()
        again = ArrayList()
        remain = current.size
    }

    /**
     * Swipe card to direction
     */
    private fun swipe(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()

        manager.setSwipeAnimationSetting(setting)
        cardstack_words.swipe()
    }

    /**
     * Generates learnToday words
     */
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

        viewModel.setLearnToday(learnToday)
    }

    /**
     * Generates repeatLong words
     */
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

        viewModel.setRepeatLong(repeatLong)
    }



    /**
     * Show choose layout
     */
    private fun showChoose(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutChooseWord)
    }

    /**
     * Show show word layout
     */
    private fun showWord(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutShowWord)
    }

    /**
     * Show variants layout
     */
    private fun showVariant(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutVariantWord)
    }

    /**
     * Show write layout
     */
    private fun showWrite(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.showView(layoutWriteWord)
    }



    /**
     * Word validation check
     */
    private fun checkWord(chosen: String, word: String, view: View?) {
        val words = word.split(", ")

        if (words.contains(chosen)) {
            showWord(view)
        } else {
            if (chosen == word) {
                // Right answer handle
                showWord(view)
            } else {
                // Wrong answer handle
                Toast.makeText(activity?.applicationContext, getString(R.string.wrong_answer), Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * TODO: didn't work, fix
     * Help to type word
     */
    private fun helpWord(written: String, word: String, editText: EditText) {
        val words = word.split(", ")

        if (words.contains(written)) {
            showWord(editText.rootView)
        } else {
            words.forEach {
                var count = 0

                for (i in 0..(written.length - 1)) {
                    if (written[i] == it[i]) {
                        count++
                    } else {
                        return@forEach
                    }
                }

                if (count == it.length) {
                    // Right answer handle
                    showWord(editText.rootView)
                    return
                } else {
                    // Wrong answer handle
                    editText.setText(it.substring(0..count), TextView.BufferType.EDITABLE)
                    editText.setSelection(written.length + 1)
                    return
                }
            }
        }
    }









    private fun initializeObservers() {
        initializeAllWordsObserver()
        initializeRepeatYesterdayWordsObserver()
        initializeRepeatTwoDaysWordsObserver()
        initializeRepeatThreeDaysWordsObserver()
        initializeRepeatFourDaysWordsObserver()
        initializeLearnedWordsObserver()
        initializeKnowWordsObserver()
        initializeCategoriesObserver()
        initializeProgressObserver()
        initializeLastActivityObserver()
    }

    private fun initializeAllWordsObserver() {
        val file = File(activity?.applicationContext?.filesDir, "allWords")
        if (file.exists()) {
            val type = object : TypeToken<ArrayList<Word>>() {}.type
            allWords = Gson().fromJson(file.readText(), type)

            viewModel.setAllWords(allWords)
        }

        val allWordsLiveData = viewModel.getAllWords()
        allWordsLiveData.observe(this, Observer {
            allWords = it

            file.writeText(Gson().toJson(allWords))

            isAllWordsLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatYesterdayWordsObserver() {
        val repeatYesterdayWordsLiveData = viewModel.getRepeatYesterdayWords()
        repeatYesterdayWordsLiveData.observe(this, Observer {
            repeatYesterday = it
            isRepeatYesterdayLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatTwoDaysWordsObserver() {
        val repeatTwoDaysWordsLiveData = viewModel.getRepeatTwoDaysWords()
        repeatTwoDaysWordsLiveData.observe(this, Observer {
            repeatTwoDays = it
            isRepeatTwoDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatThreeDaysWordsObserver() {
        val repeatThreeDaysWordsLiveData = viewModel.getRepeatThreeDaysWords()
        repeatThreeDaysWordsLiveData.observe(this, Observer {
            repeatThreeDays = it
            isRepeatThreeDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatFourDaysWordsObserver() {
        val repeatFourDaysWordsLiveData = viewModel.getRepeatFourDaysWords()
        repeatFourDaysWordsLiveData.observe(this, Observer {
            repeatFourDays = it
            isRepeatFourDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeLearnedWordsObserver() {
        val learnedLiveData = viewModel.getLearnedWords()
        learnedLiveData.observe(this, Observer {
            learned = it
            isLearnedLoaded = true
            afterLoad()
        })
    }

    private fun initializeKnowWordsObserver() {
        val knowLiveData = viewModel.getKnowWords()
        knowLiveData.observe(this, Observer {
            know = it
            isKnowLoaded = true
            afterLoad()
        })
    }

    private fun initializeProgressObserver() {
        val progressLiveData = viewModel.getProgress()
        progressLiveData.observe(this, Observer {
            progress = it
            isProgressLoaded = true
            afterLoad()
        })
    }

    private fun initializeCategoriesObserver() {
        val categoriesLiveData = viewModel.getCategories()
        categoriesLiveData.observe(this, Observer {
            categories = it
            isCategoriesLoaded = true
            afterLoad()
        })
    }

    private fun initializeLastActivityObserver() {
        val lastActivityData = viewModel.getLastActivity()
        lastActivityData.observe(this, Observer {
            lastActivity = it
            isLastActivityLoaded = true
            afterLoad()
        })
    }


    private fun afterLoad() {
        if (isAllWordsLoaded && isLearnedLoaded && isKnowLoaded && isProgressLoaded &&
            isCategoriesLoaded && isRepeatFourDaysLoaded && isLearnedLoaded &&
            isRepeatThreeDaysLoaded && isRepeatTwoDaysLoaded && isRepeatYesterdayLoaded && isLastActivityLoaded) {

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

            initializeCardStackView()

            UiHelper.hideView(progressbar_learn)
            UiHelper.showView(cardstack_words)
        }
    }
}