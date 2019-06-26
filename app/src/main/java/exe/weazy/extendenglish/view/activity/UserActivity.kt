package exe.weazy.extendenglish.view.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.arch.UserContract
import exe.weazy.extendenglish.presenter.UserPresenter
import exe.weazy.extendenglish.tools.GlideApp
import exe.weazy.extendenglish.view.dialog.AvatarDialog
import exe.weazy.extendenglish.view.dialog.NewPasswordDialog
import exe.weazy.extendenglish.view.dialog.TextDialog
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity(), TextDialog.TextDialogListener, NewPasswordDialog.NewPasswordDialogListener,
        AvatarDialog.AvatarDialogListener, UserContract.View {

    private var presenter = UserPresenter()

    private lateinit var loadingSnackbar : Snackbar

    private lateinit var type : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        presenter.attach(this)
        presenter.setAvatar(intent.getStringExtra("avatar_path")!!)
        presenter.setLevel(intent.getStringExtra("level")!!)
        presenter.setupUsername()
    }

    override fun onStart() {
        super.onStart()

        card_avatar.setOnClickListener {
            val dialog = AvatarDialog()
            dialog.show(supportFragmentManager, "Avatar change dialog")
        }
    }



    override fun applyPassword(password : String, repeat : String) {
        if (password == repeat) {
            loadingSnackbar = buildLoadingSnackbar()
            loadingSnackbar.show()

            presenter.updatePassword(password)
        } else {
            Snackbar.make(layout_user, R.string.passwords_do_not_match, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun applyText(text : String) {
        loadingSnackbar = buildLoadingSnackbar()
        loadingSnackbar.show()

        if (type == "Email") {
            presenter.updateEmail(text)
        } else {
            presenter.updateUsername(text)
        }
    }

    override fun applyAvatar(chosen: String) {
        loadingSnackbar = buildLoadingSnackbar()
        loadingSnackbar.show()

        presenter.updateAvatar(chosen)
    }



    override fun setUsername(username: String) {
        text_username.text = username
    }

    override fun setAvatar(ref: StorageReference) {
        GlideApp.with(layout_user)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(imageview_avatar)
    }

    override fun setLevel(level: String) {
        text_level.text = level
    }



    override fun showError() {
        if (::loadingSnackbar.isInitialized && loadingSnackbar.isShown) {
            loadingSnackbar.dismiss()
        }
        Snackbar.make(layout_user, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    override fun showSuccessSnackbar(stringResId : Int) {
        loadingSnackbar.dismiss()
        Snackbar.make(layout_user, stringResId, Snackbar.LENGTH_LONG).show()
    }



    fun onEmailChangeClickButton(view : View) {
        type = "Email"
        val dialog = TextDialog(type)
        dialog.show(supportFragmentManager, "Email change dialog")
    }

    fun onUsernameChangeClickButton(view : View) {
        type = "Username"
        val dialog = TextDialog(type)
        dialog.show(supportFragmentManager, "Username change dialog")
    }

    fun onPasswordChangeClickButton(view : View) {
        type = "Password"
        val dialog = NewPasswordDialog()
        dialog.show(supportFragmentManager, "Password change dialog")
    }

    fun onLogOutButtonClick(v: View) {
        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_message)
            .setNegativeButton(R.string.no) { _, _ ->

            }
            .setPositiveButton(R.string.yes) { _, _ ->
                presenter.logOut()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        dialog.show()
    }

    fun onBackButtonClick(view : View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        val bundle = presenter.getAccountBundle()

        intent.putExtras(bundle)

        setResult(RESULT_OK, intent)

        super.onBackPressed()
    }



    private fun buildLoadingSnackbar() : Snackbar {
        val bar = Snackbar.make(layout_user, R.string.loading, Snackbar.LENGTH_INDEFINITE)
        val contentLay = bar.view.findViewById<View>(R.id.snackbar_text).parent as LinearLayout
        val item = ProgressBar(this)

        contentLay.gravity = Gravity.CENTER_VERTICAL
        contentLay.addView(item, 70, 70)

        return bar
    }
}
