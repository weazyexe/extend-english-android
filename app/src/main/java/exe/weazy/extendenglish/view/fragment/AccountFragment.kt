package exe.weazy.extendenglish.view.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesWidgetAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.tools.GlideApp
import exe.weazy.extendenglish.view.activity.CategoriesActivity
import exe.weazy.extendenglish.view.activity.UserActivity
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var avatarPath = "default_avatars/placeholder.png"

    private lateinit var level : String
    private lateinit var categories : ArrayList<Category>
    private lateinit var showedCategories : ArrayList<Category>
    private lateinit var allCategories : ArrayList<Category>

    private lateinit var manager : GridLayoutManager
    private lateinit var adapter : CategoriesWidgetAdapter

    private var isCategoriesLoaded = false
    private var isLevelLoaded = false
    private var isAllCategoriesLoaded = false

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        initializeLevelObserver()
        initializeCategoriesObserver()
        initializeAllCategoriesObserver()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onStart() {
        super.onStart()

        if (!isCategoriesLoaded && !isLevelLoaded) {
            layout_account.visibility = View.GONE
            progressbar_account.visibility = View.VISIBLE
        }

        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }

        card_account.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(activity as Activity,
                Pair.create<View, String>(imageview_avatar,"trtext_avatar"),
                Pair.create<View, String>(text_username,"trtext_username"),
                Pair.create<View, String>(text_level,"trtext_level"),
                Pair.create<View, String>(card_account,"trtext_card"))

            intent.putExtra("level", level)
            intent.putExtra("avatar_path", avatarPath)

            startActivity(intent, options.toBundle())
        }

        setAvatar()
    }

    private fun initializeLevelObserver() {
        val levelLiveData = viewModel.getLevel()
        levelLiveData.observe(this, Observer {
            level = it
            text_level.text = level

            isLevelLoaded = true
            afterLoad()
        })
    }

    private fun initializeAllCategoriesObserver() {
        val allCategoriesLiveData = viewModel.getAllCategories()
        allCategoriesLiveData.observe(this, Observer {
            allCategories = it

            isAllCategoriesLoaded = true
            afterLoad()
        })
    }

    private fun initializeCategoriesObserver() {
        val categoriesLiveData = viewModel.getCategories()
        categoriesLiveData.observe(this, Observer {
            categories = it

            if (!::adapter.isInitialized) {
                getShowedCategories()

                val mOnClickListener = View.OnClickListener {
                    val intent = Intent(activity, CategoriesActivity::class.java)

                    val bundle = Bundle()
                    bundle.putSerializable("categories", categories)
                    bundle.putSerializable("allCategories", allCategories)

                    intent.putExtras(bundle)

                    startActivityForResult(intent, 100)
                }

                adapter = CategoriesWidgetAdapter(showedCategories, mOnClickListener)
                recyclerview_categories_account.adapter = adapter
                manager = GridLayoutManager(activity, 3)
                recyclerview_categories_account.layoutManager = manager
            } else {
                adapter.setCategories(showedCategories)
            }

            isCategoriesLoaded = true

            afterLoad()
        })
    }

    private fun afterLoad() {
        if (isCategoriesLoaded && isLevelLoaded && isAllCategoriesLoaded && isCategoriesLoaded) {
            progressbar_account.visibility = View.GONE
            layout_account.visibility = View.VISIBLE
        }
    }

    private fun setAvatar() {

        firestore.document("users/${auth.currentUser?.uid}").get().addOnCompleteListener {
            val path = it.result?.get("avatar").toString()

            val ref = storage.getReference(path)

            GlideApp.with(this)
                .load(ref)
                .placeholder(R.drawable.avatar_placeholder)
                .into(imageview_avatar)

            avatarPath = path
        }

    }

    private fun getShowedCategories() {
        /*showedCategories = if (categories.size > 6) {
            arrayListOf(categories[0], categories[1], categories[2], categories[3], categories[4], categories[5])
        } else {
            ArrayList(categories)
        }*/
        showedCategories = ArrayList(categories)
    }
}