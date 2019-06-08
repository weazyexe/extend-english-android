package exe.weazy.extendenglish.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesRecyclerViewAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var level : String
    private lateinit var categories : ArrayList<Category>

    private lateinit var manager : LinearLayoutManager
    private lateinit var adapter : CategoriesRecyclerViewAdapter

    private var isCategoriesLoaded = false
    private var isLevelLoaded = false

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        initializeLevelObserver()
        initializeCategoriesObserver()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onStart() {
        super.onStart()
        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }
    }

    private fun initializeLevelObserver() {
        val levelLiveData = viewModel.getLevel()
        levelLiveData.observe(this, Observer {
            level = it
            text_level.text = level

            afterLoad()
        })
    }

    private fun initializeCategoriesObserver() {
        val categoriesLiveData = viewModel.getCategories()
        categoriesLiveData.observe(this, Observer {
            categories = it

            if (!::adapter.isInitialized) {
                adapter = CategoriesRecyclerViewAdapter(categories)
                recyclerview_categories_account.adapter = adapter
                manager = LinearLayoutManager(activity?.applicationContext)
                recyclerview_categories_account.layoutManager = manager
            } else {
                adapter.setCategories(categories)
            }

            afterLoad()
        })
    }

    private fun afterLoad() {
        if (isCategoriesLoaded && isLevelLoaded) {
            progressbar_account.visibility = View.GONE
            layout_account.visibility = View.VISIBLE
        }
    }
}