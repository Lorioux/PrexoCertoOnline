package online.merkatos.prexocertoonline

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import online.merkatos.prexocertoonline.Entities.StoreDescriptor
import online.merkatos.prexocertoonline.data.model.LoggedInUser

class MainActivity : AppCompatActivity(),
        View.OnClickListener
{

    private var storeCategories: RecyclerView? = null

    /**Variable to instatiate the store menu item main view stub */
    private var storepage: ViewStub? = null

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

        storepage = findViewById(R.id.vstub_store_container)
        storepage?.visibility = VISIBLE

        bottomnavig = findViewById(R.id.bottomNavigationView)

        /**Instantiate the store categories recycler view to layout all retrieved categories*/
        storeCategories = findViewById(R.id.inc_layout_store_categories_recycler)
        storeCategories?.adapter = StoreCategoriesRecyclerAdapter(this)
        storeCategories?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        /**Get the tooolbar child element */
        searchView = findViewById(R.id.storeProductsSearcher)
        mTitle = findViewById(R.id.app_title)
        mTitle.setOnClickListener(this)
        //searchView.setOnClickListener(this)

        /** */
        store_manager_profile = findViewById(R.id.vstub_layout_store_manager_profile)

        store_manager_profile = findViewById(R.id.vstub_layout_store_manager_profile)

        /**AdMob Initializer*/
        MobileAds.initialize(this) {}

    }

    override fun onResume() {
        super.onResume()

        //val menu = bottomnavig!!.menu

        bottomnavig!!.setOnNavigationItemSelectedListener{
            /**Set this to handle the selected navigation menuItem */
            val id = it.itemId
            when(id){

                R.id.menu__action_userCatogue -> {

                    println("CATALOGUE CLICK: $id")
                    findViewById<ViewStub>(R.id.container).visibility = VISIBLE
                    true
                }
                R.id.menu__action_userProfile -> {

                    //val activity: Class<ManagerProfileFragmentActivity> = ManagerProfileFragmentActivity::class.java
                    val managerprofile = Intent(this, Class.forName("ManagerProfileFragmentActivity"))
                    startActivity(managerprofile)

                    println("CATALOGUE CLICK: $id")
                    println("MENU_ITEM: $id")
                    findViewById<ViewStub>(R.id.container).visibility = GONE
                    true

                }
                else -> false
            }
        }

        bottomnavig!!.setOnNavigationItemReselectedListener {
            val id = it.itemId
            when(id){

                R.id.menu__action_userCatogue -> {
                    println("CATALOGUE CLICK: $id")
                    if (findViewById<ViewStub>(R.id.container).visibility == VISIBLE){
                    }
                }
                else -> {

                }
            }
        }


        //store = StoreDescriptor(this, findViewById(R.id.container),supportFragmentManager)
    }

    /**Set the visibility of the product information files on back button pressed*/
    override fun onBackPressed() {
        //super.onBackPressed()
        /*if (store.productinfo_page_one.visibility == View.VISIBLE) {
            finish()
        }
        else if(store.product_snapper.visibility == VISIBLE){
            store.product_snapper.visibility = GONE
            store.productinfo_page_one.visibility = View.VISIBLE
        }
        else{
            store.productinfo_page_two.visibility = View.GONE
            store.productinfo_page_one.visibility = View.VISIBLE
        }*/
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


/***/
class ManagerProfileFragmentActivity: FragmentActivity(){

    lateinit var registered: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_store_manager_profile)

        findViewById<ViewStub>(R.id.vstub_layout_store_manager_profile).visibility = VISIBLE

    }

    fun checkIsStoreManagerRegistired(){
        registered = LoggedInUser("","")
    }
}

class ManagerProfileFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_product_mini_desc, container, false)
    }
}
