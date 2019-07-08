package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.extendenglish.presenter.CategoriesPresenter

class CategoriesViewModel : ViewModel() {

    private lateinit var presenter : MutableLiveData<CategoriesPresenter>

    fun getPresenter() : LiveData<CategoriesPresenter> {
        if (!::presenter.isInitialized) {
            presenter = MutableLiveData()
            presenter.postValue(CategoriesPresenter())
        }

        return presenter
    }
}