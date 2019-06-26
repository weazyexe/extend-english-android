package exe.weazy.extendenglish.presenter

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import exe.weazy.extendenglish.arch.AccountContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.model.AccountModel

class AccountPresenter : AccountContract.Presenter, AccountContract.LoadingListener {

    private lateinit var view : AccountContract.View
    private var model = AccountModel(this)

    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var avatarPath = "default_avatars/placeholder.png"

    private lateinit var level : String
    private lateinit var categories : ArrayList<Category>
    private lateinit var allCategories : ArrayList<Category>

    private var isCategoriesLoaded = false
    private var isLevelAndAvatarLoaded = false
    private var isAllCategoriesLoaded = false



    override fun getAllData() {
        view.showLoading()

        model.loadAllCategories()
        model.loadLevelAndAvatar()
        model.loadCategories()

        setupUsername()
    }

    override fun attach(view: AccountContract.View) {
        this.view = view
        preLoad()
    }

    private fun setupUsername() {
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

    override fun getUserActivityBundle(): Bundle {
        val bundle = Bundle()

        bundle.putString("level", level)
        bundle.putString("avatar_path", avatarPath)

        return bundle
    }



    override fun setAvatar(path: String) {
        avatarPath = path
        view.setAvatar(storage.getReference(avatarPath))
    }

    override fun setLevel(level: String) {
        this.level = level
        view.setLevel(level)
    }



    override fun onCategoriesLoadFinished(categories: ArrayList<Category>) {
        isCategoriesLoaded = true
        this.categories = categories
        afterLoad()
    }

    override fun onCategoriesLoadFailure(exception: Exception?) {
        view.showError()
    }

    override fun onAllCategoriesLoadFinished(allCategories: ArrayList<Category>) {
        isAllCategoriesLoaded = true
        this.allCategories = allCategories
        afterLoad()
    }

    override fun onAllCategoriesLoadFailure(exception: Exception?) {
        view.showError()
    }

    override fun onLevelAndAvatarLoadFinished(level : String, path : String) {
        isLevelAndAvatarLoaded = true
        this.level = level
        avatarPath = path
        afterLoad()
    }

    override fun onLevelAndAvatarLoadFailure(exception: Exception?) {
        view.showError()
    }

    override fun updateCategories(categories: ArrayList<Category>) {
        this.categories = categories

        view.updateAdapter(categories, allCategories)
    }



    private fun afterLoad() {
        if (isCategoriesLoaded && isLevelAndAvatarLoaded && isAllCategoriesLoaded) {
            val ref = storage.getReference(avatarPath)

            view.setAvatar(ref)
            view.setLevel(level)
            view.updateAdapter(categories, allCategories)

            view.showScreen()
        }
    }

    private fun preLoad() {
        isCategoriesLoaded = false
        isLevelAndAvatarLoaded = false
        isAllCategoriesLoaded = false
    }
}