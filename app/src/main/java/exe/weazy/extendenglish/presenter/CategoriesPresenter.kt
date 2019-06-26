package exe.weazy.extendenglish.presenter

import exe.weazy.extendenglish.arch.CategoriesContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.model.CategoriesModel

class CategoriesPresenter : CategoriesContract.Presenter, CategoriesContract.LoadingListener {

    private lateinit var view : CategoriesContract.View
    private var model = CategoriesModel(this)

    private lateinit var categories : ArrayList<Category>
    private lateinit var allCategories : ArrayList<Category>



    override fun attach(view: CategoriesContract.View) {
        this.view = view
    }

    override fun done(checks: ArrayList<Boolean>) {
        val selectedCategories = ArrayList<Category>()
        for (i in 0 until (checks.size - 1)) {
            if (checks[i]) {
                selectedCategories.add(allCategories[i])
            }
        }

        view.showLoadingFAB()

        categories = selectedCategories

        model.writeCategories(selectedCategories)
    }


    override fun setCategories(categories: ArrayList<Category>) {
        this.categories = categories
    }

    override fun setAllCategories(allCategories: ArrayList<Category>) {
        this.allCategories = allCategories
    }



    override fun onCategoriesWriteFinished() {
        view.sendDataBackAndClose(categories)
    }

    override fun onCategoriesWriteFailure() {
        view.showDoneFAB()
        view.showError()
    }
}