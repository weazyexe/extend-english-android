package exe.weazy.extendenglish.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.arch.CategoriesContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.presenter.CategoriesPresenter

class CategoriesModel(private var presenter : CategoriesPresenter) : CategoriesContract.Model {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun writeCategories(categories: ArrayList<Category>) {
        var index = 0
        var written = 0

        categories.forEach {
            val map = HashMap<String, String>()
            map["name"] = it.name

            firestore.document("users/${auth.currentUser?.uid}/categories/category-${index++}").set(map).addOnCompleteListener { task ->
                written++

                if (written == categories.size) {
                    presenter.onCategoriesWriteFinished()
                }

                if (task.exception != null) {
                    presenter.onCategoriesWriteFailure()
                }
            }
        }
    }
}