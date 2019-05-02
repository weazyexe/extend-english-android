package exe.weazy.extendenglish.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.WordCardStackAdapter
import exe.weazy.extendenglish.entity.LearnWord

class LearnFragment : Fragment(), CardStackListener {

    private lateinit var learnToday : ArrayList<LearnWord>
    private lateinit var repeatYesterday : ArrayList<LearnWord>
    private lateinit var repeatTwoDays : ArrayList<LearnWord>
    private lateinit var repeatThreeDays : ArrayList<LearnWord>
    private lateinit var repeatFourDays : ArrayList<LearnWord>
    private lateinit var repeatLong : ArrayList<LearnWord>

    private var isLearnToday = false
    private var isRepeatYesterday = false
    private var isRepeatTwoDays = false
    private var isRepeatThreeDays = false
    private var isRepeatFourDays = false
    private var isRepeatLongTimeAgo = false

    private val stack by lazy { view?.findViewById<CardStackView>(R.id.word_card_stack) }
    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private lateinit var adapter : WordCardStackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val words = arguments?.getParcelableArrayList<LearnWord>("words")

        adapter = if (words != null) WordCardStackAdapter(words) else WordCardStackAdapter(ArrayList())

        initialize()

        return inflater.inflate(R.layout.fragment_learn, null)
    }

    override fun onStart() {
        super.onStart()

        stack?.layoutManager = manager
        stack?.adapter = adapter
        stack?.itemAnimator.apply {
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

    }

    override fun onCardRewound() {

    }

    private fun initialize() {
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
}