package online.merkatos.prexocertoonline.models

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import online.merkatos.prexocertoonline.R

@SuppressLint("ValidFragment")
open class CatalogueCreatorPagerFragment(): Fragment(){

    var pagemumber: Int? = null

    constructor(positin: Int): this(){
        pagemumber = positin
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /**Get the the actual page number and return the appropriate fragmment view for this page */
        return when(this.pagemumber){
            0 -> inflater.inflate(PageIdentityNumber.PAGENUM_ONE.page, container, false)

            1 -> inflater.inflate(PageIdentityNumber.PAGENUM_TWO.page, container, false)

            2 -> inflater.inflate(R.layout.layout_store_single_category, container, false)

            else -> inflater.inflate(R.layout.layout_prod_registry, container, false)
        }
    }

}

enum class PageIdentityNumber(val page: Int){

    PAGENUM_ONE (R.layout.layout_product_info_page_one),
    PAGENUM_TWO (R.layout.layout_product_info_page_two)

}

class CatalogueCreatorPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fm = fm

    override fun getItem(pos: Int): Fragment {
        return when(pos){
             0 -> CatalogueCreatorPagerFragment(pos)
             else -> CatalogueCreatorPagerFragment(pos)
        }
    }

    override fun getCount(): Int {
        return PageIdentityNumber.values().size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position)
        return  when(position){
            0 -> {
                fm!!.beginTransaction().add(this.getItem(position), "ViewPager").commitNow()
                startUpdate(container)
            }
            else -> {
                fm!!.beginTransaction().add(this.getItem(position), "ViewPager").commitNow()
                return startUpdate(container)
            }
        }

    }
}

