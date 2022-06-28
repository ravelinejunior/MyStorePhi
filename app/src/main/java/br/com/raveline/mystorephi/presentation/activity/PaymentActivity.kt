package br.com.raveline.mystorephi.presentation.activity

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.data.model.FeaturesModel
import br.com.raveline.mystorephi.databinding.ActivityPaymentBinding
import br.com.raveline.mystorephi.databinding.FragmentPaymentBinding
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.HomeViewModelFactory
import br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory.UserViewModelFactory
import br.com.raveline.mystorephi.utils.SystemFunctions
import br.com.raveline.mystorephi.utils.razorKeyId
import br.com.raveline.mystorephi.utils.totalPriceBundleKey
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.round


@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(),PaymentResultListener {

    private var _itemBinding: ActivityPaymentBinding? = null
    private val itemBinding get() = _itemBinding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var args: FeaturesModel? = null

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { homeViewModelFactory }

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private val userViewModel: UserViewModel by viewModels {
        userViewModelFactory
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _itemBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment)
        setContentView(itemBinding.root)

        Checkout.preload(this)

        itemBinding.apply {
            toolbarItemDetailFragment.setNavigationOnClickListener {
            }

            buttonPaymentFragmentCheckout.setOnClickListener {
                startPayment()
            }

        }


    }

    override fun onStart() {
        super.onStart()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            homeViewModel.featureFlow.collectLatest { feature ->
                if (feature != null) {
                    args = feature
                } else {
                    getFeaturesFromJson()
                }
                displayData()
            }
        }
    }

    private fun displayData() {
        if (args != null) {
            val product = args!!
            val discount = product.price * 0.10
            val total = product.price - discount
            itemBinding.apply {
                textViewPaymentFragmentSubTotal.text =
                    SystemFunctions.replaceDotToComma(product.price)
                textViewPaymentFragmentDiscount.text = SystemFunctions.replaceDotToComma(discount)
                textViewPaymentFragmentTotal.text = SystemFunctions.replaceDotToComma(total)
                textViewPaymentFragmentShipping.text =
                    SystemFunctions.replaceDotToComma(total * 0.05)
            }
        }
    }

    private fun getFeaturesFromJson() {
        if (sharedPreferences.contains(totalPriceBundleKey)) {
            val featureJson = sharedPreferences.getString(totalPriceBundleKey, null)
            val gson = Gson()
            args = (gson.fromJson(featureJson, FeaturesModel::class.java))
        }
    }


    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        co.setKeyID(razorKeyId)

        try {
            val product = args!!
            val discount = product.price * 0.10
            val total = round(product.price - discount).toInt() * 100
            val options = JSONObject()
            options.put("name", product.name)
            options.put("description", product.description.substring(0,200))
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "USD")
            //options.put("order_id", "order_9A33XWu170gUtm")
            options.put("amount", total)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", userViewModel.userModel?.email)
            prefill.put("contact", userViewModel.userModel?.phoneNumber)

            options.put("prefill", prefill)
            co.open(activity, options)

            Log.i("TAGPay", "startPayment: $options")
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _itemBinding = null
    }

    override fun onPaymentSuccess(result: String?) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(errorCode: Int, errorMessage: String?) {
        Toast.makeText(this, "$errorMessage with $errorCode", Toast.LENGTH_SHORT).show()
        Log.i("TAGPay", "startPayment: $errorCode $errorMessage ")
    }
}