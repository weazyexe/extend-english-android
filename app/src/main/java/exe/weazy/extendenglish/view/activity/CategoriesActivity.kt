package exe.weazy.extendenglish.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.tools.FirebaseHelper
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var categories : ArrayList<Category>
    private lateinit var allCategories : ArrayList<Category>
    private lateinit var adapter : CategoriesAdapter
    private lateinit var manager : LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        categories = intent.getSerializableExtra("categories") as ArrayList<Category>
        allCategories = intent.getSerializableExtra("allCategories") as ArrayList<Category>
    }

    override fun onStart() {
        super.onStart()
        initializeRecyclerView()
    }

    fun onBackButtonClick(view : View) {
        onBackPressed()
    }

    fun onDoneButtonClick(view: View) {
        val checks = adapter.getChecks()
        val selectedCategories = ArrayList<Category>()
        for (i in 0 until (checks.size - 1)) {
            if (checks[i]) {
                selectedCategories.add(allCategories[i])
            }
        }

        val run = fun() {
            finishActivity(100)
        }

        button_done.setImageDrawable(null)
        progressbar_categories.visibility = View.VISIBLE
        FirebaseHelper.writeCategories(firestore, selectedCategories, run)
    }

    private fun initializeRecyclerView() {

        val checks = Array(allCategories.size) { false }

        for (i in 0 until (allCategories.size - 1)) {
            checks[i] = categories.contains(allCategories[i])
        }

        adapter = CategoriesAdapter(allCategories, ArrayList(checks.toList()))
        manager = LinearLayoutManager(this)
        recyclerview_categories.adapter = adapter
        recyclerview_categories.layoutManager = manager
    }
}
