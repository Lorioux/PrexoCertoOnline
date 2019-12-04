package online.merkatos.prexocertoonline.Entities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import online.merkatos.prexocertoonline.R
import online.merkatos.prexocertoonline.models.CatalogueCreatorPagerFragment

open class StoreDescriptor(context: Context, container: View, fm: FragmentManager): View.OnClickListener{

    open lateinit var product_snapper: ViewStub
    val context: Context
    /**Variable to instantiate the store layout view */
    open lateinit var view: View
    /**An instance of the layout inflator */
    open lateinit var inflator: LayoutInflater

    open lateinit var productinfo_page_two: ViewStub
    open lateinit var productinfo_page_one: ViewStub

    open var commit: FragmentTransaction? = null

    init {

        this.context = context
        setUpStoreFragments(fm)
        view = container
    }

    init {


        //Instantiate the product information view
        productinfo_page_one = view.findViewById<ViewStub>(R.id.vstub_layout_product_info_page_one)

        productinfo_page_one.visibility = View.VISIBLE

        //Intantiate the product description view
        productinfo_page_two =  view.findViewById<ViewStub>(R.id.vstub_layout_product_info_page_two)

        //product_snapper = view.findViewById<ViewStub>(R.id.stub_layout_prod_snap)

    }

    init {
        view.findViewById<ImageView>(R.id.imageView).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageView2).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageView3).setOnClickListener(this)
        view.findViewById<Button>(R.id.button).setOnClickListener(this)
    }

    fun setUpStoreFragments(fm: FragmentManager){
        //Instatite a fragment to host the product description
        /*commit = fm.beginTransaction().add(
                R.id.fragmentProductRegistry,
                CatalogueCreatorPagerFragment(-1),
                "Details"
        )
        commit!!.commitNow()*/

    }

    override fun onClick(v: View?) {

        //Set the visibility of the product description form
        if (v!!.id == R.id.button){

            productinfo_page_two.visibility = View.VISIBLE
            productinfo_page_one.visibility = View.GONE
            //val  View = findViewById<ViewStub>(R.id.stub_layout_prod_desc).inflate()
            //commit!!.commitNow()
        }

        /**Set to open the camera preview */
        val images = intArrayOf(R.id.imageView, R.id.imageView2, R.id.imageView3)
        when(v.id){
            in images -> {
                productinfo_page_one.visibility = View.GONE
                product_snapper.visibility = View.VISIBLE
                product_snapper.elevation = 12f

            }
        }

    }

}