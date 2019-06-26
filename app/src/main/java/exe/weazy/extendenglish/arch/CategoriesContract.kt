package exe.weazy.extendenglish.arch

import exe.weazy.extendenglish.entity.Category

interface CategoriesContract {
    interface View {
        fun showError()
        fun showLoadingFAB()
        fun showDoneFAB()
        fun sendDataBackAndClose(categories: ArrayList<Category>)
    }

    interface Model {
        fun writeCategories(categories : ArrayList<Category>)
    }

    interface Presenter {
        fun attach(view : View)
        fun setCategories(categories: ArrayList<Category>)
        fun setAllCategories(allCategories : ArrayList<Category>)
        fun done(checks : ArrayList<Boolean>)
    }

    interface LoadingListener {
        fun onCategoriesWriteFinished()
        fun onCategoriesWriteFailure()
    }
}