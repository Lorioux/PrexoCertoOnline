package online.merkatos.prexocertoonline

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.layout_store_manager_login.*
import online.merkatos.prexocertoonline.data.model.LoggedInUser

class MainActivity : AppCompatActivity(),
        View.OnClickListener
{

    private lateinit var fragmentsmanager: FragmentManager

    //single category fragment tag
    private val TAG_SINGLE_CAT_PAGE: String = "SingleCategoryPage"

    //bottom menu items and titles
    private lateinit var storepage: MyStorePage
    private lateinit var storenotifications: StoreNotificationsPage
    private lateinit var managerprofile: ManagerProfilePage
    private val TAG_MY_STORE: String = "My Store"
    private val TAG_MY_NOTIFS: String = "Notifications"
    private val TAG_MY_PROFILE: String = "My Profile"

    //toobar content
    lateinit var toolbar: Toolbar
    lateinit var mTitle: TextView
    private lateinit var searchView: SearchView
    private var toolbarisVisible = true


    /**Variable to instantiate the bottom navigation view */
    lateinit var bottomnavig: BottomNavigationView




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentsmanager = supportFragmentManager

        toolbar = findViewById<Toolbar>(R.id.inc_layout_store_products_searcher)
        //this.setSupportActionBar(toolbar)

        //initiatilize all fragments for future use
        storepage = MyStorePage(this)
        storenotifications = StoreNotificationsPage()
        managerprofile = ManagerProfilePage(this)

        //instantiate bottom menu
        bottomnavig = findViewById(R.id.bottomNavigationView)

        bottomnavig.setOnNavigationItemSelectedListener {
            bottomMenuController(it)
            true
        }

        //Get the tooolbar child element
        searchView = findViewById(R.id.storeProductsSearcher)
        mTitle = findViewById(R.id.app_title)
        //mTitle.setOnClickListener(this)

        searchView.setOnCloseListener {
            if (mTitle.visibility == INVISIBLE) {
                mTitle.visibility = VISIBLE
                searchView.elevation = 0f
            }
            true
        }

        searchView.setOnClickListener {
            if (mTitle.visibility == VISIBLE) {
                mTitle.visibility = INVISIBLE
                searchView.elevation = 8f
            }
        }


        fragmentsmanager.beginTransaction().add(
                R.id.fragments_container,
                storepage,
                TAG_MY_STORE
        ).commitNow()


        /**AdMob Initializer*/
        //MobileAds.initialize(this) {}

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        handleBackClick()
    }

    fun handleBackClick() {

        if (fragmentsmanager.findFragmentByTag(TAG_SINGLE_CAT_PAGE)!!.isVisible) {
            //change title
            mTitle.text = getString(R.string.app_title)

            handlefragments(fragmentsmanager, TAG_MY_STORE)

        } else {

            finish()

        }
    }

    override fun onClick(v: View?) {

        if (v?.visibility == VISIBLE){

            searchView.visibility = VISIBLE//return true
            v.visibility = INVISIBLE

        } else {
            return
        }
    }

    /**
     * 1) hide toolbar while visualizing the manager profile
     * 2) add the appropriate fragment and hide any other visible fragment not corresponding to the selected menu item
     */
    fun bottomMenuController(item: MenuItem) {

        when (item.title) {

            TAG_MY_STORE -> {

                if (!toolbarisVisible) {

                    toolbar.visibility = View.VISIBLE

                    toolbarisVisible = true

                    handlefragments(fragmentsmanager, TAG_MY_STORE)

                } else if (storepage.isHidden) {

                    handlefragments(fragmentsmanager, TAG_MY_STORE)

                }
            }
            TAG_MY_NOTIFS -> {

                hideToobar()

                handlefragments(fragmentsmanager, TAG_MY_NOTIFS)

            }
            TAG_MY_PROFILE -> {

                hideToobar()

                handlefragments(fragmentsmanager, TAG_MY_PROFILE)

            }
        }
    }

    fun hideToobar() {
        if (toolbarisVisible) {
            toolbar.visibility = View.GONE
            toolbarisVisible = false
        }
    }

    fun handlefragments(fragmentmanager: FragmentManager, title: String) {

        val fragment = fragmentmanager.findFragmentByTag(title)

        val transaction = fragmentmanager.beginTransaction()

        when (title) {

            TAG_MY_STORE -> {

                if (hidefragments(fragmentmanager, title)) {

                    if (!fragment!!.isVisible) {

                        //show home UI fragment
                        transaction.show(storepage)

                        //set the bottom menu background to default
                        bottomnavig.setBackgroundColor(-1)

                    }
                }
            }
            TAG_MY_NOTIFS -> {

                if (hidefragments(fragmentmanager, title)) {

                    if (fragment == null) {

                        transaction.add(R.id.fragments_container, storenotifications, TAG_MY_NOTIFS)

                        return

                    } else if (!fragment.isVisible) {

                        transaction.show(storenotifications)
                    }
                }
            }
            TAG_MY_PROFILE -> {

                if (hidefragments(fragmentmanager, title)) {

                    if (fragment == null) {

                        fragmentmanager.beginTransaction().add(R.id.fragments_container, managerprofile, TAG_MY_PROFILE).commitNow()

                    } else if (!fragment.isVisible) {

                        fragmentmanager.beginTransaction().show(managerprofile).commitNow()

                    }

                }
            }
        }
        transaction.commitNow()
    }

    /**
     * method to hide any fragment with tag different to this title
     */
    fun hidefragments(fragmentmanager: FragmentManager, title: String): Boolean {
        val transition = fragmentmanager.beginTransaction()

        for (x in fragmentmanager.fragments) {

            //check if this is the store and if it is visible, while its true return true else hide and return true
            if (x.tag.equals(TAG_MY_STORE)) {

                if (x.isHidden) continue else transition.hide(x)

            }
            //check if the fragment is the same then return false
            else if (x.tag.equals(title)) {

                return false

            } else {

                transition.remove(x)

            }
        }
        //commit the transaction
        transition.commitNow()
        return true
    }
}

