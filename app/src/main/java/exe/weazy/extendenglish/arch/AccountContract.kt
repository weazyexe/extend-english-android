package exe.weazy.extendenglish.arch

import android.os.Bundle
import com.google.firebase.storage.StorageReference
import exe.weazy.extendenglish.entity.Category


interface AccountContract {

    interface Model {
        fun loadLevelAndAvatar()
        fun loadCategories()
        fun loadAllCategories()
    }

    interface View {
        fun updateCategories(categories: ArrayList<Category>, allCategories: ArrayList<Category>)
        fun setAvatar(reference : StorageReference)
        fun setUsername(username : String)
        fun setLevel(level : String)
        fun showLoading()
        fun showError()
        fun showScreen()
    }

    interface Presenter {
        fun attach(view : View)
        fun getAllData()
        fun getUserActivityBundle() : Bundle
        fun setAvatar(path : String)
        fun setLevel(level : String)
    }

    interface LoadingListener {
        fun onLevelAndAvatarLoadFinished(level : String, path : String)
        fun onLevelAndAvatarLoadFailure(exception: Exception?)

        fun onCategoriesLoadFinished(categories : ArrayList<Category>)
        fun onCategoriesLoadFailure(exception: Exception?)

        fun onAllCategoriesLoadFinished(allCategories : ArrayList<Category>)
        fun onAllCategoriesLoadFailure(exception: Exception?)
    }
}