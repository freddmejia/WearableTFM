package tm.wearable.wearabletfm.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.*
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.ActivityOauthFitbitBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding


@AndroidEntryPoint
class OAuthFitbitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOauthFitbitBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOauthFitbitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()

        getUrlData(intent.extras!!)
    }

    fun setUpToolBar(){
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.login_fitbit_account)
        toolbarAppBinding.calendar.isVisible = false
        toolbarAppBinding.profile.isVisible = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->
            {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getUrlData(bundle: Bundle){
        if (bundle != null){
            try {
                runOnUiThread {
                    binding.webView.clearCache(true)
                }

                val url = bundle.getString("url").toString()

                Log.e("", "onPageStarted:url "+url )
                binding.webView.settings.javaScriptEnabled = true
                binding.webView.webViewClient = object : WebViewClient() {
                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        // Limpiar la caché del WebView
                        runOnUiThread {
                            view?.clearCache(true)
                        }
                        //view?.clearCache(true)

                        // Continuar con la solicitud de carga de recursos normalmente
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        Log.e("", "onPageStarted: ", )
                    }
                    override fun onPageFinished(view: WebView?, url: String?) {
                        // La carga de la URL ha finalizado
                        Log.e("", "onPageFinished: ", )
                        if (view?.url == url) {
                            Log.e("", "onPageFinished: correctamente ", )
                            // La URL se ha cargado correctamente
                            // Realiza aquí las acciones adicionales que necesites
                        } else {
                            Log.e("", "onPageFinished: errores ", )
                            // Ocurrió un error al cargar la URL
                            // Realiza aquí el manejo de errores correspondiente
                        }
                    }
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        Log.e("", "onReceivedError: ", )
                        // Se produjo un error al cargar la URL
                        // Realiza aquí el manejo de errores correspondiente
                    }
                }

                binding.webView.loadUrl(url)


            }catch (e: java.lang.Exception){
                Log.e("", "getExtraDeviceData: "+e.message )
            }
        }
    }

}