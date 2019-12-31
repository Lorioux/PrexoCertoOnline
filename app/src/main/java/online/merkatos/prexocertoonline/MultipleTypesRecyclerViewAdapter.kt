package online.merkatos.prexocertoonline

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import kotlin.math.abs

class SingleCategoryWithTypeRecyclerViewAdapter(c: Context?) : RecyclerView.Adapter<SingleCategoryWithTypeRecyclerViewAdapter.ViewHolder>() {
    lateinit var gridisplayadapter: android.widget.ListAdapter

    init {
        gridisplayadapter = SingleGridDisplayAdapter()
    }

    override fun onCreateViewHolder(container: ViewGroup, pos: Int): ViewHolder {

        val view = LayoutInflater.from(container.context).inflate(R.layout.layout_single_type_grid_display, container, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        /**Count the number of products in this category as by the value received from the server*/
        return 3
    }

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {

        //Set the margin left for the first item and margin right for the last item
        vh.displayer.adapter = gridisplayadapter

        vh.productadder.setOnClickListener {
            vh.registryNewProduct(vh.producttypeheader.text.toString())
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        lateinit var producttypeheader: TextView
        lateinit var productadder: TextView
        lateinit var displayer: GridView

        val context = v.context

        init {
            producttypeheader = v.findViewById(R.id.singleProductsTypeHeader)
            productadder = v.findViewById(R.id.singleProductsTypeAdd)
            displayer = v.findViewById(R.id.singleProductsTypeDisplay)
        }

        fun registryNewProduct(type: String?) {
            val startProductRegistry = Intent(context, RegistryProducts::class.java)
            startProductRegistry.putExtra("type", type)
            context.startActivity(startProductRegistry)
        }
    }

}


class SingleTypeDisplayListAdapter : ListAdapter<View, RecyclerView.ViewHolder>(ProductDiffer()) {

    //constructor(c): super(config){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_product_mini_desc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        //val gridisplayer = v.findViewById<G>()
    }
}

class ProductDiffer : DiffUtil.ItemCallback<View>() {
    override fun areContentsTheSame(oldItem: View, newItem: View): Boolean {

        //To improve : use product instead
        return oldItem.equals(newItem)
    }

    override fun areItemsTheSame(oldItem: View, newItem: View): Boolean {
        //To improve: use product id instead
        return oldItem.id == newItem.id
    }
}

class SingleGridDisplayAdapter : android.widget.ListAdapter, BaseAdapter() {
    lateinit var view: View
    override fun isEmpty(): Boolean {
        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.layout_product_mini_desc, parent, false)
            return view
        }

        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItem(position: Int): Any {
        return Unit
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return view.id.toLong()
    }

    override fun getCount(): Int {
        return 4
    }

}

class RegistryProducts : AppCompatActivity() {

    private lateinit var currentPhotoPath: String
    //Code to use for camera intent call
    private val RES_CODE: Int = 100
    private val MAIN_IMAGE_CODE: Int = abs(R.id.main_productImage.shr(16))
    private val SECD_IMAGE_CODE: Int = abs(R.id.second_productImage.shr(16))
    private val THIRD_IMAGE_CODE: Int = abs(R.id.third_productImage.shr(16))

    private lateinit var cameramanager: CameraManager
    private lateinit var camerasurface: SurfaceView
    private lateinit var camerapage: View
    private lateinit var callintent: Intent
    private lateinit var productinfopage_one: View
    private lateinit var productinfopage_two: View
    private lateinit var productinfonextpage: Button

    private lateinit var main_productSubmitRegistry: Button

    private lateinit var brandname: EditText
    private lateinit var type: EditText
    private lateinit var category: EditText
    private lateinit var subcategory: EditText
    private lateinit var price: EditText
    private lateinit var quantity: EditText
    private lateinit var description: EditText
    private lateinit var main_productImage: ImageView
    private lateinit var second_productImage: ImageView
    private lateinit var third_productImage: ImageView

