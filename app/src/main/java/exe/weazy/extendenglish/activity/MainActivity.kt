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
    private lateinit var firestore : FirebaseFirestore
    private lateinit var words : ArrayList<LearnWord>
    private lateinit var categories : ArrayList<Category>
    private var level : String? = null
    private var firebaseAuth = FirebaseAuth.getInstance()

    private var isAccountLoaded = false
    private var isCategoriesLoaded = false

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {
                //if (!::learnFragment.isInitialized) learnFragment = LearnFragment()

                learnFragment = LearnFragment()

                if (!::words.isInitialized) words = ArrayList()

                val bundle = Bundle()
                bundle.putParcelableArrayList("words", words)
                learnFragment.arguments = bundle

                loadFragment(learnFragment)

                appbar_text.text = getString(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {

                val bundle = Bundle()
                bundle.putString("username", user.email)
                bundle.putString("level", level)
                bundle.putSerializable("categories", categories)

                accountFragment = AccountFragment()
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
        firestore = FirebaseFirestore.getInstance()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fragment_layout.visibility = View.GONE
        loading_layout.visibility = View.VISIBLE

        getAllWords()
        getAllCategories()
        getLevel()
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

    private fun getAllWords() {
        firestore.collection("words").get().addOnCompleteListener { querySnapshot ->
            val result = querySnapshot.result?.documents

            words = ArrayList()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }

    private fun getLevel() {
        firestore.document("users/${user.uid}").get().addOnCompleteListener { documentSnapshot ->
            level = documentSnapshot.result?.getString("level")

            isAccountLoaded = true

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
}
