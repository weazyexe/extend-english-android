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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.StorageReference
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesWidgetAdapter
import exe.weazy.extendenglish.arch.AccountContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.presenter.AccountPresenter
import exe.weazy.extendenglish.tools.GlideApp
import exe.weazy.extendenglish.view.activity.CategoriesActivity
import exe.weazy.extendenglish.view.activity.UserActivity
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(), AccountContract.View {

    private val CATEGORIES_ACTIVITY_CODE = 100
    private val USER_ACTIVITY_CODE = 101

    private var presenter = AccountPresenter()

    private var manager = GridLayoutManager(activity, 3)
    private lateinit var adapter : CategoriesWidgetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attach(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onStart() {
        super.onStart()

        if (!::adapter.isInitialized) {
            presenter.getAllData()
        }

        card_account.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(activity as Activity,
                Pair.create<View, String>(imageview_avatar,"trtext_avatar"),
                Pair.create<View, String>(text_username,"trtext_username"),
                Pair.create<View, String>(text_level,"trtext_level"),
                Pair.create<View, String>(card_account,"trtext_card"))


            val bundle = presenter.getUserActivityBundle()
            intent.putExtras(bundle)

            startActivityForResult(intent, USER_ACTIVITY_CODE, options.toBundle())
        }
    }

    override fun updateAdapter(categories : ArrayList<Category>, allCategories: ArrayList<Category>) {

        val mOnClickListener = View.OnClickListener {
            val intent = Intent(activity, CategoriesActivity::class.java)

            val bundle = Bundle()
            bundle.putSerializable("categories", categories)
            bundle.putSerializable("allCategories", allCategories)

            intent.putExtras(bundle)

            startActivityForResult(intent, CATEGORIES_ACTIVITY_CODE)
        }

        adapter = CategoriesWidgetAdapter(categories, mOnClickListener)
        recyclerview_categories_account.adapter = adapter
        recyclerview_categories_account.layoutManager = manager
    }

    override fun setAvatar(reference : StorageReference) {
        GlideApp.with(this)
            .load(reference)
            .placeholder(R.drawable.avatar_placeholder)
            .into(imageview_avatar)
    }

    override fun setLevel(level: String) {
        text_level.text = level
    }

    override fun setUsername(username: String) {
        text_username.text = username
    }

    override fun showLoading() {
        layout_account.visibility = View.GONE
        progressbar_account.visibility = View.VISIBLE
        layout_error.visibility = View.GONE
    }

    override fun showError() {
        layout_account.visibility = View.GONE
        progressbar_account.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
    }

    override fun showScreen() {
        layout_account.visibility = View.VISIBLE
        progressbar_account.visibility = View.GONE
        layout_error.visibility = View.GONE
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CATEGORIES_ACTIVITY_CODE -> {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    val categories = data.getSerializableExtra("categories") as ArrayList<Category>

                    presenter.updateCategories(categories)
                }
            }
            USER_ACTIVITY_CODE -> {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    val username = data.getStringExtra("username")!!
                    val avatar = data.getStringExtra("avatar")!!
                    val level = data.getStringExtra("level")!!

                    setUsername(username)
                    presenter.setLevel(level)
                    presenter.setAvatar(avatar)
                }
            }
        }
    }
}