package exe.weazy.extendenglish.tools

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word

class FirebaseHelper {

    companion object {

        private val auth = FirebaseAuth.getInstance()

        fun writeCategories(firestore : FirebaseFirestore, data : List<Category>, function : () -> Unit) {
            var index = 0
            var writed = 0

            data.forEach {
                val map = HashMap<String, String>()
                map["name"] = it.name

                firestore.document("users/${auth.currentUser?.uid}/categories/category-${index++}").set(map).addOnCompleteListener {
                    writed++

                    if (writed == data.size) {
                        function()
                    }
                }
            }
        }
    }
}