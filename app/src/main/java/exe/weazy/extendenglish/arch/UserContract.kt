package exe.weazy.extendenglish.arch

import android.os.Bundle
import com.google.firebase.storage.StorageReference

interface UserContract {

    interface View {
        fun setAvatar(ref : StorageReference)
        fun setLevel(level : String)
        fun setUsername(username : String)
        fun showError()
        fun showSuccessSnackbar(stringResId : Int)
    }

    interface Model {
        fun writeAvatar(path : String)
        fun writeUsername(username : String)
        fun writeEmail(email : String)
        fun writePassword(password : String)
    }

    interface Presenter {
        fun attach(view : View)
        fun logOut()
        fun setLevel(level : String)
        fun setAvatar(path : String)
        fun setupUsername()
        fun updateAvatar(path : String)
        fun updateUsername(username : String)
        fun updateEmail(email : String)
        fun updatePassword(password : String)
        fun getAccountBundle() : Bundle
    }

    interface LoadingListener {
        fun onAvatarUpdateFinished()
        fun onAvatarUpdateFailure()

        fun onEmailUpdateFinished()
        fun onEmailUpdateFailure()

        fun onUsernameUpdateFinished()
        fun onUsernameUpdateFailure()

        fun onPasswordUpdateFinished()
        fun onPasswordUpdateFailure()
    }
}