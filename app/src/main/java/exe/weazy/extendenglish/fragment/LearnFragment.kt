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
import exe.weazy.extendenglish.entity.Category
import java.util.Arrays.asList

class LearnFragment : Fragment(), CardStackListener {

    lateinit var words : List<LearnWord>

    private val stack by lazy { view?.findViewById<CardStackView>(R.id.word_card_stack) }
    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private val adapter by lazy { WordCardStackAdapter(words) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        words = asList(
            LearnWord("word", "слово", Category.BASICS),
            LearnWord("hello", "привет", Category.BASICS),
            LearnWord("sequence", "последовательность", Category.COMPUTER),
            LearnWord("cat", "кот", Category.ANIMALS),
            LearnWord("крышка", "lid", Category.HOUSE))

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
        manager.setDirections(arrayListOf(Direction.Bottom, Direction.Right, Direction.Left))
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
    }
}