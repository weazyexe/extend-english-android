package exe.weazy.extendenglish.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.Level
import exe.weazy.extendenglish.tools.StringHelper
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        loading_signup.visibility = View.GONE
    }



    fun onSubmitButtonClick(view : View) {
        val email = email_signup.text.toString()
        val password = password_signup.text.toString()
        val confirmPassword = confirm_password_signup.text.toString()
        val username = username_signup.text.toString()

        form_signup.visibility = View.GONE
        loading_signup.visibility = View.VISIBLE

        if (password == confirmPassword) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                    user?.updateProfile(profileUpdates)

                    createUserFolder()
                } else {
                    Snackbar.make(layout_signup, getString(R.string.error), Snackbar.LENGTH_LONG).show()
                    form_signup.visibility = View.VISIBLE
                    loading_signup.visibility = View.GONE
                }
            }
        } else {
            Snackbar.make(layout_signup, getString(R.string.passwords_arent_equal), Snackbar.LENGTH_LONG).show()
            form_signup.visibility = View.VISIBLE
            loading_signup.visibility = View.GONE
        }
    }

    private fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, CategoriesActivity::class.java)
            intent.putExtra("user", user)

            startActivity(intent)
            finish()
        }
    }

    private fun createUserFolder() {
        val user = firebaseAuth.currentUser

        if (user != null) {

        }
    }
}
