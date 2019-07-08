package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.extendenglish.presenter.AccountPresenter
import exe.weazy.extendenglish.presenter.LearnPresenter

class MainViewModel : ViewModel() {

    private lateinit var accountPresenter : MutableLiveData<AccountPresenter>
    private lateinit var learnPresenter : MutableLiveData<LearnPresenter>

    fun getAccountPresenter() : LiveData<AccountPresenter> {
        if (!::accountPresenter.isInitialized) {
            accountPresenter = MutableLiveData()
            accountPresenter.postValue(AccountPresenter())
        }

        return accountPresenter
    }

    fun getLearnPresenter() : LiveData<LearnPresenter> {
        if (!::learnPresenter.isInitialized) {
            learnPresenter = MutableLiveData()
            learnPresenter.postValue(LearnPresenter())
        }

        return learnPresenter
    }
}