class ManagerProfileFragmentActivity : FragmentActivity() {

    lateinit var registered: LoggedInUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_store_manager_profile)
    }

    fun checkIsStoreManagerRegistired(){
        registered = LoggedInUser("","")
    }
}

class MyStorePage(val activity: MainActivity?) : Fragment(),
        StoreCategoriesRecyclerAdapter.CategoryCallback {

    private lateinit var multiplecategoriesadapter: StoreCategoriesRecyclerAdapter
    private lateinit var singlecategorypage: View
    private lateinit var multiplecategoriesrecycler: RecyclerView

    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        multiplecategoriesadapter = StoreCategoriesRecyclerAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.layout_store, container, false)

        multiplecategoriesrecycler = rootView!!.findViewById(R.id.inc_layout_store_categories_recycler)

        //singlecategorypage = rootView!!.findViewById(R.id.vstub_layout_store_single_category)

        multiplecategoriesrecycler.adapter = multiplecategoriesadapter

        multiplecategoriesrecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return rootView

    }

    override fun onResume() {
        super.onResume()
        multiplecategoriesadapter.setCategoryClickListener(this)
    }

    override fun onCategoryHeaderClick(pos: Int) {

        //instantiate the single category recycler
        val transaction = fragmentManager!!.beginTransaction()
        transaction.hide(fragmentManager?.findFragmentByTag("My Store")!!)
        transaction.add(R.id.fragments_container, SingleCategoryPage(activity), "SingleCategoryPage")
        transaction.commitNow()

    }
}

class StoreNotificationsPage : Fragment()

class ManagerProfilePage(val activity: MainActivity?) : Fragment() {


    private var profilestatus: String? = ""
    private var viewstub_summary: View? = null
    private var viewstub_registry: View? = null
    private var viewstub_signin: View? = null
    private lateinit var passwd: EditText
    private lateinit var email: EditText
    private lateinit var signin: Button
    private lateinit var sharedpreferencesEditor: SharedPreferences.Editor
    private lateinit var sharedpreferences: SharedPreferences

    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreferences = context!!.getSharedPreferences("online.merkatos.prexocertoonline", Context.MODE_PRIVATE)

        sharedpreferencesEditor = sharedpreferences.edit()

        profilestatus = sharedpreferences.getString(getString(R.string.profile_status_key), getString(R.string.profile_status_value))

