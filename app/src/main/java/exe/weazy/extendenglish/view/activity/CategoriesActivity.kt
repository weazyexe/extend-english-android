package exe.weazy.extendenglish.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesAdapter
import exe.weazy.extendenglish.arch.CategoriesContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.presenter.CategoriesPresenter
import exe.weazy.extendenglish.viewmodel.CategoriesViewModel
import kotlinx.android.synthetic.main.activity_categories.*

class CategoriesActivity : AppCompatActivity(), CategoriesContract.View {

    private lateinit var presenter : CategoriesPresenter

    private lateinit var viewModel : CategoriesViewModel

    private lateinit var adapter : CategoriesAdapter
    private lateinit var manager : LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)

        val presenterLiveData = viewModel.getPresenter()
        presenterLiveData.observe(this, Observer {
            presenter = it
            presenter.attach(this)

            if (intent != null) {
                val categories = intent.getSerializableExtra("categories") as ArrayList<Category>
                val allCategories = intent.getSerializableExtra("allCategories") as ArrayList<Category>

                presenter.setCategories(categories)
                presenter.setAllCategories(allCategories)

                initializeAdapter(categories, allCategories)
            }
        })
    }


    override fun showError() {
        Snackbar.make(layout_categories, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoadingFAB() {
        button_done.setImageDrawable(null)
        progressbar_categories.visibility = View.VISIBLE
    }

    override fun showDoneFAB() {
        button_done.setImageDrawable(getDrawable(R.drawable.ic_done_white_24dp))
        progressbar_categories.visibility = View.GONE
    }

    override fun sendDataBackAndClose(categories : ArrayList<Category>) {
        val intent = Intent()
        intent.putExtra("categories", categories)

        setResult(Activity.RESULT_OK, intent)
        onBackPressed()
    }



    fun onBackButtonClick(view : View) {
        onBackPressed()
    }

    fun onDoneButtonClick(view: View) {
        val checks = adapter.getChecks()
        presenter.done(checks)
    }



    private fun initializeAdapter(categories: ArrayList<Category>, allCategories : ArrayList<Category>) {

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
