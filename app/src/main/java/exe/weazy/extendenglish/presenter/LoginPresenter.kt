package exe.weazy.extendenglish.presenter

import com.google.firebase.auth.FirebaseAuth
import exe.weazy.extendenglish.arch.LoginContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Step
import exe.weazy.extendenglish.model.LoginModel

class LoginPresenter : LoginContract.Presenter, LoginContract.LoadingListener {

    private lateinit var view : LoginContract.View
    private var model = LoginModel(this)

    private val auth = FirebaseAuth.getInstance()

    private lateinit var allCategories : ArrayList<Category>

    private lateinit var email : String
    private lateinit var password : String
    private lateinit var confirmation: String

    private var isSignIn = true
    private var step = Step.WELCOME



    override fun attach(view: LoginContract.View) {
        this.view = view
        step = Step.WELCOME
    }

    override fun checkAccount() {
        if (auth.currentUser != null) {
            model.loadCategories()
        } else {
            view.showWelcome()
        }
    }

    override fun createAccount() {
        isSignIn = false
        step = Step.EMAIL
        view.showEmail()

    }

    override fun logInToAccount() {
        isSignIn = true
        step = Step.EMAIL
        view.showEmail()
    }

    override fun next(checks : ArrayList<Boolean>?) {
        when (step) {
            Step.EMAIL -> {
                step = Step.PASSWORD
                view.readEmail()
                view.showPassword()
            }

            Step.PASSWORD -> {
                view.readPassword()
                passwordHandle()
            }

            Step.CONFIRM_PASSWORD -> {
                view.readConfirmation()
                confirmPasswordHandle()
            }

            Step.CATEGORIES -> {
                categoriesHandle(checks)
            }
        }
    }

    override fun back() {
        when (step) {
            Step.WELCOME -> {
                step = Step.WELCOME
            }

            Step.EMAIL -> {
                step = Step.WELCOME
                view.goBack(true)
            }

            Step.PASSWORD -> {
                step = Step.EMAIL
                view.goBack()
            }

            Step.CONFIRM_PASSWORD -> {
                step = Step.PASSWORD
                view.goBack()
            }

            Step.CATEGORIES -> {
                step = Step.CONFIRM_PASSWORD
                view.goBack()
            }
        }
    }

    override fun setEmail(email: String) {
        this.email = email
    }

    override fun setPassword(password: String) {
        this.password = password
    }

    override fun setConfirmation(confirmation: String) {
        this.confirmation = confirmation
    }



    override fun onCategoriesWriteFinished() {
        view.updateUI()
    }

    override fun onCategoriesWriteFailure() {
        view.showDoneFAB()
        view.showErrorSnackbar()
    }

    override fun onCategoriesLoadingFinished(categories: ArrayList<Category>) {
        if (categories.isNullOrEmpty()) {
            model.loadAllCategories()
            step = Step.CATEGORIES
        } else {
            view.updateUI()
        }
    }

    override fun onCategoriesLoadingFailure(exception: Exception?) {
        view.showErrorSnackbar()
    }

    override fun onAllCategoriesLoadingFinished(allCategories: ArrayList<Category>) {
        this.allCategories = allCategories

        view.showDoneFAB()
        view.showBackButton()
        view.showCategories(allCategories)
    }

    override fun onAllCategoriesLoadingFailure(exception: Exception?) {
        view.showDefaultFAB()
        view.showErrorSnackbar()
    }



    override fun onLogInFinished() {
        checkAccount()
    }

    override fun onLogInFailure() {
        view.showDefaultFAB()
        view.showWrongEmailOrPasswordSnackbar()
    }

    override fun onCreateAccountFinished() {
        model.loadAllCategories()
    }

    override fun onCreateAccountFailure() {
        view.showDefaultFAB()
        view.showErrorSnackbar()
    }





    private fun passwordHandle() {

        if (!isSignIn) {
            step = Step.CONFIRM_PASSWORD
            view.showConfirmPassword()
        } else {
            view.showLoadingFAB()
            model.logIn(email, password)
        }
    }

    private fun confirmPasswordHandle() {
        // If passwords don't match
        if (password != confirmation) {
            view.showPasswordsDoNotMatchSnackbar()
        } else {
            step = Step.CATEGORIES
            view.showLoadingFAB()

            model.createAccount(email, password)
        }
    }

    private fun categoriesHandle(checks : ArrayList<Boolean>?) {
        if (checks != null) {
            var count = 0
            checks.forEach { if (it) count++ }
            if (count < 5) {
                view.showChooseCategoriesSnackbar()
            } else {
                val selectedCategories = ArrayList<Category>()
                for (i in 0 until (checks.size - 1)) {
                    if (checks[i]) selectedCategories.add(allCategories[i])
                }

                view.showLoadingFAB()
                model.writeCategories(selectedCategories)
            }
        }
    }
}