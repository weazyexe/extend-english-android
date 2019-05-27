package exe.weazy.extendenglish.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.tools.UiHelper
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categories, null)
    }

    fun getRecyclerView() = recyclerview_categories_login

    fun hideProgressBar() {
        UiHelper.hideView(progressbar_categories)
        UiHelper.showView(recyclerview_categories_login)
    }

    fun showProgressBar() {
        UiHelper.showView(progressbar_categories)
        UiHelper.hideView(recyclerview_categories_login)
    }
}