package exe.weazy.extendenglish.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import exe.weazy.extendenglish.R
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

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {
                //if (!::learnFragment.isInitialized) learnFragment = LearnFragment()
                loadFragment(LearnFragment())
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
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment).commit()
    }
}
