package exe.weazy.extendenglish.tools

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.model.Progress
import exe.weazy.extendenglish.model.Word

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

        fun rewriteWordsByProgress(firestore : FirebaseFirestore, words: ArrayList<Word>, p : Progress) {
            var index = 0
            val collection = StringHelper.upperSnakeToLowerCamel(p.name)

            words.forEach {
                firestore.document("users/${auth.currentUser?.uid}/$collection/word-${index++}").set(it)
            }
        }

        fun writeWordsByProgress(firestore : FirebaseFirestore, words: ArrayList<Word>, p : Progress) {
            var index = words.size
            val collection = StringHelper.upperSnakeToLowerCamel(p.name)

            words.forEach {
                firestore.document("users/${auth.currentUser?.uid}/$collection/word-${index++}").set(it)
            }
        }

        fun writeProgress(firestore : FirebaseFirestore, p : Progress) {
            firestore.document("users/${auth.currentUser?.uid}").update("progress", StringHelper.upperSnakeToLowerCamel(p.name))
        }

        fun writeKnown(firestore : FirebaseFirestore, newKnow : ArrayList<Word>, knowCount : Int) {
            var index = knowCount

            newKnow.forEach {
                firestore.document("users/${auth.currentUser?.uid}/know/word-${index++}").set(it)
            }
        }
    }
}