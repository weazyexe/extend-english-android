package exe.weazy.extendenglish.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.arch.AccountContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.presenter.AccountPresenter

class AccountModel(private var presenter : AccountPresenter) : AccountContract.Model {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun loadLevelAndAvatar() {
        firestore.document("users/${auth.currentUser?.uid}").get().addOnCompleteListener { documentSnapshot ->
            if (documentSnapshot.isSuccessful) {
                val result = documentSnapshot.result

                if (result != null) {
                    val level = result.getString("level")
                    val path = result.getString("avatar")

                    if (level != null && path != null) {
                        presenter.onLevelAndAvatarLoadFinished(level, path)
                    }
                }
            } else {
                presenter.onLevelAndAvatarLoadFailure(documentSnapshot.exception)
            }
        }
    }

    override fun loadCategories() {
        firestore.collection("users/${auth.currentUser?.uid}/categories").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result?.documents

                val c = ArrayList<Category>()

                result?.forEach {

                    val name = it.getString("name")
                    if (name != null)
                        c.add(Category.getCategoryByString(name))
                }

                presenter.onCategoriesLoadFinished(c)
            } else {
                presenter.onCategoriesLoadFailure(querySnapshot.exception)
            }
        }
    }

    override fun loadAllCategories() {
        firestore.collection("categories").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result?.documents

                val c = ArrayList<Category>()

                result?.forEach {

                    val name = it.getString("name")
                    if (name != null)
                        c.add(Category.getCategoryByString(name))
                }

                presenter.onAllCategoriesLoadFinished(c)
            } else {
                presenter.onAllCategoriesLoadFailure(querySnapshot.exception)
            }
        }
    }
}