package online.merkatos.prexocertoonline

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import online.merkatos.prexocertoonline.data.model.LoggedInUser

class MainActivity : AppCompatActivity(),
        View.OnClickListener
{

    private lateinit var mTitle: TextView
    private lateinit var searchView: SearchView


    /**Variable to instantiate the bottom navigation view */
    private var bottomnavig: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = this.findViewById<Toolbar>(R.id.main__appBar_toolbar)
        this.setSupportActionBar(toolbar)

        bottomnavig = findViewById(R.id.bottomNavigationView)
        bottomnavig?.inflateMenu(R.menu.manager_menu)
        bottomnavig!!.setOnNavigationItemSelectedListener { this.navigationAction(it) }
        //navigationMenuController(bottomnavig!!)

        /**Get the tooolbar child element */
        searchView = findViewById(R.id.storeProductsSearcher)
        mTitle = findViewById(R.id.app_title)
        mTitle.setOnClickListener(this)

        supportFragmentManager.beginTransaction().add(
                R.id.fragments_container,
                MyStorePage(),
                "StorePAGE"
        ).commitNow()


        /**AdMob Initializer*/
        //MobileAds.initialize(this) {}

    }

    override fun onClick(v: View?) {

        if (v?.visibility == VISIBLE){

            searchView.visibility = VISIBLE//return true
            v.visibility = INVISIBLE

        } else {
            return
        }
    }

    fun navigationAction(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu__action_userNotify -> return true
            R.id.menu__action_userProfile -> {

                val activity: Class<ManagerProfileFragmentActivity> = ManagerProfileFragmentActivity::class.java
                val managerprofile = Intent(this, activity)
                startActivity(managerprofile)
                return true

            }
            else -> return false
        }
    }

}

class ManagerProfileFragmentActivity: FragmentActivity(){

    lateinit var registered: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_store_manager_profile)
    }

    fun checkIsStoreManagerRegistired(){
        registered = LoggedInUser("","")
    }
}

class ManagerProfileFragment: Fragment(){


    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.layout_product_mini_desc, container, false)

        return rootView
    }


}

class MyStorePage : Fragment() {

    private var rootView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.layout_store_categories_recycler, container, false) as RecyclerView

        rootView!!.adapter = StoreCategoriesRecyclerAdapter(context)
        rootView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return rootView
    }
}
