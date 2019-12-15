package online.merkatos.prexocertoonline

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreCategoriesRecyclerAdapter(val c: Context?) : RecyclerView.Adapter<StoreCategoriesRecyclerAdapter.ViewHolder>() {

    private val VIEWHOLDER_TYPE_ZERO: Int = 0
    private val VIEWHOLDER_TYPE_ONE: Int = 1


    private var rootView: View? = null

    override fun onCreateViewHolder(container: ViewGroup, pos: Int): ViewHolder {

        when (pos) {

            0 -> {
                val ll = LinearLayout(c)
                ll.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                ll.orientation = LinearLayout.VERTICAL

                val v = LayoutInflater.from(c).inflate(R.layout.layout_store_headers, null, false)
                ll.addView(v, 0)

                ll.addView(LayoutInflater.from(c).inflate(R.layout.layout_store_single_category, null, false), 1)

                return ViewHolder(ll as View)
            }
            else -> {

                rootView = LayoutInflater.from(c).inflate(R.layout.layout_store_single_category, container, false)

                return ViewHolder(rootView!!)
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

        SingleCategoryRecyclingAsyncTask(c, vh.rv).execute()
    }

    class ViewHolder (v: View): RecyclerView.ViewHolder(v) {

        val rv = v.findViewById<RecyclerView>(R.id.singleCategoryProductsRecycler)
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
}
