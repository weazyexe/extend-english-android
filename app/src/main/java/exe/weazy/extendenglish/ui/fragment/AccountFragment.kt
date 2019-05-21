package exe.weazy.extendenglish.ui.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.UserActivity
import exe.weazy.extendenglish.adapter.CategoriesRecyclerViewAdapter
import exe.weazy.extendenglish.entity.Category
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private var username : String? = "name placeholder"
    private var level : String? = "level placeholder"
    lateinit var categories : ArrayList<Category>
    private var adapter : CategoriesRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (arguments != null) {
            username = arguments?.getString("username")
            level = arguments?.getString("level")

            categories = arguments?.getSerializable("categories") as ArrayList<Category>

            if (::categories.isInitialized) {
                adapter = CategoriesRecyclerViewAdapter(categories)
            }
        }

        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onStart() {
        super.onStart()

        categories_rv.adapter = adapter
        categories_rv.layoutManager = GridLayoutManager(activity?.applicationContext, 2)

        username_text.text = username
        level_text.text = level

        account_card.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(activity as Activity, avatar_imageView, "avatar_image")
            startActivity(intent, options.toBundle())
        }
    }
}