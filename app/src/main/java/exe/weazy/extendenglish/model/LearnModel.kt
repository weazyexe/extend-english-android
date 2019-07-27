package exe.weazy.extendenglish.model

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import exe.weazy.extendenglish.arch.LearnContract
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.Progress
import exe.weazy.extendenglish.entity.Word
import exe.weazy.extendenglish.presenter.LearnPresenter
import exe.weazy.extendenglish.tools.StringHelper
import java.io.File

class LearnModel(private val presenter: LearnPresenter) : LearnContract.Model {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override fun loadAllWords(file: File) {
        var words = ArrayList<Word>()

        if (file.exists()) {
            val type = object : TypeToken<ArrayList<Word>>() {}.type
            words = Gson().fromJson(file.readText(), type)
            presenter.onLoadAllWordsFinished(words)
        } else {
            firestore.collection("words").get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result?.documents

                    result?.forEach {
                        words.add(it.toObject(Word::class.java)!!)
                    }

                    file.writeText(Gson().toJson(words))

                    presenter.onLoadAllWordsFinished(words)
                } else {
                    presenter.onLoadAllWordsFailure(querySnapshot.exception)
                }
            }
        }
    }

    override fun loadKnowWords() {
        firestore.collection("users/${auth.currentUser?.uid}/know").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result?.documents

                val words = ArrayList<Word>()

                result?.forEach {
                    words.add(it.toObject(Word::class.java)!!)
                }

                presenter.onLoadKnowWordsFinished(words)
            } else {
                presenter.onLoadKnowWordsFailure(querySnapshot.exception)
            }
        }
    }

    override fun loadLearnedWords() {
        firestore.collection("users/${auth.currentUser?.uid}/learned").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result?.documents

                val words = ArrayList<Word>()

                result?.forEach {
                    words.add(it.toObject(Word::class.java)!!)
                }

                presenter.onLoadLearnedWordsFinished(words)
            } else {
                presenter.onLoadLearnedWordsFailure(querySnapshot.exception)
            }
        }
    }

    override fun loadRepeatYesterdayWords() {
        firestore.collection("users/${auth.currentUser?.uid}/repeatYesterday").get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result?.documents

                    val words = ArrayList<Word>()

                    result?.forEach {
                        words.add(it.toObject(Word::class.java)!!)
                    }

                    words.shuffle()

                    presenter.onLoadRepeatYesterdayWordsFinished(words)
                } else {
                    presenter.onLoadRepeatYesterdayWordsFailure(querySnapshot.exception)
                }
            }
    }

    override fun loadRepeatTwoDaysWords() {
        firestore.collection("users/${auth.currentUser?.uid}/repeatTwoDays").get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result?.documents

                    val words = ArrayList<Word>()

                    result?.forEach {
                        words.add(it.toObject(Word::class.java)!!)
                    }

                    words.shuffle()

                    presenter.onLoadRepeatTwoDaysWordsFinished(words)
                } else {
                    presenter.onLoadRepeatTwoDaysWordsFailure(querySnapshot.exception)
                }
            }
    }

    override fun loadRepeatThreeDaysWords() {
        firestore.collection("users/${auth.currentUser?.uid}/repeatThreeDays").get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result?.documents

                    val words = ArrayList<Word>()

                    result?.forEach {
                        words.add(it.toObject(Word::class.java)!!)
                    }

                    words.shuffle()

                    presenter.onLoadRepeatThreeDaysWordsFinished(words)
                } else {
                    presenter.onLoadRepeatThreeDaysWordsFailure(querySnapshot.exception)
                }
            }
    }

    override fun loadRepeatFourDaysWords() {
        firestore.collection("users/${auth.currentUser?.uid}/repeatFourDays").get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result?.documents

                    val words = ArrayList<Word>()

                    result?.forEach {
                        words.add(it.toObject(Word::class.java)!!)
                    }

                    words.shuffle()

                    presenter.onLoadRepeatFourDaysWordsFinished(words)
                } else {
                    presenter.onLoadRepeatFourDaysWordsFailure(querySnapshot.exception)
                }
            }
    }

    override fun loadProgress() {
        firestore.document("users/${auth.currentUser?.uid}").get().addOnCompleteListener { documentSnapshot ->
            if (documentSnapshot.isSuccessful) {
                val result = documentSnapshot.result

                if (result != null) {
                    var pr = result.getString("progress")
                    if (pr != null) {
                        pr = StringHelper.lowerCamelToUpperSnake(pr)

                        presenter.onLoadProgressFinished(Progress.getLearnProgressByString(pr)!!)
                    }
                }
            } else {
                presenter.onLoadProgressFailure(documentSnapshot.exception)
            }
        }
    }

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

                presenter.onLoadCategoriesFinished(categories)
            } else {
                presenter.onLoadCategoriesFailure(querySnapshot.exception)
            }
        }
    }

    override fun loadLastActivity() {
        firestore.document("users/${auth.currentUser?.uid}").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result
                val format = SimpleDateFormat("MM dd yyyy HH:mm")
                format.timeZone = TimeZone.GMT_ZONE

                presenter.onLoadLastActivityFinished(format.parse(result?.get("lastActivity").toString()))
            } else {
                presenter.onLoadLastActivityFailure(querySnapshot.exception)
            }
        }
    }


    override fun writeLearned(words: ArrayList<Word>, learnedCount: Int) {
        var index = learnedCount
        val collection = StringHelper.upperSnakeToLowerCamel(Progress.LEARNED.name)

        words.forEach {
            firestore.document("users/${auth.currentUser?.uid}/$collection/word-${index++}").set(it)
        }
    }

    override fun writeWordsByProgress(words: ArrayList<Word>, p: Progress) {
        var index = 0
        val collection = StringHelper.upperSnakeToLowerCamel(p.name)

        words.forEach {
            firestore.document("users/${auth.currentUser?.uid}/$collection/word-${index++}").set(it)
        }
    }


    override fun writeProgress(p: Progress) {
        firestore.document("users/${auth.currentUser?.uid}")
            .update("progress", StringHelper.upperSnakeToLowerCamel(p.name))
    }

    override fun writeKnown(newKnow: ArrayList<Word>, knowCount: Int) {
        var index = knowCount

        newKnow.forEach {
            firestore.document("users/${auth.currentUser?.uid}/know/word-${index++}").set(it)
        }
    }
}