package exe.weazy.extendenglish.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.base.CaseFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import exe.weazy.extendenglish.tools.StringHelper
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.ui.fragment.AccountFragment
import exe.weazy.extendenglish.ui.fragment.LearnFragment
import exe.weazy.extendenglish.ui.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var learnFragment : LearnFragment
    private lateinit var accountFragment : AccountFragment
    private lateinit var settingsFragment : SettingsFragment
    private var active = Fragment()

    private lateinit var user : FirebaseUser
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    private lateinit var repeatYesterday : ArrayList<LearnWord>
    private lateinit var repeatTwoDays : ArrayList<LearnWord>
    private lateinit var repeatThreeDays : ArrayList<LearnWord>
    private lateinit var repeatFourDays : ArrayList<LearnWord>
    private lateinit var repeatLong : ArrayList<LearnWord>
    private lateinit var words : ArrayList<LearnWord>

    private lateinit var categories : ArrayList<Category>

    private var isAllWordsLoaded = false
    private var isAccountLoaded = false
    private var isCategoriesLoaded = false
    private var isRepeatYesterdayLoaded = false
    private var isRepeatTwoDaysLoaded = false
    private var isRepeatThreeDaysLoaded = false
    private var isRepeatFourDaysLoaded = false
    private var isRepeatLongLoaded = false

    private var level : String? = null
    private var progress : LearnProgress? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {

                if (learnFragment != active) {
                    changeFragment(learnFragment)
                    active = learnFragment
                }

                appbar_text.text = getString(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {

                if (accountFragment != active) {
                    changeFragment(accountFragment)
                    active = accountFragment
                }

                appbar_text.text = getString(R.string.title_account)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                if (settingsFragment != active) {
                    changeFragment(settingsFragment)
                    active = settingsFragment
                }

                appbar_text.text = getString(R.string.title_settings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = intent.getParcelableExtra("user")

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        UiHelper.hideView(fragment_layout)
        UiHelper.showView(loading_layout)

        navigation.menu.findItem(R.id.navigation_account).isChecked = true

        getUserData()
    }

    fun onLogOutButtonClick(v: View) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun loadFragments() {
        learnFragment = LearnFragment()
        var bundle = getInitLearnFragmentBundle()
        learnFragment.arguments = bundle

        accountFragment = AccountFragment()
        bundle = getInitAccountFragmentBundle()
        accountFragment.arguments = bundle

        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, learnFragment).hide(learnFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, settingsFragment).hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, accountFragment).commit()

        navigation.selectedItemId = R.id.navigation_account
    }

    private fun changeFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().show(fragment).hide(active).commit()
    }



    private fun getUserData() {
        getAllCategories()
        getAllWords()
        getRepeatLongWords()
        getRepeatYesterdayWords()
        getRepeatTwoDaysWords()
        getRepeatThreeDaysWords()
        getRepeatFourDaysWords()
        getUserFields()
    }

    private fun getAllWords() {
        firestore.collection("words").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            words = ArrayList()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }

            isAllWordsLoaded = true
            afterLoad()
        }
    }

    private fun getRepeatYesterdayWords() {
        firestore.collection("users/${user.uid}/repeatYesterday").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatYesterday = ArrayList()

            result?.forEach {
                repeatYesterday.add(it.toObject(LearnWord::class.java)!!)
            }

            isRepeatYesterdayLoaded = true
            afterLoad()
        }
    }

    private fun getRepeatTwoDaysWords() {
        firestore.collection("users/${user.uid}/repeatTwoDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatTwoDays = ArrayList()

            result?.forEach {
                repeatTwoDays.add(it.toObject(LearnWord::class.java)!!)
            }

            isRepeatTwoDaysLoaded = true
            afterLoad()
        }
    }

    private fun getRepeatThreeDaysWords() {
        firestore.collection("users/${user.uid}/repeatThreeDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatThreeDays = ArrayList()

            result?.forEach {
                repeatThreeDays.add(it.toObject(LearnWord::class.java)!!)
            }

            isRepeatThreeDaysLoaded = true
            afterLoad()
        }
    }

    private fun getRepeatFourDaysWords() {
        firestore.collection("users/${user.uid}/repeatFourDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatFourDays = ArrayList()

            result?.forEach {
                repeatFourDays.add(it.toObject(LearnWord::class.java)!!)
            }

            isRepeatFourDaysLoaded = true
            afterLoad()
        }
    }

    private fun getRepeatLongWords() {
        firestore.collection("users/${user.uid}/learned").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatLong = ArrayList()

            if (result != null) {
                result.shuffle()

                if (result.size > 7) {
                    for (i in 0..6) {
                        repeatLong.add(result[i].toObject(LearnWord::class.java)!!)
                    }
                } else {
                    result.forEach {
                        repeatLong.add(it.toObject(LearnWord::class.java)!!)
                    }
                }

            }

            isRepeatLongLoaded = true
            afterLoad()
        }
    }

    private fun getUserFields() {
        firestore.document("users/${user.uid}").get().addOnCompleteListener { documentSnapshot ->
            val result = documentSnapshot.result

            if (result != null) {
                level = result.getString("level")

                var pr = result.getString("progress")
                if (pr != null) {
                    pr = StringHelper.lowerCamelToUpperSnake(pr)
                    progress = LearnProgress.getLearnProgressByString(pr)
                }

                isAccountLoaded = true
                afterLoad()
            }
        }
    }

    private fun getAllCategories() {
        firestore.collection("users/${user.uid}/categories").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            categories = ArrayList()

            result?.forEach {

                val name = it.getString("name")
                if (name != null)
                    categories.add(Category.getCategoryByString(name))
            }

            isCategoriesLoaded = true
            afterLoad()
        }
    }



    private fun getInitLearnFragmentBundle() : Bundle {
        val bundle = Bundle()

        if (::repeatLong.isInitialized) {
            bundle.putParcelableArrayList("allWords", words)
        }

        if (::repeatYesterday.isInitialized) {
            bundle.putParcelableArrayList("repeatYesterday", repeatYesterday)
        }

        if (::repeatTwoDays.isInitialized) {
            bundle.putParcelableArrayList("repeatTwoDays", repeatTwoDays)
        }

        if (::repeatThreeDays.isInitialized) {
            bundle.putParcelableArrayList("repeatThreeDays", repeatThreeDays)
        }

        if (::repeatFourDays.isInitialized) {
            bundle.putParcelableArrayList("repeatFourDays", repeatFourDays)
        }

        if (::repeatLong.isInitialized) {
            bundle.putParcelableArrayList("repeatLong", repeatLong)
        }

        if (::categories.isInitialized) {
            bundle.putSerializable("categories", categories)
        }

        bundle.putSerializable("progress", progress)

        return bundle
    }

    private fun getInitAccountFragmentBundle() : Bundle {
        val bundle = Bundle()

        if (!user.displayName.isNullOrEmpty()) {
            bundle.putString("username", user.displayName)
        } else {
            bundle.putString("username", user.email)
        }

        bundle.putString("level", level)
        bundle.putSerializable("categories", categories)

        return bundle
    }



    private fun afterLoad() {
        if (isAllWordsLoaded && isAccountLoaded && isCategoriesLoaded && isRepeatFourDaysLoaded && isRepeatLongLoaded &&
                isRepeatThreeDaysLoaded && isRepeatTwoDaysLoaded && isRepeatYesterdayLoaded) {
            loadFragments()
            active = accountFragment

            UiHelper.hideView(loading_layout)
            UiHelper.showView(fragment_layout)
        }
    }
}
