package online.merkatos.prexocertoonline

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import online.merkatos.prexocertoonline.Entities.StoreDescriptor
import online.merkatos.prexocertoonline.data.model.LoggedInUser

class MainActivity : AppCompatActivity(),
        View.OnClickListener
{

    private var storeCategories: RecyclerView? = null

    /**Variable to instatiate the store menu item main view stub */
    private var storepage: View? = null

    /** */
    private var store_manager_profile: ViewStub? = null

    private lateinit var mTitle: TextView
    private lateinit var searchView: SearchView
    private lateinit var prod_categ_recyclerv: RecyclerView
    private lateinit var store: StoreDescriptor

    /**Variable to instantiate the bottom navigation view */
    private var bottomnavig: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = this.findViewById<Toolbar>(R.id.main__appBar_toolbar)
        this.setSupportActionBar(toolbar)

        bottomnavig = findViewById(R.id.bottomNavigationView)
        bottomnavig!!.setOnNavigationItemSelectedListener(BottomNavigationController(this, storepage))
        bottomnavig!!.setOnNavigationItemReselectedListener {}

        /**Get the tooolbar child element */
        searchView = findViewById(R.id.storeProductsSearcher)
        mTitle = findViewById(R.id.app_title)
        mTitle.setOnClickListener(this)

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
}

/**BottomNavigation item selection and reselection controller*/
class BottomNavigationController(val main: MainActivity, val view: View?) :
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    override fun onNavigationItemSelected(it: MenuItem): Boolean {
        val id = it.itemId
        return when (id) {

            R.id.menu__action_storeCatalogue -> {
                main.supportFragmentManager.beginTransaction().add(
                        R.id.fragments_container,
                        MyStorePage(),
                        "StorePAGE"
                ).commitNow()

                println("CATALOGUE CLICK: $id")
                view?.visibility = VISIBLE
                true
            }
            R.id.menu__action_userProfile -> {

                //val activity: Class<ManagerProfileFragmentActivity> = ManagerProfileFragmentActivity::class.java
                val managerprofile = Intent(view?.context, Class.forName("ManagerProfileFragmentActivity"))

                view?.context?.startActivity(managerprofile)

                println("CATALOGUE CLICK: $id")
                println("MENU_ITEM: $id")
                view?.visibility = GONE
                true

            }
            else -> false

        }
    }

    override fun onNavigationItemReselected(it: MenuItem) {
        val id = it.itemId
        when (id) {

            R.id.menu__action_storeCatalogue -> {
                println("CATALOGUE CLICK: $id")
                if (view?.visibility == VISIBLE) {
                }
            }
            else -> {

            }
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

    private var rootView: View? = null
    private var storeCategories: RecyclerView? = null
    private var storepage: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.layout_store, container)
        return rootView
    }

    override fun onResume() {
        super.onResume()

        /**Instantiate the store categories recycler view to layout all retrieved categories*/
        storeCategories = rootView?.findViewById<RecyclerView>(R.id.inc_layout_store_categories_recycler)
        storeCategories?.adapter = StoreCategoriesRecyclerAdapter(this.requireContext())
        storeCategories?.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }
}
