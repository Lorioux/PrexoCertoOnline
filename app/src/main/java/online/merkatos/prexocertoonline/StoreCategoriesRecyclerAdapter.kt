package online.merkatos.prexocertoonline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoreCategoriesRecyclerAdapter(context: Context) : RecyclerView.Adapter<StoreCategoriesRecyclerAdapter.ViewHolder>() {

    val c= context

    override fun onCreateViewHolder(container: ViewGroup, pos: Int): ViewHolder {

        return when (pos) {
            0 -> ViewHolder(LayoutInflater.from(c).inflate(R.layout.layout_store_headers, container, false))
            else -> ViewHolder(LayoutInflater.from(c).inflate(R.layout.layout_store_single_category, container, false))
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {


        vh.rv.adapter = SingleCategoryRecyclerViewAdapter(c)
        vh.rv.layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
    }

    class ViewHolder (v: View): RecyclerView.ViewHolder(v) {

        val rv = v.findViewById<RecyclerView>(R.id.singleCategoryProductsRecycler)
    }
}
