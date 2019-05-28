package exe.weazy.extendenglish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import exe.weazy.extendenglish.tools.StringHelper

class MainViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    private lateinit var repeatYesterday : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var repeatTwoDays : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var repeatThreeDays : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var repeatFourDays : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var learned : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var allWords : MutableLiveData<ArrayList<LearnWord>>
    private lateinit var know : MutableLiveData<ArrayList<LearnWord>>

    private lateinit var level : MutableLiveData<String>
    private lateinit var progress : MutableLiveData<LearnProgress>
    private lateinit var categories : MutableLiveData<ArrayList<Category>>



    fun getAllWords() : LiveData<ArrayList<LearnWord>> {
        if (!::allWords.isInitialized) {
            allWords = MutableLiveData()
            loadAllWords()
        }

        return allWords
    }

    fun getRepeatYesterdayWords() : LiveData<ArrayList<LearnWord>> {
        if (!::repeatYesterday.isInitialized) {
            repeatYesterday = MutableLiveData()
            loadRepeatYesterdayWords()
        }

        return repeatYesterday
    }

    fun getRepeatTwoDaysWords() : LiveData<ArrayList<LearnWord>> {
        if (!::repeatTwoDays.isInitialized) {
            repeatTwoDays = MutableLiveData()
            loadRepeatTwoDaysWords()
        }

        return repeatTwoDays
    }

    fun getRepeatThreeDaysWords() : LiveData<ArrayList<LearnWord>> {
        if (!::repeatThreeDays.isInitialized) {
            repeatThreeDays = MutableLiveData()
            loadRepeatThreeDaysWords()
        }

        return repeatThreeDays
    }

    fun getRepeatFourDaysWords() : LiveData<ArrayList<LearnWord>> {
        if (!::repeatFourDays.isInitialized) {
            repeatFourDays = MutableLiveData()
            loadRepeatFourDaysWords()
        }

        return repeatFourDays
    }

    fun getLearnedWords() : LiveData<ArrayList<LearnWord>> {
        if (!::learned.isInitialized) {
            learned = MutableLiveData()
            loadLearnedWords()
        }

        return learned
    }

    fun getKnowWords() : LiveData<ArrayList<LearnWord>> {
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

    fun getProgress() : LiveData<LearnProgress> {
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

    fun setCategories(categories : ArrayList<Category>) {
        this.categories = MutableLiveData()
        this.categories.postValue(categories)
    }

    fun setAllWords(allWords : ArrayList<LearnWord>) {
        this.allWords = MutableLiveData()
        this.allWords.postValue(allWords)
    }



    private fun loadAllWords() {
        firestore.collection("words").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            allWords.postValue(words)
        }
    }

    private fun loadRepeatYesterdayWords() {
        firestore.collection("users/${user?.uid}/repeatYesterday").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            repeatYesterday.postValue(words)
        }
    }

    private fun loadRepeatTwoDaysWords() {
        firestore.collection("users/${user?.uid}/repeatTwoDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            repeatTwoDays.postValue(words)
        }
    }

    private fun loadRepeatThreeDaysWords() {
        firestore.collection("users/${user?.uid}/repeatThreeDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            repeatThreeDays.postValue(words)
        }
    }

    private fun loadRepeatFourDaysWords() {
        firestore.collection("users/${user?.uid}/repeatFourDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            repeatFourDays.postValue(words)
        }
    }

    private fun loadLearnedWords() {
        firestore.collection("users/${user?.uid}/learned").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            learned.postValue(words)
        }
    }

    private fun loadKnowWords() {
        firestore.collection("users/${user?.uid}/know").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            val words = ArrayList<LearnWord>()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
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
                    progress.postValue(LearnProgress.getLearnProgressByString(pr))
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
}