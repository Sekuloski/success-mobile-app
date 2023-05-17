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
import mk.sekuloski.success.models.Month
import okhttp3.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val api = API()
        val months: ArrayList<Month> = api.getMonths()

        while(true)
        {
            if (months.size == 0)
            {
                continue
            }

            break
        }
        val homeFragment = HomeFragment()
        setCurrentFragment(homeFragment)
        binding.bottomNavigationView.selectedItemId = R.id.miHome


        val financesFragment = FinancesFragment(months)
        val workoutsFragment = WorkoutsFragment()

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
            addToBackStack(null)
            commit()
        }

}