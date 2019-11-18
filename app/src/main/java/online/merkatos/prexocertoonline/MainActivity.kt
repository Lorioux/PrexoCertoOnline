package online.merkatos.prexocertoonline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var productDesc: View? = null
    private var include: View? = null
    var commit: FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = this.findViewById<Toolbar>(R.id.main__appBar_toolbar)
        this.setSupportActionBar(toolbar)

        //Instantiate the product information view
        include = this.findViewById(R.id.inc_layout_product_info)

        //Intantiate the product description view
        productDesc = this.findViewById<View>(R.id.stub_layout_prod_desc)

        //Instatite a fragment to host the product description
        commit = this.supportFragmentManager.beginTransaction().add(
                R.id.container,
                ProductDescriptionFragT(),
                "Details"
        )

        findViewById<ImageView>(R.id.imageView).setOnClickListener(/*object: View.OnClickListener{
            override fun onClick(v: View?) {

            }
        }*/this)

        findViewById<ImageView>(R.id.imageView2).setOnClickListener(/*object: View.OnClickListener{
            override fun onClick(v: View?) {

            }
        }*/this)
        findViewById<ImageView>(R.id.imageView3).setOnClickListener(/*object: View.OnClickListener{
            override fun onClick(v: View?) {

            }
        }*/this)
        //var bitmap: Bitmap = Bitmap.createBitmap()

        //Intantiate the next button
        this.findViewById<Button>(R.id.button).setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        //Set the visibility of the product description form
        if (v!!.id == R.id.button) {

            productDesc?.visibility = VISIBLE
            include?.visibility = GONE
            //val  View = findViewById<ViewStub>(R.id.stub_layout_prod_desc).inflate()
            //commit!!.commitNow()
        }
        if (v.id == R.id.imageView) {
            findViewById<View>(R.id.stub_layout_prod_snap)?.visibility = VISIBLE
            include?.visibility = GONE
        }
        if (v.id == R.id.imageView2) {
            findViewById<View>(R.id.stub_layout_prod_snap)?.visibility = VISIBLE
            include?.visibility = GONE
        }

        if (v.id == R.id.imageView3) {
            findViewById<View>(R.id.stub_layout_prod_snap)?.visibility = VISIBLE
            include?.visibility = GONE
        }
    }

    //Set the visibility of the product information files on back button pressed
    override fun onBackPressed() {
        //super.onBackPressed()
        if (include?.visibility == VISIBLE) finish()
        productDesc?.visibility = GONE
        include?.visibility = VISIBLE
    }

    class ProductDescriptionFragT : Fragment {

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            return inflater.inflate(R.layout.layout_product_desc, container, false)
        }
    }
}

