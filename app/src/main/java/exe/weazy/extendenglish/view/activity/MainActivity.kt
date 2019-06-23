package exe.weazy.extendenglish.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.view.fragment.AccountFragment
import exe.weazy.extendenglish.view.fragment.LearnFragment
import exe.weazy.extendenglish.view.fragment.SettingsFragment
import exe.weazy.extendenglish.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var viewModel: MainViewModel

    private lateinit var learnFragment : LearnFragment
    private lateinit var accountFragment : AccountFragment
    private lateinit var settingsFragment : SettingsFragment
    private var active = Fragment()

    private var newPosition = 1
    private var startingPosition = 1


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {
                newPosition = 0
                changeFragment(learnFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                newPosition = 1
                changeFragment(accountFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                newPosition = 2
                changeFragment(settingsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadFragments()

        if (savedInstanceState != null) {
            newPosition = savedInstanceState["new_position"] as Int
            startingPosition = savedInstanceState["starting_position"] as Int

            when (startingPosition) {
                0 -> {
                    changeFragment(learnFragment)
                    bottom_navigation.selectedItemId = R.id.navigation_learn
                }
                1 -> {
                    changeFragment(accountFragment)
                    bottom_navigation.selectedItemId = R.id.navigation_account
                }
                2 -> {
                    changeFragment(settingsFragment)
                    bottom_navigation.selectedItemId = R.id.navigation_settings
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .show(active)
            .commit()
    }

    override fun onStop() {
        supportFragmentManager.beginTransaction()
            .hide(active)
            .commit()

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.clear()

        outState.putInt("starting_position", startingPosition)
        outState.putInt("new_position", newPosition)

        super.onSaveInstanceState(outState)
    }


    private fun loadFragments() {
        learnFragment = LearnFragment()
        accountFragment = AccountFragment()
        settingsFragment = SettingsFragment()

        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, learnFragment).hide(learnFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, settingsFragment).hide(settingsFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, accountFragment).commit()

        bottom_navigation.selectedItemId = R.id.navigation_account
    }

    private fun changeFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.top_level_in, R.anim.top_level_out).show(fragment).hide(active).commit()
        startingPosition = newPosition
        active = fragment
    }
}