    //registry content
    private lateinit var page_one_content: Map<String, Any>
    private lateinit var page_two_content: Map<String, Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_product_registry)

        callintent = intent
        productinfopage_one = findViewById<ViewStub>(R.id.vstub_layout_product_info_page_one)
        productinfopage_two = findViewById<ViewStub>(R.id.vstub_layout_product_info_page_two)
        camerapage = findViewById<ViewStub>(R.id.vstub_layout_camera_surface)

        productinfopage_one.visibility = View.VISIBLE

        page_one_content = mapOf()
        page_two_content = mapOf()


    }

    override fun onResume() {
        super.onResume()

        productinfonextpage = findViewById(R.id.productInfoNextPage)

        instantiatePageOneRegistryComponents(productinfopage_one.visibility)

        productinfonextpage.setOnClickListener {

            productinfopage_one.visibility = View.GONE

            RegistryPageOneManagementAsyncTask().execute(1, page_one_content)
            //retrieve all components
            //instantiatePageTwoRegistryComponents(productinfopage_two.visibility)
        }
    }

    private fun instantiatePageOneRegistryComponents(visibility: Int) {

        if (visibility == View.VISIBLE) {
            brandname = findViewById(R.id.productBrandName)
            type = findViewById(R.id.productName)
            price = findViewById(R.id.productPrice)
            val priceCurrency = findViewById<Spinner>(R.id.priceCurrency)

            main_productImage = findViewById(R.id.main_productImage)
            second_productImage = findViewById(R.id.second_productImage)
            third_productImage = findViewById(R.id.third_productImage)

            main_productImage.setOnClickListener(imagelistener)
            second_productImage.setOnClickListener(imagelistener)
            third_productImage.setOnClickListener(imagelistener)

            type.setText(callintent.getStringExtra("type"))
            type.isEnabled = false
        }
    }

    private fun instantiatePageTwoRegistryComponents(visibility: Int) {
        if (visibility == View.VISIBLE) {
            category = findViewById(R.id.productCategory)
            subcategory = findViewById(R.id.productSubCategory)
            quantity = findViewById(R.id.productQtty)
            description = findViewById(R.id.main_productDetails)

            main_productSubmitRegistry = findViewById(R.id.main_productSubmitRegistry)
        }

    }

    //getRegistryContent()
    fun getRegistryContent(pagenumber: Int): Map<String, Any> {
        return when (pagenumber) {
            1 -> {
                page_one_content.plus(Pair("brand", brandname.text.toString()))
                page_one_content.plus(Pair("type", type.text.toString()))
                page_one_content.plus(Pair("price", price.text.toString()))
                page_one_content.plus(Pair("mainimage", main_productImage.drawable))
                page_one_content.plus(Pair("secondimage", second_productImage.drawable))
                page_one_content.plus(Pair("thirdimage", third_productImage.drawable))

                page_one_content
            }
            2 -> {
                page_two_content.plus(Pair("category", category.text.toString()))
                page_two_content.plus(Pair("subcategory", subcategory.text.toString()))
                page_two_content.plus(Pair("quantity", quantity.text.toString()))
                page_two_content.plus(Pair("description", description.text.toString()))

                page_two_content
            }
            else -> {
                return mapOf()
            }
        }

    }

    //check the fields are filled including the images
    fun checkPagefields(pagenumber: Int, content: Map<String, Any>): Boolean {
        when (pagenumber) {
            1 -> {

                if (content.isEmpty()) {
                    getRegistryContent(pagenumber)

                }

                for (x in content.values) {
                    if (x.equals("")) {
                        return false
                    }
                }
                return true
            }
            2 -> {

                if (content.isEmpty()) {
                    getRegistryContent(pagenumber)
                }

                for (x in content.values) {
                    if (x.equals("")) {
                        return false
                    }
                }

                return true
            }
            else -> {
                return false
            }
        }
    }

    val imagelistener = View.OnClickListener {


        val reqcode: Int = abs(it.id.shr(16))
        val productname = brandname.text.toString()
        val permission = checkSelfPermission(Manifest.permission.CAMERA)
        if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    1001
            )
        } else {
            //Check the product name field if not empty
            productname.isBlank().run {
                when (this) {

                    true -> {
                        Toast.makeText(baseContext, "You are required to fill the product brand name field to proceed!", Toast.LENGTH_SHORT).show()/**/
                        return@OnClickListener
                    }
                    else -> takePicture(reqcode, productname)
                }
            }
        }

        //Toast.makeText(baseContext, "List of ids: ${reqcode}", Toast.LENGTH_LONG).show()

    }

    private fun takePicture(reqcode: Int, productname: String) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {

            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager).also {

                val photoFile: File? = try {

                    createImageFile(reqcode, productname)

                } catch (ex: IOException) {

                    System.err.println(ex.message)
                    null
                }

                photoFile?.also {
                    FileProvider.getUriForFile(
                            baseContext,
                            getString(R.string.app_authority),
                            it
                    ).apply {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this)
                    }
                }

            }.run {

                startActivityForResult(takePictureIntent, reqcode)

            }

        }

    }

    fun askEitherToTakePhotoOrUploadImageFileFromTheFileSystem() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                MAIN_IMAGE_CODE -> setProductImage(main_productImage, currentPhotoPath)

                SECD_IMAGE_CODE -> setProductImage(second_productImage, currentPhotoPath)

                THIRD_IMAGE_CODE -> setProductImage(third_productImage, currentPhotoPath)

            }
        }

    }

    private fun setProductImage(imageplaceholder: ImageView, currentPhotoPath: String) {
        val image = BitmapFactory.decodeFile(currentPhotoPath)
        imageplaceholder.setImageBitmap(image)
    }

    class productImageProcessorAsynTask(val imageplaceholder: ImageView) : AsyncTask<String, Any, Bitmap>() {

        override fun doInBackground(vararg params: String): Bitmap {
            return BitmapFactory.decodeFile(params.get(0))
        }

        override fun onPostExecute(result: Bitmap?) {
            result ?: apply {
                imageplaceholder.setImageBitmap(result)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(reqcode: Int, productname: String): File? {

        //val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val productImageFileDir = File(filesDir, "images/${productname}/")

        return when (reqcode) {

            MAIN_IMAGE_CODE -> {
                buildImageFileName("main", productImageFileDir)
            }

            SECD_IMAGE_CODE -> {
                buildImageFileName("second", productImageFileDir)
            }

            THIRD_IMAGE_CODE -> {
                buildImageFileName("third", productImageFileDir)
            }
            else -> null
        }

    }

    private fun buildImageFileName(prefix: String, dir: File): File {
        return when (dir.isDirectory) {
            true -> createFileDirectory(prefix, dir)
            else -> {
                dir.mkdir(); createFileDirectory(prefix, dir)
            }
        }
    }

    //Create temporary image files to host the retrieved image from camera call
    fun createFileDirectory(prefix: String, dir: File): File {

        return File.createTempFile(
                "${prefix}",
                ".tmp",
                dir.normalize()
        ).also {
            currentPhotoPath = it.absolutePath
        }
    }

    //define an asynctask class to manage the collection of the inserted information on the registry
    inner class RegistryPageOneManagementAsyncTask : AsyncTask<Any, Any, Boolean>() {

        override fun doInBackground(vararg params: Any?): Boolean {

            return checkPagefields(params.get(0) as Int, params.get(1) as Map<String, Any>)
        }

        override fun onPostExecute(result: Boolean?) {
            //check if all field were filled
            if (result!!) {
                //uploadRegistry()
                //activate page two visibility
                productinfopage_two.visibility = View.VISIBLE
                instantiatePageTwoRegistryComponents(productinfopage_two.visibility)
            } else {
                Toast.makeText(baseContext, "Complete the form to proceed!", Toast.LENGTH_LONG).show()
            }
        }

        private fun registryComplete(): Boolean {
            return false
        }
    }

    //define an asynctask class to manage the collection of the inserted information on the registry
    inner class RegistryPageTwoManagementAsyncTask : AsyncTask<View, Any, Any>() {

        override fun doInBackground(vararg params: View?): Any {

            val rootview = params.get(0)

            return Unit
        }

        override fun onPostExecute(result: Any?) {
            //check if all field were filled
            if (registryComplete()) {
                //uploadRegistry()
            }
        }

        private fun registryComplete(): Boolean {
            return false
        }
    }
}
//class FileProvider: FileP