package mk.sekuloski.success

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import mk.sekuloski.success.data.remote.services.FinancesService
import mk.sekuloski.success.databinding.ActivityMainBinding
import mk.sekuloski.success.fragments.finances.FinancesFragment
import mk.sekuloski.success.fragments.HomeFragment
import mk.sekuloski.success.fragments.workouts.WorkoutsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val client = FinancesService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment(client)
        setCurrentFragment(homeFragment)
        binding.bottomNavigationView.selectedItemId = R.id.miHome

        val financesFragment = FinancesFragment(client)
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