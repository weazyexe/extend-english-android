package exe.weazy.extendenglish.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.WordCardStackAdapter
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import kotlinx.android.synthetic.main.card_word.*
import kotlinx.android.synthetic.main.fragment_learn.*

class LearnFragment : Fragment(), CardStackListener {

    private lateinit var learnToday : ArrayList<LearnWord>
    private lateinit var repeatYesterday : ArrayList<LearnWord>
    private lateinit var repeatTwoDays : ArrayList<LearnWord>
    private lateinit var repeatThreeDays : ArrayList<LearnWord>
    private lateinit var repeatFourDays : ArrayList<LearnWord>
    private lateinit var repeatLong : ArrayList<LearnWord>
    private lateinit var words : ArrayList<LearnWord>

    private lateinit var progress : LearnProgress

    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private lateinit var adapter : WordCardStackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_learn, null)
    }

    override fun onStart() {
        super.onStart()

        words = arguments?.getParcelableArrayList<LearnWord>("words")!!
        repeatLong = arguments?.getParcelableArrayList<LearnWord>("repeatLong")!!
        repeatFourDays = arguments?.getParcelableArrayList<LearnWord>("repeatFourDays")!!
        repeatThreeDays = arguments?.getParcelableArrayList<LearnWord>("repeatThreeDays")!!
        repeatTwoDays = arguments?.getParcelableArrayList<LearnWord>("repeatTwoDays")!!
        repeatYesterday = arguments?.getParcelableArrayList<LearnWord>("repeatYesterday")!!

        progress = arguments?.getSerializable("progress")!! as LearnProgress

        initializeCardStack()

        word_card_stack.layoutManager = manager
        setCardStackByProgress()
        word_card_stack.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        /*val wordText = manager.topView.findViewById<TextView>(R.id.word_english_text).text
        val word = repeatWords.find { it.word == wordText }

        if (word != null) {
            when (direction) {
                Direction.Left -> {
                    if (isRepeat) {
                        doneWords.add(word)
                        //repeatWords.remove(word)
                    }
                }

                Direction.Right -> {
                    if (isRepeat) {
                        failedWords.add(word)
                        //repeatWords.remove(word)
                    }
                }

                Direction.Bottom -> {
                    if (isRepeat) {
                        repeatWords.remove(word)
                    } else {
                        learnWords.remove(word)
                    }
                }
            }
        }*/
    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
        //initializeSwipeButtons()
        val writeButton = view?.findViewById<Button>(R.id.write_word_button)
        val showButton = view?.findViewById<Button>(R.id.show_word_button)
        val chooseButton = view?.findViewById<Button>(R.id.choose_word_button)

        when (progress) {

            LearnProgress.LEARN_TODAY -> {
                showWord(view)
            }

            else -> {
                showVariant(view)

                writeButton?.setOnClickListener {
                    showWrite(view)
                }

                showButton?.setOnClickListener {
                    showWord(view)
                }

                chooseButton?.setOnClickListener {
                    showChoose(view)
                }
            }
        }
    }

    override fun onCardRewound() {

    }

    private fun initializeCardStack() {
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
    }

    /*private fun initializeSwipeButtons() {
        val left = view?.findViewById<View>(R.id.swipe_left_button)
        left?.setOnClickListener {
            swipe(Direction.Right)
        }

        val right = view?.findViewById<View>(R.id.swipe_right_button)
        right?.setOnClickListener {
            swipe(Direction.Left)
        }

        val bottom = view?.findViewById<View>(R.id.swipe_bottom_button)
        bottom?.setOnClickListener {
            swipe(Direction.Bottom)
        }
    }*/

    private fun setCardStackByProgress() {
        when (progress) {

            LearnProgress.LEARNED -> {
                word_card_stack.visibility = View.GONE
                layout_learned.visibility = View.VISIBLE
            }

            LearnProgress.LEARN_TODAY -> {

            }

            LearnProgress.REPEAT_YESTERDAY -> {
                repeat(repeatYesterday)
            }

            LearnProgress.REPEAT_TWO_DAYS -> {
                repeat(repeatTwoDays)
            }

            LearnProgress.REPEAT_THREE_DAYS -> {
                repeat(repeatThreeDays)
            }

            LearnProgress.REPEAT_FOUR_DAYS -> {
                repeat(repeatFourDays)
            }

            LearnProgress.REPEAT_LONG -> {
                repeat(repeatLong)
            }

        }
    }

    private fun repeat(words : ArrayList<LearnWord>) {
        this.words.shuffle()
        val variants = this.words.subList(0, 200)
        adapter = WordCardStackAdapter(words, ArrayList(variants))
        word_card_stack.adapter = adapter
    }

    private fun swipe(direction : Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()

        manager.setSwipeAnimationSetting(setting)
        word_card_stack.swipe()
    }

    private fun showChoose(view : View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.VISIBLE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.GONE
    }

    private fun showWord(view : View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.VISIBLE
        layoutVariantWord?.visibility = View.GONE
    }

    private fun showVariant(view : View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.GONE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.VISIBLE
    }

    private fun showWrite(view : View?) {
        val layoutChooseWord = view?.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = view?.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = view?.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = view?.findViewById<View>(R.id.layout_variant)

        layoutChooseWord?.visibility = View.GONE
        layoutWriteWord?.visibility = View.VISIBLE
        layoutShowWord?.visibility = View.GONE
        layoutVariantWord?.visibility = View.GONE
    }
}