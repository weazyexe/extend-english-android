package exe.weazy.extendenglish.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import exe.weazy.extendenglish.tools.UiHelper
import exe.weazy.extendenglish.ui.fragment.AccountFragment
import exe.weazy.extendenglish.ui.fragment.LearnFragment
import exe.weazy.extendenglish.ui.fragment.SettingsFragment
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var viewModel: MainViewModel

    private lateinit var learnFragment : LearnFragment
    private lateinit var accountFragment : AccountFragment
    private lateinit var settingsFragment : SettingsFragment
    private var active = Fragment()

    private lateinit var repeatYesterday : ArrayList<LearnWord>
    private lateinit var repeatTwoDays : ArrayList<LearnWord>
    private lateinit var repeatThreeDays : ArrayList<LearnWord>
    private lateinit var repeatFourDays : ArrayList<LearnWord>
    private lateinit var learned : ArrayList<LearnWord>
    private lateinit var allWords : ArrayList<LearnWord>
    private lateinit var know : ArrayList<LearnWord>

    private lateinit var level : String
    private lateinit var progress : LearnProgress
    private lateinit var categories : ArrayList<Category>

    private var isAllWordsLoaded = false
    private var isCategoriesLoaded = false
    private var isRepeatYesterdayLoaded = false
    private var isRepeatTwoDaysLoaded = false
    private var isRepeatThreeDaysLoaded = false
    private var isRepeatFourDaysLoaded = false
    private var isLearnedLoaded = false
    private var isKnowLoaded = false
    private var isLevelLoaded = false
    private var isProgressLoaded = false

    private var newPosition = 1
    private var startingPosition = 1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {

                if (learnFragment != active) {
                    newPosition = 0
                    changeFragment(learnFragment)
                    active = learnFragment
                }

                appbar_text.text = getString(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {

                if (accountFragment != active) {
                    newPosition = 1
                    changeFragment(accountFragment)
                    active = accountFragment
                }

                appbar_text.text = getString(R.string.title_account)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                if (settingsFragment != active) {
                    newPosition = 2
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

        preLoad()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        UiHelper.hideView(fragment_layout)
        UiHelper.showView(loading_layout)

        navigation.menu.findItem(R.id.navigation_account).isChecked = true

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initializeObservers()
    }

    fun onLogOutButtonClick(v: View) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun initializeObservers() {
        val file = File(applicationContext.filesDir, "allWords")
        if (file.exists()) {
            val type = object : TypeToken<ArrayList<LearnWord>>() {}.type
            allWords = Gson().fromJson(file.readText(), type)

            Log.d("KEK", "Read from file")
            isAllWordsLoaded = true
            afterLoad()
        } else {
            initializeAllWordsObserver()
        }
        initializeLearnedWordsObserver()
        initializeKnowWordsObserver()
        initializeRepeatYesterdayWordsObserver()
        initializeRepeatTwoDaysWordsObserver()
        initializeRepeatThreeDaysWordsObserver()
        initializeRepeatFourDaysWordsObserver()
        initializeCategoriesObserver()
        initializeLevelObserver()
        initializeProgressObserver()
    }

    private fun initializeAllWordsObserver() {
        val allWordsLiveData = viewModel.getAllWords()
        allWordsLiveData.observe(this, Observer {
            allWords = it

            val file = File(applicationContext.filesDir, "allWords")
            file.writeText(Gson().toJson(allWords))

            Log.d("KEK", "Callback initAllWords")

            isAllWordsLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatYesterdayWordsObserver() {
        val repeatYesterdayWordsLiveData = viewModel.getRepeatYesterdayWords()
        repeatYesterdayWordsLiveData.observe(this, Observer {
            repeatYesterday = it
            isRepeatYesterdayLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatTwoDaysWordsObserver() {
        val repeatTwoDaysWordsLiveData = viewModel.getRepeatTwoDaysWords()
        repeatTwoDaysWordsLiveData.observe(this, Observer {
            repeatTwoDays = it
            isRepeatTwoDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatThreeDaysWordsObserver() {
        val repeatThreeDaysWordsLiveData = viewModel.getRepeatThreeDaysWords()
        repeatThreeDaysWordsLiveData.observe(this, Observer {
            repeatThreeDays = it
            isRepeatThreeDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeRepeatFourDaysWordsObserver() {
        val repeatFourDaysWordsLiveData = viewModel.getRepeatFourDaysWords()
        repeatFourDaysWordsLiveData.observe(this, Observer {
            repeatFourDays = it
            isRepeatFourDaysLoaded = true
            afterLoad()
        })
    }

    private fun initializeLearnedWordsObserver() {
        val learnedLiveData = viewModel.getLearnedWords()
        learnedLiveData.observe(this, Observer {
            learned = it
            isLearnedLoaded = true
            afterLoad()
        })
    }

    private fun initializeKnowWordsObserver() {
        val knowLiveData = viewModel.getKnowWords()
        knowLiveData.observe(this, Observer {
            know = it
            isKnowLoaded = true
            afterLoad()
        })
    }

    private fun initializeLevelObserver() {
        val levelLiveData = viewModel.getLevel()
        levelLiveData.observe(this, Observer {
            level = it
            isLevelLoaded = true
            afterLoad()
        })
    }

    private fun initializeProgressObserver() {
        val progressLiveData = viewModel.getProgress()
        progressLiveData.observe(this, Observer {
            progress = it
            isProgressLoaded = true
            afterLoad()
        })
    }

    private fun initializeCategoriesObserver() {
        val categoriesLiveData = viewModel.getCategories()
        categoriesLiveData.observe(this, Observer {
            categories = it
            isCategoriesLoaded = true
            afterLoad()
        })
    }



    private fun loadFragments() {
        learnFragment = LearnFragment()
        var bundle = getLearnFragmentBundle()
        learnFragment.arguments = bundle

        accountFragment = AccountFragment()
        bundle = getAccountFragmentBundle()
        accountFragment.arguments = bundle

        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, learnFragment).hide(learnFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, settingsFragment).hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_layout, accountFragment).commit()

        navigation.selectedItemId = R.id.navigation_account
    }

    private fun changeFragment(fragment : Fragment) {
        if (startingPosition > newPosition) {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_to_right, R.anim.slide_out_right).show(fragment).hide(active).commit()
        } else {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_to_left, R.anim.slide_out_left).show(fragment).hide(active).commit()
        }
        startingPosition = newPosition
    }



    private fun getLearnFragmentBundle() : Bundle {
        val bundle = Bundle()

        if (::allWords.isInitialized) {
            bundle.putParcelableArrayList("allWords", allWords)
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

        if (::learned.isInitialized) {
            bundle.putParcelableArrayList("learned", learned)
        }

        if (::know.isInitialized) {
            bundle.putParcelableArrayList("know", know)
        }

        if (::categories.isInitialized) {
            bundle.putSerializable("categories", categories)
        }

        bundle.putSerializable("progress", progress)

        return bundle
    }

    private fun getAccountFragmentBundle() : Bundle {
        val bundle = Bundle()

        if (!user?.displayName.isNullOrEmpty()) {
            bundle.putString("username", user?.displayName)
        } else {
            bundle.putString("username", user?.email)
        }

        bundle.putString("level", level)
        bundle.putSerializable("categories", categories)

        return bundle
    }



    private fun afterLoad() {
        if (isAllWordsLoaded && isLearnedLoaded && isKnowLoaded && isLevelLoaded && isProgressLoaded &&
                isCategoriesLoaded && isRepeatFourDaysLoaded && isLearnedLoaded &&
                isRepeatThreeDaysLoaded && isRepeatTwoDaysLoaded && isRepeatYesterdayLoaded) {

            loadFragments()
            active = accountFragment
            preLoad()

            UiHelper.hideView(loading_layout)
            UiHelper.showView(fragment_layout)
        }
    }

    private fun preLoad() {
        isAllWordsLoaded = false
        isCategoriesLoaded = false
        isRepeatYesterdayLoaded = false
        isRepeatTwoDaysLoaded = false
        isRepeatThreeDaysLoaded = false
        isRepeatFourDaysLoaded = false
        isLearnedLoaded = false
        isKnowLoaded = false
        isLevelLoaded = false
        isProgressLoaded = false
    }
}
