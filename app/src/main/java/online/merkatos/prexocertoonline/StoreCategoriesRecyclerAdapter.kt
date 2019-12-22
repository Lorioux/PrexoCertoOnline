package online.merkatos.prexocertoonline

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreCategoriesRecyclerAdapter(val c: Context?) : RecyclerView.Adapter<StoreCategoriesRecyclerAdapter.ViewHolder>() {

    private lateinit var categoryCallback: CategoryCallback
    private val VIEWHOLDER_TYPE_ZERO: Int = 0
    private val VIEWHOLDER_TYPE_ONE: Int = 1


    private var rootView: View? = null

    override fun onCreateViewHolder(container: ViewGroup, pos: Int): ViewHolder {

        when (pos) {

            0 -> {


                val v = LayoutInflater.from(c).inflate(R.layout.layout_store_headers, container, false)

                return ViewHolder(v, pos)
            }
            else -> {

                rootView = LayoutInflater.from(c).inflate(R.layout.layout_store_single_category, container, false)

                return ViewHolder(rootView!!, pos)
            }
        }

    }

    override fun getItemViewType(pos: Int): Int {
        return when (pos) {
            0 -> VIEWHOLDER_TYPE_ZERO
            else -> VIEWHOLDER_TYPE_ONE
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {

        //check if it is first position to and customize the catalogue header
        //vh.bind(pos)

        vh.singlecategoryheader.setOnClickListener {

            if (!(categoryCallback == null)) {

                categoryCallback.onCategoryHeaderClick(pos)

            }

        }

        SingleCategoryRecyclingAsyncTask(c, vh.singlecategoryrecycler).execute()

    }

    class ViewHolder(v: View, pos: Int) : RecyclerView.ViewHolder(v) {
        lateinit var singlecategoryheader: View
        lateinit var categoryheadertitle: TextView
        lateinit var categorymore: AppCompatImageView
        lateinit var singlecategory: CardView
        lateinit var singlecategoryrecycler: RecyclerView
        lateinit var advsheader: CardView
        lateinit var catalogueheader: CardView

        init {

            singlecategoryheader = v.findViewById(R.id.singleCategoryHeader) as View
            categoryheadertitle = v.findViewById<TextView>(R.id.productsCategoryHeader)
            categorymore = v.findViewById<AppCompatImageView>(R.id.moreInThisCategoryArrow)
            singlecategory = v.findViewById<CardView>(R.id.storeSingleCategory)
            singlecategoryrecycler = v.findViewById<RecyclerView>(R.id.singleCategoryProductsRecycler)

            if (pos.equals(0)) {

                v.setBackgroundColor(v.context.getColor(android.R.color.holo_orange_light))

                catalogueheader = v.findViewById(R.id.storeCatalogueControler)
                advsheader = v.findViewById(R.id.storeAdvsControler)
                advsheader.setCardBackgroundColor(v.context.getColor(android.R.color.holo_orange_light))

                //To improve
                catalogueheader.setOnClickListener {

                    catalogueheader.setCardBackgroundColor(-1)

                    advsheader.setCardBackgroundColor(it.context.getColor(android.R.color.holo_orange_light))
                    //To implement
                    //storepage = updatecontentlist("Catalogue")
                }

                //To improve
                advsheader.setOnClickListener {

                    advsheader.setCardBackgroundColor(-1)
                    catalogueheader.setCardBackgroundColor(it.context.getColor(android.R.color.holo_orange_light))
                    //To implement
                    //storepage = updatecontentlist("Advertisements")

                }
            }
        }


        fun bind(pos: Int) {

        }

    }



    class SingleCategoryRecyclingAsyncTask(val c: Context?, val rv: RecyclerView) : AsyncTask<Any, Any, Any>() {

        override fun doInBackground(vararg params: Any?): Any {
            return 0
        }

        override fun onPostExecute(result: Any?) {
            rv.adapter = SingleCategoryRecyclerViewAdapter(c)
            rv.layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**
     * Callback function to response to any category click
     */
    interface CategoryCallback {
        fun onCategoryHeaderClick(pos: Int)
    }

    fun setCategoryClickListener(callback: CategoryCallback) {
        categoryCallback = callback
    }
}
