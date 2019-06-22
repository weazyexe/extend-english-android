package exe.weazy.extendenglish.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.model.Category
import exe.weazy.extendenglish.model.Progress
import exe.weazy.extendenglish.model.Word
import exe.weazy.extendenglish.tools.StringHelper
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var repeatYesterday : MutableLiveData<ArrayList<Word>>
    private lateinit var repeatTwoDays : MutableLiveData<ArrayList<Word>>
    private lateinit var repeatThreeDays : MutableLiveData<ArrayList<Word>>
    private lateinit var repeatFourDays : MutableLiveData<ArrayList<Word>>
    private lateinit var learned : MutableLiveData<ArrayList<Word>>
    private lateinit var allWords : MutableLiveData<ArrayList<Word>>
    private lateinit var know : MutableLiveData<ArrayList<Word>>
    private lateinit var repeatLong : MutableLiveData<ArrayList<Word>>
    private lateinit var learnToday : MutableLiveData<ArrayList<Word>>

    private lateinit var level : MutableLiveData<String>
    private lateinit var progress : MutableLiveData<Progress>
    private lateinit var categories : MutableLiveData<ArrayList<Category>>
    private lateinit var lastActivity : MutableLiveData<Date>
    private lateinit var allCategories : MutableLiveData<ArrayList<Category>>



    fun getAllCategories() : LiveData<ArrayList<Category>> {
        if (!::allCategories.isInitialized) {
            allCategories = MutableLiveData()
            loadAllCategories()
        }

        return allCategories
    }

    fun getAllWords() : LiveData<ArrayList<Word>> {
        if (!::allWords.isInitialized) {
            allWords = MutableLiveData()
            loadAllWords()
        }

        return allWords
    }

    fun getRepeatYesterdayWords() : LiveData<ArrayList<Word>> {
        if (!::repeatYesterday.isInitialized) {
            repeatYesterday = MutableLiveData()
            loadRepeatYesterdayWords()
        }

        return repeatYesterday
    }

    fun getRepeatTwoDaysWords() : LiveData<ArrayList<Word>> {
        if (!::repeatTwoDays.isInitialized) {
            repeatTwoDays = MutableLiveData()
            loadRepeatTwoDaysWords()
        }

        return repeatTwoDays
    }

    fun getRepeatThreeDaysWords() : LiveData<ArrayList<Word>> {
        if (!::repeatThreeDays.isInitialized) {
            repeatThreeDays = MutableLiveData()
            loadRepeatThreeDaysWords()
        }

        return repeatThreeDays
    }

    fun getRepeatFourDaysWords() : LiveData<ArrayList<Word>> {
        if (!::repeatFourDays.isInitialized) {
            repeatFourDays = MutableLiveData()
            loadRepeatFourDaysWords()
        }

        return repeatFourDays
    }

    fun getLearnedWords() : LiveData<ArrayList<Word>> {
        if (!::learned.isInitialized) {
            learned = MutableLiveData()
            loadLearnedWords()
        }

        return learned
    }

    fun getKnowWords() : LiveData<ArrayList<Word>> {
        if (!::know.isInitialized) {
            know = MutableLiveData()
            loadKnowWords()
        }

        return know
    }

    fun getLevel() : LiveData<String> {
        if (!::level.isInitialized) {
            level = MutableLiveData()
            loadLevel()
        }

        return level
    }

    fun getProgress() : LiveData<Progress> {
        if (!::progress.isInitialized) {
            progress = MutableLiveData()
            loadProgress()
        }

        return progress
    }

    fun getCategories() : LiveData<ArrayList<Category>> {
        if (!::categories.isInitialized) {
            categories = MutableLiveData()
            loadCategories()
        }

        return categories
    }

    fun getLearnToday() : LiveData<ArrayList<Word>> {
        if (!::learnToday.isInitialized) {
            learnToday = MutableLiveData()
            learnToday.postValue(ArrayList())
        }

        return learnToday
    }

    fun getRepeatLong() : LiveData<ArrayList<Word>> {
        if (!::repeatLong.isInitialized) {
            repeatLong = MutableLiveData()
            repeatLong.postValue(ArrayList())
        }

        return repeatLong
    }

    fun getLastActivity() : LiveData<Date> {
        if (!::lastActivity.isInitialized) {
            lastActivity = MutableLiveData()
            loadLastActivity()
        }

        return lastActivity
    }



    fun setCategories(ctgrs : ArrayList<Category>) {
        if (!::categories.isInitialized) {
            categories = MutableLiveData()
        }
        categories.postValue(ctgrs)
    }

    fun setAllWords(words : ArrayList<Word>) {
        if (!::allWords.isInitialized) {
            allWords = MutableLiveData()
        }
        allWords.postValue(words)
    }

    fun setLearnToday(words : ArrayList<Word>) {
        if (!::learnToday.isInitialized) {
            learnToday = MutableLiveData()
        }
        learnToday.postValue(words)
    }

    fun setRepeatLong(words : ArrayList<Word>) {
        if (!::repeatLong.isInitialized) {
            repeatLong = MutableLiveData()
        }
        repeatLong.postValue(words)
    }



    private fun loadAllWords() {
        firestore.collection("words").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            allWords.postValue(words)
        }
    }

    private fun loadRepeatYesterdayWords() {
        firestore.collection("users/${user?.uid}/repeatYesterday").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            repeatYesterday.postValue(words)
        }
    }

    private fun loadRepeatTwoDaysWords() {
        firestore.collection("users/${user?.uid}/repeatTwoDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            repeatTwoDays.postValue(words)
        }
    }

    private fun loadRepeatThreeDaysWords() {
        firestore.collection("users/${user?.uid}/repeatThreeDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            repeatThreeDays.postValue(words)
        }
    }

    private fun loadRepeatFourDaysWords() {
        firestore.collection("users/${user?.uid}/repeatFourDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            repeatFourDays.postValue(words)
        }
    }

    private fun loadLearnedWords() {
        firestore.collection("users/${user?.uid}/learned").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            learned.postValue(words)
        }
    }

    private fun loadKnowWords() {
        firestore.collection("users/${user?.uid}/know").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<Word>()

            result?.forEach {
                words.add(it.toObject(Word::class.java)!!)
            }

            know.postValue(words)
        }
    }

    private fun loadProgress() {
        firestore.document("users/${user?.uid}").get().addOnCompleteListener { documentSnapshot ->
            val result = documentSnapshot.result

            if (result != null) {
                var pr = result.getString("progress")
                if (pr != null) {
                    pr = StringHelper.lowerCamelToUpperSnake(pr)
                    progress.postValue(Progress.getLearnProgressByString(pr))
                }
            }
        }
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
        firestore.collection("users/${user?.uid}/categories").get().addOnCompleteListener { querySnapshot ->
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

    private fun loadLastActivity() {
        firestore.document("users/${user?.uid}").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result
            val format = SimpleDateFormat("MM dd yyyy HH:mm")

            lastActivity.postValue(format.parse(result?.get("lastActivity").toString()))
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