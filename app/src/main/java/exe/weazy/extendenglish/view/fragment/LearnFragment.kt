package exe.weazy.extendenglish.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CardStackAdapter
import exe.weazy.extendenglish.arch.LearnContract
import exe.weazy.extendenglish.entity.Word
import exe.weazy.extendenglish.presenter.LearnPresenter
import exe.weazy.extendenglish.tools.UiHelper
import kotlinx.android.synthetic.main.fragment_learn.*
import java.io.File

class LearnFragment : Fragment(), CardStackListener, LearnContract.View {

    private lateinit var presenter : LearnPresenter

    private val manager by lazy { CardStackLayoutManager(activity?.applicationContext, this) }
    private lateinit var adapter: CardStackAdapter



    override fun onAttach(context: Context) {
        super.onAttach(context)

        presenter = LearnPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        if (::adapter.isInitialized) {
            adapter = CardStackAdapter(ArrayList(), ArrayList())
            cardstack_words.adapter = adapter
        }

        val file = File(activity?.applicationContext?.filesDir, "allWords")
        presenter.getAllData(file)

        fab_update.setOnClickListener {
            presenter.getAllData(file)
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
        if (direction != null) {
            presenter.cardSwiped(direction)
        }
    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

        // FIXME: word/translate issue
        presenter.cardAppeared(position, view)
    }

    override fun onCardRewound() {

    }




    override fun updateCardStack(words: ArrayList<Word>) {
        adapter.setWords(words)
    }

    override fun showLoading() {
        cardstack_words.visibility = View.GONE
        progressbar_learn.visibility = View.VISIBLE
        layout_learned.visibility = View.GONE
        layout_error.visibility = View.GONE
    }

    override fun showEnd() {
        cardstack_words.visibility = View.GONE
        progressbar_learn.visibility = View.GONE
        layout_learned.visibility = View.VISIBLE
        layout_error.visibility = View.GONE
    }

    override fun showError() {
        cardstack_words.visibility = View.GONE
        progressbar_learn.visibility = View.GONE
        layout_learned.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
    }

    override fun showCardStack() {
        cardstack_words.visibility = View.VISIBLE
        progressbar_learn.visibility = View.GONE
        layout_learned.visibility = View.GONE
        layout_error.visibility = View.GONE
    }

    override fun showWrongAnswerToast() {
        Toast.makeText(activity?.applicationContext, getString(R.string.wrong_answer), Toast.LENGTH_LONG).show()
    }

    override fun showWordCard(cardView : View) {
        val layoutChooseWord = cardView.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = cardView.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = cardView.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = cardView.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutShowWord)
    }

    override fun showVariantCard(cardView : View) {
        val writeButton = cardView.findViewById<Button>(R.id.write_word_button)
        val showButton = cardView.findViewById<Button>(R.id.show_word_button)
        val chooseButton = cardView.findViewById<Button>(R.id.choose_word_button)

        val layoutChooseWord = cardView.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = cardView.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = cardView.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = cardView.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutVariantWord)

        initializeVariantListeners(cardView, writeButton, showButton, chooseButton)
    }

    override fun showChooseCard(cardView: View) {
        val layoutChooseWord = cardView.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = cardView.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = cardView.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = cardView.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.hideView(layoutWriteWord)
        UiHelper.showView(layoutChooseWord)
    }

    override fun showWriteCard(cardView : View) {
        val layoutChooseWord = cardView.findViewById<View>(R.id.layout_choose_word)
        val layoutWriteWord = cardView.findViewById<View>(R.id.layout_write_word)
        val layoutShowWord = cardView.findViewById<View>(R.id.layout_show_word)
        val layoutVariantWord = cardView.findViewById<View>(R.id.layout_variant)

        UiHelper.hideView(layoutChooseWord)
        UiHelper.hideView(layoutShowWord)
        UiHelper.hideView(layoutVariantWord)
        UiHelper.showView(layoutWriteWord)
    }

    override fun initializeCardStackAdapter(words : ArrayList<Word>, variants : ArrayList<Word>) {
        adapter = CardStackAdapter(words, ArrayList(variants))
        cardstack_words.adapter = adapter
    }

    override fun setupCardStack() {
        initializeCardStackView()
        presenter.setCardStackByProgress()
    }



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
            showChooseCard(view)

            val button1 = view.findViewById<Button>(R.id.choose_one_button)
            val button2 = view.findViewById<Button>(R.id.choose_two_button)
            val button3 = view.findViewById<Button>(R.id.choose_three_button)
            val button4 = view.findViewById<Button>(R.id.choose_four_button)

            button1.setOnClickListener {
                presenter.checkWord(button1.text.toString(), view)
            }

            button2.setOnClickListener {
                presenter.checkWord(button2.text.toString(), view)
            }

            button3.setOnClickListener {
                presenter.checkWord(button3.text.toString(), view)
            }

            button4.setOnClickListener {
                presenter.checkWord(button4.text.toString(), view)
            }
        }
    }

    /**
     * Initialize showButton
     */
    private fun initializeShowButton(view : View, showButton: Button?) {
        showButton?.setOnClickListener {
            showWordCard(view)
        }
    }

    /**
     * Initialize writeButton
     */
    private fun initializeWriteButton(view: View, writeButton: Button?) {
        writeButton?.setOnClickListener {
            showWriteCard(view)

            val submitButton = view.findViewById<Button>(R.id.submit_word_button)
            val helpButton = view.findViewById<Button>(R.id.help_word_button)
            val written = view.findViewById<EditText>(R.id.word_written_edit_text)

            submitButton?.setOnClickListener {
                UiHelper.hideKeyboard(view, context)
                presenter.checkWord(written?.text.toString(), view)
                written?.setText("", TextView.BufferType.EDITABLE)
            }

            helpButton?.setOnClickListener {
                presenter.helpWord(written?.text.toString(), view)
            }
        }
    }
}