package online.merkatos.prexocertoonline

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class SingleCategoryRecyclerViewAdapter(c: Context?) : RecyclerView.Adapter<SingleCategoryRecyclerViewAdapter.ViewHolder>() {

    var c: Context? = null
    var numberofItems: Int

    init {
        this.c = c
        numberofItems = 6
    }

    override fun onCreateViewHolder(container: ViewGroup, pos: Int): ViewHolder {

        val view = LayoutInflater.from(c).inflate(R.layout.layout_product_mini_desc, container,false)

        return ViewHolder(view, c)
    }

    override fun getItemCount(): Int {
        /**Count the number of products in this category as by the value received from the server*/
        return numberofItems
    }

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {

        //Set the margin left for the first item and margin right for the last item
        when(pos){
            0 -> {
                /**Set the margin and view parameters*/
                vh.clp.marginStart = vh.marginpx.toInt()
                vh.clp.marginEnd = vh.marginpx.toInt()/2
                vh.product_mini_desc.layoutParams = vh.clp
            }
            (numberofItems - 1) -> {
                /**Set the margin and view parameters*/
                vh.clp.marginEnd = vh.marginpx.toInt()
                vh.product_mini_desc.layoutParams = vh.clp
            }
            else -> return
        }
    }

    class ViewHolder(v: View, c: Context?) : RecyclerView.ViewHolder(v) {

        var marginpx: Float
        private var res: Resources
        val clp: ConstraintLayout.LayoutParams
        val product_mini_desc: View

        //val cl: ConstraintLayout = v

        init {
            product_mini_desc = v
            clp = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )

            res = c!!.resources
            /**Convert 16dp in px for margin*/
            marginpx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f,
                    res.displayMetrics
            )

        }
    }

}
