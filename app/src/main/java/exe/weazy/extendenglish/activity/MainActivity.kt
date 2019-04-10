package exe.weazy.extendenglish.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.fragment.AccountFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var user : FirebaseUser

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_learn -> {
                appbar_text.text = getString(R.string.title_learn)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                loadFragment(AccountFragment())
                appbar_text.text = getString(R.string.title_account)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
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

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, fragment).commit()
    }
}
