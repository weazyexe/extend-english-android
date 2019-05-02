package exe.weazy.extendenglish.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.Category
import exe.weazy.extendenglish.entity.LearnProgress
import exe.weazy.extendenglish.entity.LearnWord
import exe.weazy.extendenglish.fragment.AccountFragment
import exe.weazy.extendenglish.fragment.LearnFragment
import exe.weazy.extendenglish.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var learnFragment : LearnFragment
    private lateinit var accountFragment : AccountFragment
    private lateinit var settingsFragment: SettingsFragment

    private lateinit var user : FirebaseUser
    private var firestore = FirebaseFirestore.getInstance()
    private var firebaseAuth = FirebaseAuth.getInstance()

    private lateinit var repeatYesterday : ArrayList<LearnWord>
    private lateinit var repeatTwoDays : ArrayList<LearnWord>
    private lateinit var repeatThreeDays : ArrayList<LearnWord>
    private lateinit var repeatFourDays : ArrayList<LearnWord>
    private lateinit var repeatLong : ArrayList<LearnWord>

    private lateinit var categories : ArrayList<Category>

    private var isAccountLoaded = false
    private var isCategoriesLoaded = false

    private var level : String? = null
    private var progress : LearnProgress? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {
                learnFragment = LearnFragment()

                val bundle = getInitLearnFragmentBundle()

                learnFragment.arguments = bundle

                loadFragment(learnFragment)

                appbar_text.text = getString(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                accountFragment = AccountFragment()
                val bundle = getInitAccountFragmentBundle()
                accountFragment.arguments = bundle

                loadFragment(accountFragment)

                appbar_text.text = getString(R.string.title_account)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                if (!::settingsFragment.isInitialized) settingsFragment = SettingsFragment()
                loadFragment(settingsFragment)
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

        fragment_layout.visibility = View.GONE
        loading_layout.visibility = View.VISIBLE

        navigation.menu.findItem(R.id.navigation_account).isChecked = true

        getUserData()
    }

    fun onLogOutButtonClick(v: View) {
        firebaseAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment).commit()
    }

    private fun getUserData() {
        getAllCategories()
        getRepeatYesterdayWords()
        getRepeatTwoDaysWords()
        getRepeatThreeDaysWords()
        getRepeatFourDaysWords()
        getRepeatLongTimeAgoWords()
        getUserFields()
    }

    private fun getRepeatYesterdayWords() {
        firestore.collection("users/${user.uid}/repeatYesterday").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatYesterday = ArrayList()

            result?.forEach {
                repeatYesterday.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }

    private fun getRepeatTwoDaysWords() {
        firestore.collection("users/${user.uid}/repeatTwoDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatTwoDays = ArrayList()

            result?.forEach {
                repeatTwoDays.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }

    private fun getRepeatThreeDaysWords() {
        firestore.collection("users/${user.uid}/repeatThreeDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatThreeDays = ArrayList()

            result?.forEach {
                repeatThreeDays.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }

    private fun getRepeatFourDaysWords() {
        firestore.collection("users/${user.uid}/repeatFourDays").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            repeatFourDays = ArrayList()

            result?.forEach {
                repeatFourDays.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }

    private fun getRepeatLongTimeAgoWords() {
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
        }
    }

    private fun getUserFields() {
        firestore.document("users/${user.uid}").get().addOnCompleteListener { documentSnapshot ->
            val result = documentSnapshot.result

            if (result != null) {
                level = result.getString("level")

                val pr = result.getString("progress")
                if (pr != null) {
                    progress = LearnProgress.getLearnProgressByString(pr)
                }

                isAccountLoaded = true
            }

            if (isAccountLoaded && isCategoriesLoaded) {
                navigation.selectedItemId = R.id.navigation_account
                fragment_layout.visibility = View.VISIBLE
                loading_layout.visibility = View.GONE
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

            if (isAccountLoaded && isCategoriesLoaded) {
                navigation.selectedItemId = R.id.navigation_account
                fragment_layout.visibility = View.VISIBLE
                loading_layout.visibility = View.GONE
            }
        }
    }

    private fun getInitLearnFragmentBundle() : Bundle {
        val bundle = Bundle()

        if (::repeatLong.isInitialized) {
            bundle.putParcelableArrayList("words", repeatLong)
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
}
