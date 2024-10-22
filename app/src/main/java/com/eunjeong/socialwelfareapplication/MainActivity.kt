package com.eunjeong.socialwelfareapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eunjeong.socialwelfareapplication.fragments.ContactFragment
import com.eunjeong.socialwelfareapplication.fragments.MonitoringFragment
import com.eunjeong.socialwelfareapplication.fragments.NoticeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, NoticeFragment())
            .commit()

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(NoticeFragment())
                    true
                }

                R.id.navigation_address -> {
                    replaceFragment(ContactFragment())
                    true
                }

//                R.id.navigation_map -> {
//                    replaceFragment(MonitoringFragment())
//                    true
//                }

                R.id.navigation_monitoring -> {
                    replaceFragment(MonitoringFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()

        transaction.replace(R.id.fragmentContainer, fragment)
            .commit()

    }
}
