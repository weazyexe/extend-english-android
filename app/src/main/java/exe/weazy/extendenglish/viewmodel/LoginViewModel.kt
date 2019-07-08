package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.extendenglish.presenter.LoginPresenter

class LoginViewModel : ViewModel() {

    private lateinit var presenter : MutableLiveData<LoginPresenter>

    fun getPresenter() : LiveData<LoginPresenter> {
        if (!::presenter.isInitialized) {
            presenter = MutableLiveData()
            presenter.postValue(LoginPresenter())
        }

        return presenter
    }
}