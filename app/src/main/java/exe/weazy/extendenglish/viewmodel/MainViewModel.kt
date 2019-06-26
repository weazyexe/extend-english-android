package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.entity.Category

class MainViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var level : MutableLiveData<String>
    private lateinit var categories : MutableLiveData<ArrayList<Category>>
    private lateinit var allCategories : MutableLiveData<ArrayList<Category>>



    fun getAllCategories() : LiveData<ArrayList<Category>> {
        if (!::allCategories.isInitialized) {
            allCategories = MutableLiveData()
            loadAllCategories()
        }

        return allCategories
    }


    fun getLevel() : LiveData<String> {
        if (!::level.isInitialized) {
            level = MutableLiveData()
            loadLevel()
        }

        return level
    }

    fun getCategories() : LiveData<ArrayList<Category>> {
        if (!::categories.isInitialized) {
            categories = MutableLiveData()
            loadCategories()
        }

        return categories
    }


    private fun loadLevel() {
        firestore.document("users/${user?.uid}").get().addOnCompleteListener { documentSnapshot ->
            val result = documentSnapshot.result

            if (result != null) {
                level.postValue(result.getString("level"))
            }
        }
    }

    private fun loadCategories() {
        firestore.collection("categories/${user?.uid}/categories").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val c = ArrayList<Category>()

            result?.forEach {

                val name = it.getString("name")
                if (name != null)
                    c.add(Category.getCategoryByString(name))
            }

            categories.postValue(c)
        }
    }

    private fun loadAllCategories() {
        firestore.collection("categories").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val c = ArrayList<Category>()

            result?.forEach {

                val name = it.getString("name")
                if (name != null)
                    c.add(Category.getCategoryByString(name))
            }

            allCategories.postValue(c)
        }
    }
}