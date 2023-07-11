package tm.wearable.wearabletfm.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import tm.wearable.wearabletfm.data.interfaces.UIUserProfile
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.databinding.UpdateUserAlertBinding
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.FriendsRequestAdapter
import tm.wearable.wearabletfm.data.interfaces.UIObserverFriendGeoZone
import tm.wearable.wearabletfm.data.interfaces.UIObserverFriendRequest
import tm.wearable.wearabletfm.data.interfaces.UIUserHealth
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.databinding.AddGeozoneAlertBinding
import tm.wearable.wearabletfm.databinding.FriendAlertBinding
import tm.wearable.wearabletfm.databinding.OauthFitbitAlertBinding
import tm.wearable.wearabletfm.databinding.UpdateHealthAlertBinding
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import androidx.appcompat.app.AlertDialog

class WearableDialogs {
    companion object {
        fun update_profile_user(context: Context, observer: UIUserProfile, user: User, message: String){
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = UpdateUserAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.update_user_alert,null))

            binding.etName.setText(user.name)
            binding.etEmail.setText(user.email)

            binding.cvLogout.setOnClickListener {
                observer.logout(user = user)
            }
            alertDialogBuilder.setButton(
                Dialog.BUTTON_POSITIVE,context.resources.getString(R.string.update_user),
                DialogInterface.OnClickListener { dialogInterface, i ->

                    val  email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()
                    val username = binding.etName.text.toString()
                    if (email.trim().isEmpty() && username.trim().isEmpty()){
                        observer.showMessage(message = message)
                        return@OnClickListener
                    }
                    user.name = username
                    user.password = password
                    user.email = email

                    observer.save(user = user)
                    dialogInterface.dismiss()
                })

            alertDialogBuilder.setButton(
                Dialog.BUTTON_NEGATIVE,context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })


            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.red))
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.blue_f4))
        }

        fun update_health_user(context: Context, observer: UIUserHealth, health: Health, message: String, fragmentManager: FragmentManager){
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = UpdateHealthAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.update_health_alert,null))
            val pattern = "yyyy-MM-dd"
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())

            binding.weightUser.setText(health.weight)
            binding.sizeUser.setText(health.height)
            binding.oldUser.setText(health.birthay)
            binding.cvOld.setOnClickListener {
                val calendar = Calendar.getInstance()
                val actualDate = GregorianCalendar()
                var  gregorianCalendar = actualDate.time
                //val calendar = actualDate
                try {
                    gregorianCalendar = if (health.yearOld > 0) formatter.parse(health.birthay) else
                        actualDate.time
                }catch (e: java.lang.Exception){
                    gregorianCalendar = actualDate.time
                }
                calendar.time = gregorianCalendar
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)

                    // Actualizar el TextView con la nueva fecha
                    binding.oldUser.text = formatter.format(calendar.time)

                }, year, month, dayOfMonth)

                datePicker.datePicker.maxDate = actualDate.timeInMillis
                datePicker.show()


            }
            alertDialogBuilder.setButton(
                Dialog.BUTTON_POSITIVE,context.resources.getString(R.string.update_user),
                DialogInterface.OnClickListener { dialogInterface, i ->

                    val weight = binding.weightUser.text.toString()
                    val height = binding.sizeUser.text.toString()
                    val birthay = binding.oldUser.text.toString()
                    if (weight.trim().isEmpty() && height.trim().isEmpty() && birthay.trim().isEmpty()){
                        observer.showMessage(message = message)
                        return@OnClickListener
                    }
                    health.weight = weight
                    health.height = height
                    health.birthay = birthay

                    observer.save(health = health)
                    dialogInterface.dismiss()
                }
            )

            alertDialogBuilder.setButton(
                Dialog.BUTTON_NEGATIVE,context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            )


            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.red))
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.blue_f4))
        }
        fun friends_request_dialog(context: Context, observer: UIObserverFriendRequest, friendsR: ArrayList<Friend>){
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = FriendAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.friend_alert,null))

            var adapter = FriendsRequestAdapter(context = context, list =  friendsR, observer = observer, alertDialogBuilder = alertDialogBuilder)
            binding.rvFriends.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            binding.rvFriends.adapter = adapter
            alertDialogBuilder.setButton(Dialog.BUTTON_NEGATIVE,context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.red))
        }
        fun friends_geozone_dialog(friend: Friend, context: Context, observer: UIObserverFriendGeoZone){
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = AddGeozoneAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.add_geozone_alert,null))
            binding.title.text = context.resources.getString(R.string.geozone_alert_title) + " "+friend.name
            alertDialogBuilder.setButton(Dialog.BUTTON_POSITIVE,context.resources.getString(R.string.accept),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    observer.addedGeo()
                    dialogInterface.dismiss()
                })
            alertDialogBuilder.setButton(Dialog.BUTTON_NEGATIVE,context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.red))
            alertDialogBuilder.getButton(Dialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.blue_a7))
        }




        fun openURLEnWebView2(context: Context, url: String) {
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = OauthFitbitAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.oauth_fitbit_alert, null))

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.domStorageEnabled = true
            binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

            binding.webView.loadUrl(url)

            alertDialogBuilder.setButton(
                Dialog.BUTTON_NEGATIVE,
                context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            )

            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }


        fun openURLEnWebView(context: Context, url: String) {

            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context).setCancelable(false).create()
            val binding = OauthFitbitAlertBinding.bind(LayoutInflater.from(context).inflate(R.layout.oauth_fitbit_alert,null))


            // Crear un objeto de confianza SSL personalizado que no realiza ninguna verificación
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    // No realizar ninguna verificación
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    // No realizar ninguna verificación
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf() // No emitir certificados de confianza
                }
            })

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.domStorageEnabled = true
            binding.webView.settings.javaScriptCanOpenWindowsAutomatically = true
            binding.webView.settings.setSupportMultipleWindows(true)
            binding.webView.settings.setJavaScriptCanOpenWindowsAutomatically(true)
            binding.webView.requestFocus()
            binding.webView.requestFocusFromTouch()


            //binding.webView.apply {
                // Configurar el WebViewClient para cargar la URL dentro del WebView
                binding.webView.webViewClient = object : WebViewClient() {
                    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: android.net.http.SslError?) {
                        Log.e("", "onReceivedSslError: ", )
                        handler?.proceed() // Permitir todos los errores de SSL
                    }

                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        Log.e("", "shouldOverrideUrlLoading: ", )
                        view.loadUrl(url)
                        return true
                    }

                    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                        Log.e("WebView", "Error: $errorCode, Description: $description, URL: $failingUrl")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        binding.webView.evaluateJavascript("document.getElementById('myInputField').focus();", null)

                    }
                }

                // Cargar la URL en el WebView
                //loadUrl(url)
            //}
            // Configurar la fábrica de confianza SSL personalizada
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true } // Aceptar todos los nombres de host

            binding.webView.loadUrl(url)

            alertDialogBuilder.setButton(Dialog.BUTTON_NEGATIVE,context.resources.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            alertDialogBuilder.setView(binding.root)
            alertDialogBuilder.show()
            alertDialogBuilder.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        }
    }


}