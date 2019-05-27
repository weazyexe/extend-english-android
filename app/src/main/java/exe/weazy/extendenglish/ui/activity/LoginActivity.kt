package exe.weazy.extendenglish.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesRecyclerViewAdapter
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Level
import exe.weazy.extendenglish.tools.StringHelper
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.ui.fragment.CategoriesFragment
import exe.weazy.extendenglish.ui.fragment.TextFragment
import exe.weazy.extendenglish.ui.fragment.WelcomeFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_welcome.view.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var firestore : FirebaseFirestore

    private lateinit var active : Fragment

    private lateinit var welcomeFragment : WelcomeFragment
    private lateinit var emailFragment : TextFragment
    private lateinit var passwordFragment : TextFragment
    private lateinit var confirmPasswordFragment : TextFragment
    private lateinit var categoriesFragment: CategoriesFragment

    private lateinit var allCategories : ArrayList<Category>

    private var startingPosition = 0
    private var newPosition = 0

    private var isSignIn = true

    private var step = Step.WELCOME

    private var email : String = ""
    private var password : String = ""

    private lateinit var adapter : CategoriesRecyclerViewAdapter
    private lateinit var manager: LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        welcomeFragment = WelcomeFragment()
        active = welcomeFragment
        supportFragmentManager.beginTransaction().add(R.id.fragment_login, welcomeFragment).show(welcomeFragment).commit()
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
        if (isSignIn && newPosition < 3 || !isSignIn && newPosition != 5) {

            newPosition++

            if (isSignIn && newPosition == 3 || !isSignIn && newPosition == 5) newPosition--

            when (step) {
                Step.WELCOME -> {
                    step = Step.EMAIL
                    changeFragment(emailFragment)
                    active = emailFragment
                }

                Step.EMAIL -> {
                    step = Step.PASSWORD

                    email = emailFragment.getText()
                    passwordFragment.getEditText().requestFocus()

                    changeFragment(passwordFragment)
                    active = passwordFragment
                }

                Step.PASSWORD -> {
                    password = passwordFragment.getText()

                    if (!isSignIn) {
                        step = Step.CONFIRM_PASSWORD
                        confirmPasswordFragment.getEditText().requestFocus()
                        changeFragment(confirmPasswordFragment)
                        active = confirmPasswordFragment
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

                Step.CONFIRM_PASSWORD -> {
                    if (!isSignIn)  {
                        if (password != confirmPasswordFragment.getText()) {
                            Snackbar.make(layout_login, R.string.passwords_arent_equal, Snackbar.LENGTH_SHORT).show()
                        } else {
                            step = Step.CATEGORIES
                            changeFragment(categoriesFragment)
                            active = categoriesFragment
                            categoriesFragment.showProgressBar()
                            UiHelper.hideKeyboard(layout_login, this)

                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                                initCategories()

                                val hashMap = HashMap<String, String>()
                                hashMap["level"] = StringHelper.upperSnakeToUpperCamel(Level.NEWBIE.name)
                                hashMap["progress"] = "learnToday"

                                val user = auth.currentUser
                                if (user != null) {
                                    firestore.document("users/${user.uid}").set(hashMap).addOnCompleteListener {
                                        Toast.makeText(this, "level & progress writes correctly", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                }

                Step.CATEGORIES -> {
                    if (!isSignIn) {
                        val checks = adapter.getChecks()
                        val selectedCategories = ArrayList<Category>()
                        for (i in 0..(checks.size - 1)) {
                            if (checks[i]) selectedCategories.add(allCategories[i])
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("categories", selectedCategories)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }



    private fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadFragments() {
        emailFragment = TextFragment("E-mail")
        passwordFragment = TextFragment("Password")
        confirmPasswordFragment = TextFragment("Confirm Password")
        categoriesFragment = CategoriesFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragment_login, emailFragment).hide(emailFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_login, passwordFragment).hide(passwordFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_login, confirmPasswordFragment).hide(confirmPasswordFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_login, categoriesFragment).hide(categoriesFragment).commit()
    }

    private fun changeFragment(fragment : Fragment) {
        if (startingPosition > newPosition) {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_to_right, R.anim.slide_out_right).show(fragment).hide(active).commit()
        } else {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_to_left, R.anim.slide_out_left).show(fragment).hide(active).commit()
        }
        startingPosition = newPosition
    }

    private fun goBack() {
        if (newPosition != 0) {

            newPosition--
            when (step) {
                Step.WELCOME -> {
                    step = Step.WELCOME
                    active = welcomeFragment
                }

                Step.EMAIL -> {
                    step = Step.WELCOME
                    changeFragment(welcomeFragment)
                    active = welcomeFragment
                    button_next.hide()
                    UiHelper.hideView(button_back)
                }

                Step.PASSWORD -> {
                    step = Step.EMAIL
                    emailFragment.getEditText().requestFocus()
                    changeFragment(emailFragment)
                    active = emailFragment
                }

                Step.CONFIRM_PASSWORD -> {
                    step = Step.PASSWORD
                    passwordFragment.getEditText().requestFocus()
                    changeFragment(passwordFragment)
                    active = passwordFragment
                }

                Step.CATEGORIES -> {
                    step = Step.CONFIRM_PASSWORD
                    confirmPasswordFragment.getEditText().requestFocus()
                    changeFragment(confirmPasswordFragment)
                    active = confirmPasswordFragment
                }
            }
        } else {
            onBackPressed()
        }
    }

    private fun startLogIn(isSignIn : Boolean) {
        this.isSignIn = isSignIn
        loadFragments()
        newPosition++
        step = Step.EMAIL
        changeFragment(emailFragment)
        active = emailFragment
        button_next.show()
        UiHelper.showView(button_back)
        //emailFragment.getEditText().requestFocus()
    }

    private fun initCategories() {
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("categories").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            allCategories = ArrayList()

            result?.forEach {

                val name = it.getString("name")
                if (name != null)
                    allCategories.add(Category.getCategoryByString(name))
            }

            adapter = CategoriesRecyclerViewAdapter(allCategories)
            manager = LinearLayoutManager(this)
            categoriesFragment.getRecyclerView().adapter = adapter
            categoriesFragment.getRecyclerView().layoutManager = manager

            categoriesFragment.hideProgressBar()
        }
    }


    private enum class Step {
        WELCOME, EMAIL, PASSWORD, CONFIRM_PASSWORD, CATEGORIES
    }
}
