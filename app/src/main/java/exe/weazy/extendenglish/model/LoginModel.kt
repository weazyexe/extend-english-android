package exe.weazy.extendenglish.model

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.arch.LoginContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Level
import exe.weazy.extendenglish.presenter.LoginPresenter
import exe.weazy.extendenglish.tools.StringHelper

class LoginModel(private var presenter : LoginPresenter) : LoginContract.Model {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()



    override fun loadCategories() {
        firestore.collection("users/${auth.currentUser?.uid}/categories").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result?.documents

                val categories = ArrayList<Category>()

                result?.forEach {

                    val name = it.getString("name")
                    if (name != null) {
                        categories.add(Category.getCategoryByString(name))
                    }
                }

                presenter.onCategoriesLoadingFinished(categories)
            } else {
                presenter.onCategoriesLoadingFailure(querySnapshot.exception)
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

                presenter.onAllCategoriesLoadingFinished(c)
            } else {
                presenter.onAllCategoriesLoadingFailure(querySnapshot.exception)
            }
        }
    }

    override fun writeCategories(categories: ArrayList<Category>) {
        var index = 0
        var writed = 0

        categories.forEach {
            val map = HashMap<String, String>()
            map["name"] = it.name

            firestore.document("users/${auth.currentUser?.uid}/categories/category-${index++}").set(map).addOnCompleteListener { task ->
                writed++

                if (writed == categories.size) {
                    presenter.onCategoriesWriteFinished()
                }

                if (task.exception != null) {
                    presenter.onCategoriesWriteFailure()
                }
            }
        }
    }

    override fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                presenter.onLogInFinished()
            } else {
                presenter.onLogInFailure()
            }
        }
    }

    override fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                writeInitAccountInfo()
                presenter.onCreateAccountFinished()
            } else {
                presenter.onCreateAccountFailure()
            }
        }
    }

    private fun writeInitAccountInfo() {
        val hashMap = HashMap<String, String>()
        hashMap["level"] = StringHelper.upperSnakeToUpperCamel(Level.NEWBIE.name)
        hashMap["progress"] = "learnToday"
        hashMap["avatar"] = "default_avatars/placeholder.png"

        val format = SimpleDateFormat("MM dd yyyy HH:mm")
        format.timeZone = TimeZone.GMT_ZONE

        hashMap["lastActivity"] = format.format(Calendar.getInstance(TimeZone.GMT_ZONE))

        val user = auth.currentUser
        if (user != null) {
            firestore.document("users/${user.uid}").set(hashMap)
        }
    }
}