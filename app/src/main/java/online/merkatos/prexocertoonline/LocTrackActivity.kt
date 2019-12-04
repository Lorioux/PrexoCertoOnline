package online.merkatos.prexocertoonline

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import online.merkatos.prexocertoonline.R.id
import online.merkatos.prexocertoonline.R.layout

class LocTrackActivity : AppCompatActivity() {
    private var permissions: Array<String>? = null
    private val permReqCode: Int = 100
    private var mDisplayerTextView: TextView? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationProvider: LocationProvider? = null
    private var mLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.home)

        val toolbar: Toolbar = findViewById<View>(id.main__appBar_toolbar) as Toolbar

        val searchView = SearchView(this)
        searchView.setIconifiedByDefault(false)
        searchView.queryHint = toolbar.title
        searchView.layoutParams = (ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT))
        toolbar.addView(searchView)
        setSupportActionBar(toolbar)


        /**TODO 001:
         * (1)Instanciar  a vista de texto para mostrar o log
         */
        mDisplayerTextView = findViewById<View>(id.main__mdisplayer_txtv) as TextView

        /**TODO 002:
         * (1)Criar um mecanismo de monitorizacao da localizacao do dispositivo do utilizador.
         */
        mSetUpLocationManager()


        /**TODO 003:
         * (1)Solicitar a permissão para acesso a localização
         */
        mSolicitarPermissaoDaLocalizacao(arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION))

        /**TODO 004:
         * Manter a localização do dispositivo actualizada no sistema.
         */
        val mFabToLocate = findViewById<View>(id.main__fab_locate) as FloatingActionButton
        mFabToLocate.setOnClickListener { mActualizarPosicaoDoDispositivo(mLocationManager) }

    }

    /**
     * Metodo invocado para configurar o Gestor de Localizavao da aplicacao
     */
    private fun mSetUpLocationManager() {
        /**TODO 001:
         * (1) Verificar se a opção de Mocklocation do dispositivo está activa. Caso não, solicitar a sua activação.
         * (2) Proceder a configuração do gestor de localização provendo os provedores.
         */
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria()
        criteria.accuracy = 1
        criteria.isAltitudeRequired = true
        criteria.isSpeedRequired = true
        criteria.isCostAllowed = false

        permissions = arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION)
        requestPermissions(permissions, permReqCode)

        val providers: (List<String>) = mLocationManager!!.allProviders.toList()


        mLocationProvider = mLocationManager!!.getProvider(LocationManager.GPS_PROVIDER)

        //Let check if user set the permition for locations

        if (mLocationManager!!.isProviderEnabled(mLocationProvider!!.name)) {

            println("List of Providers $providers  ${mLocationManager!!.getProviders(true)}")

        } else if (isLocationPermitionGranted()) {

            println("List of Providers ${mLocationManager!!.getProvider("gps")}")

            println("GRANTED")
        }

        //mLocationManager!! //setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true)
        //this.mLocationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER,true);
    }

    private fun isLocationPermitionGranted(): Boolean {

        val permission: Int = checkCallingOrSelfPermission(this, permission.ACCESS_COARSE_LOCATION)

        //onRequestPermissionsResult(permReqCode, permissions!!, intArrayOf(PERMISSION_DENIED, PERMISSION_GRANTED, PERMISSION_DENIED_APP_OP))

        return when (permission) {

            PERMISSION_DENIED -> false

            PERMISSION_GRANTED -> true

            else -> true

        }
    }

    /**
     * Metodo para actualizar continuamente a posicao do dispositivo.
     * @param mLocationManager
     */
    private fun mActualizarPosicaoDoDispositivo(mLocationManager: LocationManager?) {
        /**TODO 001:
         * (1) Obter a posição actual do dispositivo
         * (2) Mostrar a posição na tela.
         */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            mSolicitarPermissaoDaLocalizacao(arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION))
            //return;

        }
        val mLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                mDisplayerTextView!!.text = "" + location.altitude
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                mDisplayerTextView!!.text = "Status Changed!"
            }

            override fun onProviderEnabled(provider: String?) {
                mDisplayerTextView!!.text = "Provider Enabled!"
            }

            override fun onProviderDisabled(provider: String?) {
                mDisplayerTextView!!.text = "Provider Disabled!"
            }
        }
        mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                2000, 0f,
                mLocationListener
        )
    }

    /**
     * Metodo invocado para emitir a solicitação da permissão da localização do dispositivo.
     * @param access_x_Location
     */
    private fun mSolicitarPermissaoDaLocalizacao(access_x_Location: Array<String>) {

        /**TODO 001:
         * Verificar os objecto do arranjo
         */
        val permissao = arrayOfNulls<String?>(access_x_Location.size)
        for (i in access_x_Location.indices) {
            permissao[i] = access_x_Location[i]
        }
        requestPermissions(permissao, 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        /**TODO 001:
         * (1)Verificar se a permissão recebida é a de localização;
         */
        //var referenciaDaPermissao = ""
        for (i in permissions.indices) {
            val referenciaDaPermissao = "100" + "_" + permissions[i]
            when (referenciaDaPermissao) {
                "100_android.permission.ACCESS_COARSE_LOCATION" -> {
                    mProcessarSolicitacaoParaLocalizacao(grantResults[i])
                    mProcessarSolicitacaoParaLocalizacao(grantResults[i])
                    return     //this.mDisplayerTextView.setText(referenciaDaPermissao);
                }
                "100_android.permission.ACCESS_FINE_LOCATION" -> {
                    mProcessarSolicitacaoParaLocalizacao(grantResults[i])
                    return
                }
                else -> return
            }
        }
    }

    /**
     * @param grantResult resultado da solicitação de permissao do dispositivo = {PERMISSION_GRANTED = 0, PERMISSION_DENIED = 1}
     * Metodo que deve ser invocado para mostrar mensagem de permissão concedida ou não.
     */
    private fun mProcessarSolicitacaoParaLocalizacao(grantResult: Int) {

        /**TODO 001:
         * Verificar se a permissão foi concedida pelo usuario.
         */
        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            mLocationProvider = mLocationManager!!.getProvider(LocationManager.NETWORK_PROVIDER)
            mDisplayerTextView!!.text = "Permissão de localização foi concedida pelo usuario!"
            mLocalizarDispositivo(mLocationProvider)
        } else {
            mDisplayerTextView!!.text = "Permissão de localização não foi concedida pelo usuario!"
        }
    }

    private fun mLocalizarDispositivo(mLocationProvider: LocationProvider?) {
        /**TODO 001:
         * (1) Verificar se o provedor existe, e mostrar na telas.
         */
        if (mLocationProvider != null) {
            var log: CharSequence = mDisplayerTextView!!.text.toString() + "\n" + mLocationProvider.name
            //this.mDisplayerTextView.setText(log);

            //Obter a localização do dispositivo

            mLocation = Location(mLocationProvider.name)
            val mCoordLatitude = mLocation!!.altitude
            val mCoordLongitude = mLocation!!.longitude

            /**TODO 002:
             * (1) Mostrar a localizacao na tela.
             */
            log = log.toString() + "\n" + mCoordLatitude.toString() + "\n" + mCoordLongitude.toString()
            mDisplayerTextView!!.text = log
            mActualizarPosicaoDoDispositivo(mLocationManager)
        } else {
            val log: CharSequence = mDisplayerTextView!!.text
            mDisplayerTextView!!.text = "$log\nNenhum provedor foi encontrado."
        }
    }

}