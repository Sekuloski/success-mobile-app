package mk.sekuloski.success

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import mk.sekuloski.success.adapter.MonthAdapter
import mk.sekuloski.success.adapter.PaymentAdapter
import mk.sekuloski.success.databinding.ActivityMainBinding
import mk.sekuloski.success.fragments.FinancesFragment
import mk.sekuloski.success.fragments.HomeFragment
import mk.sekuloski.success.fragments.WorkoutsFragment
import okhttp3.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment()
        val financesFragment = FinancesFragment()
        val workoutsFragment = WorkoutsFragment()

        setCurrentFragment(homeFragment)
        binding.bottomNavigationView.selectedItemId = R.id.miHome

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId)   {
                R.id.miFinances -> setCurrentFragment(financesFragment)
                R.id.miWorkouts -> setCurrentFragment(workoutsFragment)
                else -> setCurrentFragment(homeFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

}