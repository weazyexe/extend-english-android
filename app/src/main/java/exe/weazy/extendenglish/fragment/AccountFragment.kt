package exe.weazy.extendenglish.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesRecyclerViewAdapter
import exe.weazy.extendenglish.entity.Category
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private var username : String? = "name placeholder"
    private var level : String? = "level placeholder"
    lateinit var categories : ArrayList<Category>
    lateinit var adapter : CategoriesRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        username = arguments?.getString("username")
        level = arguments?.getString("level")

        categories = arguments?.getSerializable("categories") as ArrayList<Category>

        adapter = CategoriesRecyclerViewAdapter(categories)

        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onStart() {
        super.onStart()

        categories_rv.adapter = adapter
        categories_rv.layoutManager = GridLayoutManager(activity?.applicationContext, 2)

        username_text.text = username
        level_text.text = level
    }
}