package br.com.raveline.mystorephi.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.ActivityMainBinding
import br.com.raveline.mystorephi.utils.razorKeyId
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),PaymentResultListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPaymentSuccess(result: String?) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(errorCode: Int, errorMessage: String?) {
        Toast.makeText(this, "$errorMessage with $errorCode", Toast.LENGTH_SHORT).show()
        Log.e("TAGError", "onPaymentError: $errorMessage and $errorCode" )
    }
}