        //change the bottom menu background color
        activity?.bottomnavig!!.setBackgroundColor(context!!.getColor(android.R.color.holo_orange_light))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.layout_store_manager_profile, container, false)

        viewstub_signin = rootView.findViewById(R.id.vstub_layout_store_manager_login)

        viewstub_registry = rootView.findViewById(R.id.vstub_layout_store_manager_registry)

        viewstub_summary = rootView.findViewById(R.id.vstub_layout_profile_summary)

        return rootView

    }

    override fun onResume() {
        super.onResume()
        //check if the profile is unregistered or at any other status, then start regetering if unregistered
        if (profilestatus.equals(getString(R.string.profile_status_value))) {


            viewstub_signin?.visibility = VISIBLE

            //To improve to sinc with registry system.
            LoginAsyncTask(rootView.findViewById<ViewStub>(R.id.vstub_layout_store_manager_login)?.visibility).execute()

        } else {

            viewstub_summary?.visibility = VISIBLE

            summariseProfileSettings()

        }
    }

    inner class LoginAsyncTask(val visibility: Int?) : AsyncTask<Any, Any, Boolean>() {

        override fun doInBackground(vararg params: Any?): Boolean {
            return when (visibility) {
                VISIBLE -> true
                else -> false
            }
        }

        override fun onPostExecute(result: Boolean?) {

            //Toast.makeText(context, "VISIBLE", Toast.LENGTH_LONG).show()
            signin = rootView.findViewById(R.id.login)
            email = rootView.findViewById(R.id.useremail)
            passwd = rootView.findViewById(R.id.password)

            signin.setOnClickListener {
                //Toast.makeText(context, "VISIBLE", Toast.LENGTH_LONG).show()
                if (it.id == R.id.login) {
                    setManagerEmailPreference(email.text.subSequence(0, email.length()).toString(), password.text.subSequence(0, password.length()).toString())
                }
            }

        }

        fun setManagerEmailPreference(email: String, passwd: String) {

            //val passwd  = password.text?.subSequence(0, password.length().minus(1))

            if (email.isBlank() or passwd.isBlank()) {
                return
            } else {
                sharedpreferencesEditor.putString(getString(R.string.manager_info_email_key), email)
                viewstub_signin?.visibility = GONE
                viewstub_registry?.visibility = VISIBLE
            }

            ManagerRegistry().execute(email)
        }

    }


    inner class ManagerRegistry : AsyncTask<String, Any, Boolean>() {

        private var phone: EditText? = null
        private var surmane: EditText? = null
        private var name: EditText? = null
        var email_value: String? = null

        override fun doInBackground(vararg params: String?): Boolean {
            email_value = params.get(0)
            //retrieve form
            name = rootView.findViewById(R.id.managerName)
            surmane = rootView.findViewById(R.id.managerSurname)
            phone = rootView.findViewById(R.id.managerPhone)

            return true
        }

        override fun onPostExecute(result: Boolean?) {

            val email = rootView.findViewById<EditText>(R.id.managerEmail)

            setManagerEmailAndLock(email)

            //submit manager registry
            rootView.findViewById<Button>(R.id.submitProfileRegistry).setOnClickListener {

                if (name!!.text.isBlank() or surmane!!.text.isBlank() or phone!!.text.isBlank()) {

                    Toast.makeText(context, "Please, fill the form before submitting.", Toast.LENGTH_SHORT).show()

                } else {
                    registryPreferences(
                            name!!.text.toString(),
                            surmane!!.text.toString(),
                            phone!!.text.toString()
                    )

                    viewstub_summary?.visibility = VISIBLE

                    summariseProfileSettings()
                }
            }
        }

        private fun registryPreferences(name: String, surname: String, phone: String) {

            val fullname = name.plus(" ").plus(surname)

            sharedpreferencesEditor.putString(getString(R.string.manager_info_name_key), fullname)

            sharedpreferencesEditor.putString(getString(R.string.manager_info_phone_key), phone)

            //Toast.makeText(context, "Profile registered!", Toast.LENGTH_SHORT).show()

            viewstub_registry?.visibility = GONE

            val profile_status = resources.getStringArray(R.array.pref_profile_registry_status)
            sharedpreferencesEditor.putString(getString(R.string.profile_status_key), profile_status.get(1))

            sharedpreferencesEditor.apply()

        }

        fun setManagerEmailAndLock(email: EditText) {

            email.setText(email_value)

            email.isEnabled = false

        }
    }

    fun summariseProfileSettings() {

        rootView.findViewById<TextView>(R.id.fullName).text = sharedpreferences.getString(getString(R.string.manager_info_name_key), getString(R.string.manager_name))

        rootView.findViewById<TextView>(R.id.phoneNumber).text = sharedpreferences.getString(getString(R.string.manager_info_phone_key), getString(R.string.manager_phone))

        rootView.findViewById<TextView>(R.id.email).text = sharedpreferences.getString(getString(R.string.manager_info_email_key), getString(R.string.manager_email))

        rootView.findViewById<TextView>(R.id.companyName).text = sharedpreferences.getString(getString(R.string.company_info_name_key), getString(R.string.company_name))

        rootView.findViewById<TextView>(R.id.companyAddress).text = sharedpreferences.getString(getString(R.string.company_info_address_key), getString(R.string.company_address))

        rootView.findViewById<TextView>(R.id.cityName).text = sharedpreferences.getString(getString(R.string.company_info_city_key), getString(R.string.company_city))
    }
}

class SingleCategoryPage(val activity: MainActivity?) : Fragment() {

    private lateinit var singlecategorymanager: LinearLayoutManager
    private lateinit var singlecategoryadapter: SingleCategoryWithTypeRecyclerViewAdapter
    private var multipletypesrecycler: RecyclerView? = null
    private var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        singlecategoryadapter = SingleCategoryWithTypeRecyclerViewAdapter(context)
        singlecategorymanager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.layout_multiple_type, container, false)

        multipletypesrecycler = rootView?.findViewById(R.id.inc_layout_store_categories_recycler)

        return rootView

    }

    override fun onResume() {
        super.onResume()
        //Retrieve the toolbar to change the title
        activity?.mTitle?.text = "Procurar na categoria"

        multipletypesrecycler?.adapter = singlecategoryadapter
        multipletypesrecycler?.layoutManager = singlecategorymanager
    }

}



