package exe.weazy.extendenglish.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.WordCardStackAdapter
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import kotlinx.android.synthetic.main.fragment_learn.*


class LearnFragment : Fragment(), CardStackListener {

    private lateinit var learnToday: ArrayList<LearnWord>
    private lateinit var repeatYesterday: ArrayList<LearnWord>
    private lateinit var repeatTwoDays: ArrayList<LearnWord>
    private lateinit var repeatThreeDays: ArrayList<LearnWord>
    private lateinit var repeatFourDays: ArrayList<LearnWord>
    private lateinit var repeatLong: ArrayList<LearnWord>
    private lateinit var allWords: ArrayList<LearnWord>

    private lateinit var again: ArrayList<LearnWord>
    private lateinit var current: ArrayList<LearnWord>
    private lateinit var currentWord: LearnWord

    private lateinit var progress: LearnProgress
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

        initializeWords()
        initializeUserInfo()
        initializeCardStackView()
    }



    override fun onCardDisappeared(view: View?, position: Int) {
        currentWord = current[position]
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                again.add(currentWord)
                remain--

                if (progress == LearnProgress.LEARN_TODAY) {
                    toLearn++
                }
            }

            Direction.Left -> {
                remain--
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
        val writeButton = view?.findViewById<Button>(R.id.write_word_button)
        val showButton = view?.findViewById<Button>(R.id.show_word_button)
        val chooseButton = view?.findViewById<Button>(R.id.choose_word_button)

        when (progress) {

            LearnProgress.LEARN_TODAY -> {
                //showWord(view)
            }

            else -> {
                //showVariant(view)

                writeButton?.setOnClickListener {
                    showWrite(view)

                    val submitButton = view.findViewById<Button>(R.id.submit_word_button)
                    val helpButton = view.findViewById<Button>(R.id.help_word_button)
                    val written = view.findViewById<EditText>(R.id.word_written_edit_text)

                    submitButton.setOnClickListener {
                        hideKeyboard(view)
                        checkWord(written.text.toString(), currentWord.translate, view)
                        written.setText("", TextView.BufferType.EDITABLE)
                    }

                    helpButton.setOnClickListener {
                        helpWord(written.text.toString(), currentWord.translate, written)
                    }
                }

                showButton?.setOnClickListener {
                    showWord(view)
                }

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
        }
    }

    override fun onCardRewound() {

    }



    private fun initializeWords() {
        allWords = arguments?.getParcelableArrayList<LearnWord>("allWords")!!
        repeatLong = arguments?.getParcelableArrayList<LearnWord>("repeatLong")!!
        repeatFourDays = arguments?.getParcelableArrayList<LearnWord>("repeatFourDays")!!
        repeatThreeDays = arguments?.getParcelableArrayList<LearnWord>("repeatThreeDays")!!
        repeatTwoDays = arguments?.getParcelableArrayList<LearnWord>("repeatTwoDays")!!
        repeatYesterday = arguments?.getParcelableArrayList<LearnWord>("repeatYesterday")!!
        again = ArrayList()
    }

    private fun initializeUserInfo() {
        progress = arguments?.getSerializable("progress")!! as LearnProgress
        categories = arguments?.getSerializable("categories")!! as ArrayList<Category>
    }

    private fun initializeCardStackView() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(listOf(Direction.Bottom, Direction.Right, Direction.Left))
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())

        word_card_stack.layoutManager = manager
        word_card_stack.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

        setCardStackByProgress()
    }



    private fun setCardStackByProgress() {
        when (progress) {

            LearnProgress.LEARNED -> {
                word_card_stack.visibility = View.GONE
                layout_learned.visibility = View.VISIBLE
            }

            LearnProgress.LEARN_TODAY -> {
                generateLearnTodayWords()
                current = ArrayList(learnToday)
            }

            LearnProgress.REPEAT_YESTERDAY -> {
                current = ArrayList(repeatYesterday)
            }

            LearnProgress.REPEAT_TWO_DAYS -> {
                current = ArrayList(repeatTwoDays)
            }

            LearnProgress.REPEAT_THREE_DAYS -> {
                current = ArrayList(repeatThreeDays)
            }

            LearnProgress.REPEAT_FOUR_DAYS -> {
                current = ArrayList(repeatFourDays)
            }

            LearnProgress.REPEAT_LONG -> {
                current = ArrayList(repeatLong)
            }

        }

        initializeCardStackAdapter()
    }

    private fun initializeCardStackAdapter() {
        this.allWords.shuffle()
        val variants = this.allWords.subList(0, 150)

        adapter = WordCardStackAdapter(current, ArrayList(variants), progress)
        word_card_stack.adapter = adapter

        remain = current.size
    }

    private fun updateCardStack() {
        if (progress == LearnProgress.LEARN_TODAY && toLearn < 7 && remain == 0) {
            generateLearnTodayWords()
            setDataAndNotify(learnToday)
        }

        if (toLearn == 7) {
            setDataAndNotify(again)
            toLearn++
        }

        if (remain == 0 && again.isEmpty()) {
            when (progress) {
                LearnProgress.REPEAT_LONG -> {
                    progress = LearnProgress.REPEAT_FOUR_DAYS
                    setDataAndNotify(repeatFourDays)
                }

                LearnProgress.REPEAT_FOUR_DAYS -> {
                    progress = LearnProgress.REPEAT_THREE_DAYS
                    setDataAndNotify(repeatThreeDays)
                }

                LearnProgress.REPEAT_THREE_DAYS -> {
                    progress = LearnProgress.REPEAT_TWO_DAYS
                    setDataAndNotify(repeatTwoDays)
                }

                LearnProgress.REPEAT_TWO_DAYS -> {
                    progress = LearnProgress.REPEAT_YESTERDAY
                    setDataAndNotify(repeatYesterday)
                }

                LearnProgress.REPEAT_YESTERDAY -> {
                    progress = LearnProgress.LEARN_TODAY
                    generateLearnTodayWords()
                    setDataAndNotify(learnToday)
                }

                LearnProgress.LEARN_TODAY -> {
                    // TODO: learn today allWords
                    progress = LearnProgress.LEARNED

                    word_card_stack.visibility = View.GONE
                    layout_learned.visibility = View.VISIBLE
                }
            }
        }

        if (again.isNotEmpty() && remain == 0) {
            setDataAndNotify(again)
        }
    }

    private fun setDataAndNotify(words : ArrayList<LearnWord>) {
        current = ArrayList(words)
        adapter.words = words
        adapter.notifyDataSetChanged()
        again = ArrayList()
        remain = current.size
    }

    private fun swipe(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()

        manager.setSwipeAnimationSetting(setting)
        word_card_stack.swipe()
    }

    private fun generateLearnTodayWords() {
        val repeatWords = repeatLong + repeatFourDays + repeatThreeDays + repeatTwoDays + repeatYesterday
        var index = 0

        val toLearnWordList = allWords.filter { categories.contains(it.category) } as ArrayList<LearnWord>
        toLearnWordList.shuffle()

        learnToday = ArrayList()
        while (learnToday.size < 7) {
            val word = toLearnWordList[index++]
            if (!repeatWords.contains(word) && !again.contains(word)) {
                learnToday.add(word)
            }
        }
    }



    private fun showChoose(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.VISIBLE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.GONE
    }

    private fun showWord(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.VISIBLE
        layoutVariantWord?.visibility = View.GONE
    }

    private fun showVariant(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.VISIBLE
    }

    private fun showWrite(view: View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.VISIBLE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.GONE
    }



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

    private fun helpWord(written: String, word: String, editText: EditText) {

        // FIXME: help to type word
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



    private fun hideKeyboard(view: View?) {
        val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}