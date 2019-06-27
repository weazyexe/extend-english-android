package exe.weazy.extendenglish.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.adapter.CategoriesAdapter
import exe.weazy.extendenglish.arch.LoginContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.presenter.LoginPresenter
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.view.fragment.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.button_back
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_confirm_password.*
import kotlinx.android.synthetic.main.fragment_email.*
import kotlinx.android.synthetic.main.fragment_password.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var welcomeFragment: WelcomeFragment
    private lateinit var emailFragment: EmailFragment
    private lateinit var passwordFragment: PasswordFragment
    private lateinit var confirmPasswordFragment: ConfirmPasswordFragment
    private lateinit var categoriesFragment : CategoriesFragment
    private var active = Fragment()

    private var newPosition = 0
    private var startingPosition = 0

    private var presenter = LoginPresenter()

    private lateinit var adapter : CategoriesAdapter
    private lateinit var manager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadFragments()

        presenter.attach(this)
    }

    override fun onStart() {
        super.onStart()

        presenter.checkAccount()
    }

    override fun onBackPressed() {
        presenter.back()
    }


    override fun showBackButton() {
        button_back.visibility = View.VISIBLE
    }

    override fun showWelcome() {
        newPosition = 0
        supportFragmentManager.beginTransaction().show(welcomeFragment).commit()
    }

    override fun showEmail() {
        button_next.show()
        UiHelper.showView(button_back)

        newPosition++
        changeFragment(emailFragment)
        edittext_email.requestFocus()
    }

    override fun showPassword() {
        newPosition++
        changeFragment(passwordFragment)
        edittext_password.requestFocus()
    }

    override fun showConfirmPassword() {
        newPosition++
        changeFragment(confirmPasswordFragment)
        edittext_confirm_password.setText("", TextView.BufferType.EDITABLE)
        edittext_confirm_password.requestFocus()
    }

    override fun showCategories(categories: ArrayList<Category>) {
        newPosition++
        UiHelper.hideKeyboard(layout_login, this)
        changeFragment(categoriesFragment)

        if (!::adapter.isInitialized && !::manager.isInitialized) {
            adapter = CategoriesAdapter(categories)
            manager = LinearLayoutManager(this)
            recyclerview_categories_login.adapter = adapter
            recyclerview_categories_login.layoutManager = manager

            progressbar_categories.visibility = View.GONE
            recyclerview_categories_login.visibility = View.VISIBLE
        }
    }

    override fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun goBack(toWelcome : Boolean) {
        if (toWelcome) {
            button_next.hide()
            UiHelper.hideView(button_back)
        }

        when {
            active == welcomeFragment -> {
                super.onBackPressed()
            }

            active == emailFragment -> {
                newPosition--
                changeFragment(welcomeFragment)
            }

            active == passwordFragment -> {
                newPosition--
                changeFragment(emailFragment)
                edittext_email.requestFocus()
            }

            active == confirmPasswordFragment -> {
                newPosition--
                changeFragment(passwordFragment)
                edittext_password.requestFocus()
            }

            active == categoriesFragment -> {
                super.onBackPressed()
            }
            else -> {
                showErrorSnackbar()
            }
        }
    }



    override fun readEmail() {
        val email = edittext_email.text.toString()
        presenter.setEmail(email)
    }

    override fun readPassword() {
        val password = edittext_password.text.toString()
        presenter.setPassword(password)
    }

    override fun readConfirmation() {
        val confirmation = edittext_confirm_password.text.toString()
        presenter.setConfirmation(confirmation)
    }



    override fun showDefaultFAB() {
        progressbar_sign_in.visibility = View.GONE
        button_next.setImageDrawable(getDrawable(R.drawable.ic_arrow_forward_white_24dp))
    }

    override fun showLoadingFAB() {
        button_next.setImageDrawable(null)
        progressbar_sign_in.visibility = View.VISIBLE
    }

    override fun showDoneFAB() {
        button_next.show()

        progressbar_sign_in.visibility = View.GONE
        button_next.setImageDrawable(getDrawable(R.drawable.ic_done_white_24dp))

        val dp = resources.displayMetrics.density
        val centerX = resources.displayMetrics.widthPixels / dp

        button_next.animate()
            .setDuration(400)
            .translationX(-centerX - 10)
            .start()
    }



    override fun showWrongEmailOrPasswordSnackbar() {
        Snackbar.make(layout_login, R.string.wrong_email_or_password, Snackbar.LENGTH_SHORT).show()
    }

    override fun showPasswordsDoNotMatchSnackbar() {
        Snackbar.make(layout_login, R.string.passwords_do_not_match, Snackbar.LENGTH_SHORT).show()
    }

    override fun showErrorSnackbar() {
        Snackbar.make(layout_login, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showChooseCategoriesSnackbar() {
        Snackbar.make(layout_login, R.string.choose_categories, Snackbar.LENGTH_LONG).show()
    }



    fun onSignInButtonClick(view : View) {
        presenter.logInToAccount()
    }

    fun onBackButtonClick(view : View) {
        onBackPressed()
    }

    fun onCreateButtonClick(view: View) {
        presenter.createAccount()
    }

    fun onNextButtonClick(view : View) {
        if (::adapter.isInitialized) {
            presenter.next(adapter.getChecks())
        } else {
            presenter.next()
        }
    }


    private fun loadFragments() {
        welcomeFragment = WelcomeFragment()
        emailFragment = EmailFragment()
        passwordFragment = PasswordFragment()
        confirmPasswordFragment = ConfirmPasswordFragment()
        categoriesFragment = CategoriesFragment()

        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, categoriesFragment).hide(categoriesFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, confirmPasswordFragment).hide(confirmPasswordFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, passwordFragment).hide(passwordFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, emailFragment).hide(emailFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, welcomeFragment).hide(welcomeFragment).commit()

        active = welcomeFragment
    }

    private fun changeFragment(fragment : Fragment) {
        if (startingPosition > newPosition) {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).show(fragment).hide(active).commit()
        } else {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(fragment).hide(active).commit()
        }

        startingPosition = newPosition
        active = fragment
    }
}
