package online.merkatos.prexocertoonline

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.SurfaceView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProductImagingActivity : AppCompatActivity() {

    private val camerarequestcode: Int = 1000
    private var permission_code: Int? = null
    private lateinit var camerasurface: SurfaceView
    private lateinit var cameracapture: ImageView
    private lateinit var incomingintennt: Intent


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.layout_camera_surface)

        camerasurface = findViewById(R.id.main_camerasurface)

        cameracapture = findViewById(R.id.main_imageCapture)
        //incomingintennt =  intent

    }

    override fun onResume() {
        super.onResume()

        permission_code = checkSelfPermission(android.Manifest.permission.CAMERA)



        if (permission_code == PackageManager.PERMISSION_DENIED) {


        } else {

            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), camerarequestcode)

        }
    }


}

