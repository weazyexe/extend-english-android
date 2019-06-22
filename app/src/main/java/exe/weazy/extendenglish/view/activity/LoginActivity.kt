package exe.weazy.extendenglish.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesAdapter
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.model.Level
import exe.weazy.extendenglish.model.Step
import exe.weazy.extendenglish.tools.FirebaseHelper
import exe.weazy.extendenglish.tools.StringHelper
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_confirm_password.*
import kotlinx.android.synthetic.main.fragment_email.*
import kotlinx.android.synthetic.main.fragment_password.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var navController : NavController

    private lateinit var allCategories : ArrayList<Category>

    private lateinit var email : String
    private lateinit var password : String

    private lateinit var adapter : CategoriesAdapter
    private lateinit var manager: LinearLayoutManager

    private var isSignIn = true
    private var step = Step.WELCOME


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        navController = Navigation.findNavController(this, R.id.nav_fragment_login)
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    override fun onBackPressed() {
        goBack()
    }


    fun onSignInButtonClick(view : View) {
        startLogIn(true)
    }

    fun onBackButtonClick(view : View) {
        goBack()
    }

    fun onCreateButtonClick(view: View) {
        startLogIn(false)
    }

    fun onNextButtonClick(view : View) {
        when (step) {
            Step.WELCOME -> {
                goToEmail()
            }

            Step.EMAIL -> {
                goToPassword()
            }

            Step.PASSWORD -> {
                passwordHandle()
            }

            Step.CONFIRM_PASSWORD -> {
                confirmPasswordHandle()
            }

            Step.CATEGORIES -> {
                categoriesHandle()
            }
        }
    }


    private fun initializeAllCategoriesObserver() {
        val allCategoriesLiveData = viewModel.getCategories()
        allCategoriesLiveData.observe(this, Observer {
            allCategories = it

            initializeRecyclerView()
            recyclerview_categories_login.visibility = View.VISIBLE
            UiHelper.hideView(progressbar_categories)
        })
    }



    private fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




    private fun startLogIn(isSignIn : Boolean) {
        this.isSignIn = isSignIn

        step = Step.EMAIL

        navController.navigate(R.id.action_welcomeFragment_to_emailFragment)

        button_next.show()
        UiHelper.showView(button_back)
    }


    private fun goBack() {
        when (step) {
            Step.WELCOME -> {
                step = Step.WELCOME
                onBackPressed()
            }

            Step.EMAIL -> {
                step = Step.WELCOME
                navController.popBackStack()

                button_next.hide()
                UiHelper.hideView(button_back)
            }

            Step.PASSWORD -> {
                step = Step.EMAIL
                navController.popBackStack()
            }

            Step.CONFIRM_PASSWORD -> {
                step = Step.PASSWORD
                navController.popBackStack()
            }

            Step.CATEGORIES -> {
                step = Step.CONFIRM_PASSWORD
                navController.popBackStack()
            }
        }
    }

    private fun goToEmail() {
        step = Step.EMAIL

        navController.navigate(R.id.action_welcomeFragment_to_emailFragment)
    }

    private fun goToPassword() {
        step = Step.PASSWORD

        email = edittext_email.text.toString()
        navController.navigate(R.id.action_emailFragment_to_passwordFragment)
    }

    private fun passwordHandle() {
        password = edittext_password.text.toString()

        if (!isSignIn) {
            step = Step.CONFIRM_PASSWORD
            navController.navigate(R.id.action_passwordFragment_to_confirmPasswordFragment)
        } else {
            button_next.setImageDrawable(null)
            progressbar_sign_in.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    progressbar_sign_in.visibility = View.GONE
                    Snackbar.make(layout_login, R.string.wrong_email_or_password, Snackbar.LENGTH_SHORT).show()
                    button_next.setImageDrawable(getDrawable(R.drawable.ic_arrow_forward_white_24dp))
                }
            }
        }
    }

    private fun confirmPasswordHandle() {
        // If passwords don't match
        if (password != edittext_confirm_password.text.toString()) {
            Snackbar.make(layout_login, R.string.passwords_do_not_match, Snackbar.LENGTH_SHORT).show()
        } else {
            step = Step.CATEGORIES
            navController.navigate(R.id.action_confirmPasswordFragment_to_categoriesFragment)

            UiHelper.hideKeyboard(layout_login, this)

            // Create user and write init info: level (Newbie) and progress (learnToday)
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                initializeAllCategoriesObserver()
                writeInitAccountInfo()
            }
        }
    }

    private fun categoriesHandle() {
        val checks = adapter.getChecks()
        val selectedCategories = ArrayList<Category>()
        for (i in 0..(checks.size - 1)) {
            if (checks[i]) selectedCategories.add(allCategories[i])
        }

        val run = fun() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        button_next.setImageDrawable(null)
        progressbar_sign_in.visibility = View.VISIBLE
        FirebaseHelper.writeCategories(firestore, selectedCategories, run)
    }



    private fun writeInitAccountInfo() {
        val hashMap = HashMap<String, String>()
        hashMap["level"] = StringHelper.upperSnakeToUpperCamel(Level.NEWBIE.name)
        hashMap["progress"] = "learnToday"
        hashMap["avatar"] = "default_avatars/placeholder.png"

        val user = auth.currentUser
        if (user != null) {
            firestore.document("users/${user.uid}").set(hashMap)
        }
    }

    private fun initializeRecyclerView() {
        if (!::adapter.isInitialized && !::manager.isInitialized) {
            adapter = CategoriesAdapter(allCategories)
            manager = LinearLayoutManager(this)
            recyclerview_categories_login.adapter = adapter
            recyclerview_categories_login.layoutManager = manager
        }
    }
}
