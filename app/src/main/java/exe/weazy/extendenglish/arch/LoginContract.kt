package exe.weazy.extendenglish.arch

import exe.weazy.extendenglish.entity.Category

interface LoginContract {

    interface View {
        fun showWelcome()
        fun showEmail()
        fun showPassword()
        fun showConfirmPassword()
        fun showUsername()
        fun showCategories(categories: ArrayList<Category>)

        fun updateUI()
        fun goBack(toWelcome : Boolean = false)

        fun readEmail()
        fun readPassword()
        fun readConfirmation()
        fun readUsername()

        fun showLoadingFAB()
        fun showDefaultFAB()
        fun showDoneFAB()
        fun showBackButton()

        fun showWrongEmailOrPasswordSnackbar()
        fun showPasswordsDoNotMatchSnackbar()
        fun showErrorSnackbar()
        fun showChooseCategoriesSnackbar()
    }

    interface Model {
        fun loadCategories()
        fun loadAllCategories()
        fun writeCategories(categories: ArrayList<Category>)
        fun logIn(email : String, password: String)
        fun createAccount(email: String, password: String)
        fun setUsername(username: String)
    }

    interface Presenter {
        fun attach(view : View)
        fun createAccount()
        fun checkAccount()
        fun logInToAccount()
        fun next(checks : ArrayList<Boolean>? = null)
        fun back()
        fun setEmail(email : String)
        fun setPassword(password : String)
        fun setConfirmation(confirmation : String)
        fun setUsername(username : String)
    }

    interface LoadingListener {
        fun onCategoriesLoadingFinished(categories: ArrayList<Category>)
        fun onCategoriesLoadingFailure(exception: Exception?)

        fun onAllCategoriesLoadingFinished(allCategories : ArrayList<Category>)
        fun onAllCategoriesLoadingFailure(exception: Exception?)

        fun onCategoriesWriteFinished()
        fun onCategoriesWriteFailure()

        fun onLogInFinished()
        fun onLogInFailure()

        fun onCreateAccountFinished()
        fun onCreateAccountFailure()

        fun onUsernameUpdateFinished()
        fun onUsernameUpdateFailure()
    }
}