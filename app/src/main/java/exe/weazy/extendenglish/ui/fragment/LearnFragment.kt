package exe.weazy.extendenglish.ui.fragment

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
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.WordCardStackAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.model.Progress
import exe.weazy.extendenglish.model.Word
import exe.weazy.extendenglish.tools.StringHelper
import exe.weazy.extendenglish.tools.UiHelper
import kotlinx.android.synthetic.main.fragment_learn.*

class LearnFragment : Fragment(), CardStackListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var learnToday: ArrayList<Word>
    private lateinit var repeatYesterday: ArrayList<Word>
    private lateinit var repeatTwoDays: ArrayList<Word>
    private lateinit var repeatThreeDays: ArrayList<Word>
    private lateinit var repeatFourDays: ArrayList<Word>
    private lateinit var repeatLong: ArrayList<Word>
    private lateinit var learned : ArrayList<Word>
    private lateinit var allWords: ArrayList<Word>
    private lateinit var know : ArrayList<Word>
    private lateinit var newKnow : ArrayList<Word>

    private lateinit var learnedToRepeat : ArrayList<Word>
    private lateinit var again: ArrayList<Word>
    private lateinit var current: ArrayList<Word>
    private lateinit var currentWord: Word

    private lateinit var progress: Progress
    private lateinit var categories : ArrayList<Category>

    private var remain = 0
    private var toLearn = 0

    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private lateinit var adapter: WordCardStackAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn, null)
    }

    override fun onStart() {
        super.onStart()

        if (!::learnToday.isInitialized || learnToday.isNullOrEmpty()) {
            initializeWords()
            initializeUserInfo()
            initializeCardStackView()
        }
    }



    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                again.add(currentWord)
                remain--

                if (progress == Progress.LEARN_TODAY && toLearn < 7) {
                    toLearn++
                    learnedToRepeat.add(currentWord)
                }
            }

            Direction.Left -> {
                remain--

                if (progress == Progress.LEARN_TODAY && toLearn < 7) {
                    newKnow.add(currentWord)
                }
            }

            Direction.Bottom -> {
                remain--
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
     * Gets all words from bundle
     */
    private fun initializeWords() {
        allWords = arguments?.getParcelableArrayList<Word>("allWords")!!
        learned = arguments?.getParcelableArrayList<Word>("learned")!!
        know = arguments?.getParcelableArrayList<Word>("know")!!
        repeatFourDays = arguments?.getParcelableArrayList<Word>("repeatFourDays")!!
        repeatThreeDays = arguments?.getParcelableArrayList<Word>("repeatThreeDays")!!
        repeatTwoDays = arguments?.getParcelableArrayList<Word>("repeatTwoDays")!!
        repeatYesterday = arguments?.getParcelableArrayList<Word>("repeatYesterday")!!

        again = ArrayList()
        learnedToRepeat = ArrayList()
        current = ArrayList()
        newKnow = ArrayList()
    }


    /**
     * Gets progress level and categories
     */
    private fun initializeUserInfo() {
        progress = arguments?.getSerializable("progress")!! as Progress
        categories = arguments?.getSerializable("categories")!! as ArrayList<Category>
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

        word_card_stack.layoutManager = manager
        word_card_stack.itemAnimator.apply {
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
                word_card_stack.visibility = View.GONE
                layout_learned.visibility = View.VISIBLE
            }

            Progress.LEARN_TODAY -> {
                generateLearnTodayWords()
                current = ArrayList(learnToday)
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
                generateRepeatLongWords()
                current = ArrayList(repeatLong)
            }

        }

        initializeCardStackAdapter()
    }

    /**
     * Initialize WordCardStackAdapter first time
     */
    private fun initializeCardStackAdapter() {
        this.allWords.shuffle()
        val variants = this.allWords.filter { categories.contains(it.category) }

        adapter = WordCardStackAdapter(current, ArrayList(variants))
        word_card_stack.adapter = adapter

        remain = current.size
    }

    /**
     * Updates CardStackView data
     */
    private fun updateCardStack() {
        // If card stack are empty, but learned not 7 words, then generate new learn words
        if (progress == Progress.LEARN_TODAY && toLearn < 7 && remain == 0) {
            word_card_stack.visibility = View.GONE

            generateLearnTodayWords()
            setDataAndNotify(learnToday)

            UiHelper.showView(word_card_stack)
        }

        // If learned 7 words, let's repeat them
        if (toLearn == 7) {
            word_card_stack.visibility = View.GONE

            setDataAndNotify(learnedToRepeat)
            current = ArrayList(learnedToRepeat)
            toLearn++

            UiHelper.showView(word_card_stack)
        }

        // If day is repeated, come to next day
        if (remain == 0 && again.isEmpty()) {
            word_card_stack.visibility = View.GONE

            when (progress) {
                Progress.REPEAT_LONG -> {
                    progress = Progress.REPEAT_FOUR_DAYS
                    setDataAndNotify(repeatFourDays)
                }

                Progress.REPEAT_FOUR_DAYS -> {
                    writeWordsToFirestore(Progress.LEARNED, repeatFourDays)
                    progress = Progress.REPEAT_THREE_DAYS
                    setDataAndNotify(repeatThreeDays)
                }

                Progress.REPEAT_THREE_DAYS -> {
                    rewriteWordsToFirestore(Progress.REPEAT_FOUR_DAYS, repeatThreeDays)
                    progress = Progress.REPEAT_TWO_DAYS
                    setDataAndNotify(repeatTwoDays)
                }

                Progress.REPEAT_TWO_DAYS -> {
                    rewriteWordsToFirestore(Progress.REPEAT_THREE_DAYS, repeatTwoDays)
                    progress = Progress.REPEAT_YESTERDAY
                    writeProgressToFirestore(progress)
                    setDataAndNotify(repeatYesterday)
                }

                Progress.REPEAT_YESTERDAY -> {
                    rewriteWordsToFirestore(Progress.REPEAT_TWO_DAYS, repeatYesterday)
                    progress = Progress.LEARN_TODAY
                    generateLearnTodayWords()
                    setDataAndNotify(learnToday)
                }

                Progress.LEARN_TODAY -> {
                    // TODO: learn today allWords
                    rewriteWordsToFirestore(Progress.REPEAT_YESTERDAY, learnedToRepeat)
                    writeKnownToFirestore()
                    progress = Progress.LEARNED

                    UiHelper.hideView(word_card_stack)
                    UiHelper.showView(layout_learned)
                }
            }

            writeProgressToFirestore(progress)
            UiHelper.showView(word_card_stack)
        }

        if (again.isNotEmpty() && remain == 0) {
            word_card_stack.visibility = View.GONE
            setDataAndNotify(again)
            UiHelper.showView(word_card_stack)
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
        word_card_stack.swipe()
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



    private fun rewriteWordsToFirestore(p : Progress, words: ArrayList<Word>) {
        var index = 0
        val collection = StringHelper.upperSnakeToLowerCamel(p.name)

        words.forEach {
            firestore.document("users/${user?.uid}/$collection/word-${index++}").set(it)
        }
    }

    private fun writeWordsToFirestore(p : Progress, words: ArrayList<Word>) {
        var index = words.size
        val collection = StringHelper.upperSnakeToLowerCamel(p.name)

        words.forEach {
            firestore.document("users/${user?.uid}/$collection/word-${index++}").set(it)
        }
    }

    private fun writeProgressToFirestore(p : Progress) {
        firestore.document("users/${user?.uid}").update("progress", StringHelper.upperSnakeToLowerCamel(p.name))
    }

    private fun writeKnownToFirestore() {
        var index = know.size

        newKnow.forEach {
            firestore.document("users/${user?.uid}/know/word-${index++}").set(it)
        }
    }
}