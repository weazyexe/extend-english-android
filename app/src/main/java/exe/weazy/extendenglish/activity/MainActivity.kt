package exe.weazy.extendenglish.activity

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.LearnWord
import exe.weazy.extendenglish.fragment.AccountFragment
import exe.weazy.extendenglish.fragment.LearnFragment
import exe.weazy.extendenglish.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var learnFragment : LearnFragment
    lateinit var accountFragment : AccountFragment
    lateinit var settingsFragment: SettingsFragment
    lateinit var user : FirebaseUser
    lateinit var firestore : FirebaseFirestore
    lateinit var words : ArrayList<LearnWord>

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
                if (!::accountFragment.isInitialized) accountFragment = AccountFragment()
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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = R.id.navigation_account

        user = intent.getParcelableExtra("user")
        firestore = FirebaseFirestore.getInstance()

        getAllWords()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment).commit()
    }

    private fun getAllWords() {
        firestore.collection("words").get().addOnCompleteListener { document ->
            val result = document.result?.documents

            words = ArrayList()

            result?.forEach {
                words.add(it.toObject(LearnWord::class.java)!!)
            }
        }
    }
}
