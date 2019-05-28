package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.model.Category

class LoginViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var allCategories : MutableLiveData<ArrayList<Category>>



    fun getCategories() : LiveData<ArrayList<Category>> {
        if (!::allCategories.isInitialized) {
            allCategories = MutableLiveData()
            loadCategories()
        }

        return allCategories
    }

    private fun loadCategories() {
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