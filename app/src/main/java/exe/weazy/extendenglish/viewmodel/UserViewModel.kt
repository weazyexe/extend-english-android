package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.extendenglish.presenter.UserPresenter

class UserViewModel : ViewModel() {

    private lateinit var presenter : MutableLiveData<UserPresenter>

    fun getPresenter() : LiveData<UserPresenter> {
        if (!::presenter.isInitialized) {
            presenter = MutableLiveData()
            presenter.postValue(UserPresenter())
        }

        return presenter
    }
}