package exe.weazy.extendenglish.presenter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.arch.UserContract
import exe.weazy.extendenglish.model.UserModel

class UserPresenter : UserContract.Presenter, UserContract.LoadingListener {

    private lateinit var view : UserContract.View
    private var model = UserModel(this)

    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var level : String
    private var avatarPath = "default_avatars/placeholder.png"



    override fun attach(view: UserContract.View) {
        this.view = view
    }

    override fun logOut() {
        auth.signOut()
    }



    override fun setLevel(level: String) {
        this.level = level
    }

    override fun setAvatar(path: String) {
        avatarPath = path
    }

    override fun setupUsername() {
        val username = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }

        if (username != null) {
            view.setUsername(username)
        } else {
            view.showError()
        }
    }



    override fun updateAvatar(path: String) {
        model.writeAvatar(path)
        avatarPath = path

        val ref = storage.getReference(avatarPath)
        view.setAvatar(ref)
    }

    override fun updateUsername(username: String) {
        model.writeUsername(username)
    }

    override fun updateEmail(email: String) {
        model.writeEmail(email)
    }

    override fun updatePassword(password: String) {
        model.writePassword(password)
    }



    override fun onAvatarUpdateFinished() {
        view.showSuccessSnackbar(R.string.avatar_has_been_changed)
    }

    override fun onAvatarUpdateFailure() {
        view.showError()
    }

    override fun onEmailUpdateFinished() {
        view.showSuccessSnackbar(R.string.email_has_been_changed)
    }

    override fun onEmailUpdateFailure() {
        view.showError()
    }

    override fun onUsernameUpdateFinished() {
        view.showSuccessSnackbar(R.string.username_has_been_changed)
    }

    override fun onUsernameUpdateFailure() {
        view.showError()
    }

    override fun onPasswordUpdateFinished() {
        view.showSuccessSnackbar(R.string.password_has_been_changed)
    }

    override fun onPasswordUpdateFailure() {
        view.showError()
    }
}