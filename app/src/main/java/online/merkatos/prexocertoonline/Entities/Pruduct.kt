package online.merkatos.prexocertoonline.Entities

/**
 *  SYNPSIS:
 *	Class to record a product information into the database
 *
 *	Pre: The system provides the use with a form to insert the data
 *	Pos: The user upload the form for processing
 *
 *	Steps:
 *		1) User inserts the required information into the form such as:
 *			-ProductBrandName
 *			-ProductName
 *			-ProductCategoryName
 *			-ProductType
 *			-ProductSubType
 *			-ProductVolume
 *			-ProductQttyUnits
 *			-ProductPrice
 *			-ProductDescription
 *		2) Also, the user insert the product snaps or images at least three
 *			-The system lets the user to load the images from the file system
 *			-Or to take a snap of the product, by openning the device camera.
 *			-If the user is done with the snap, the  system lets the user to edit
 *			the image and validate.
 *		3) If the information gathering is done and the submission is requested by
 *		   the user.
 *		   -The system uploads the form for processing into the database
 */
data class Product(
        val mProductBrandName: String? = null,
        val mProductName: String? = null,
        val mProductCategory: String? = null,
        val mProductType: String? = null,
        val mProductSubType: String? = null,
        val mProductVolume: String? = null,
        val mProductQttyUnits: String? = null,
        val mProductPrice: String? = null,
        val mProductDescription: String? = null,
        val mProductThumbImages: ByteArray? = null
)

data class Ads(
        val mAdsRefCode: Int? = null,
        val mAdsCatalogue: Int? = null,
        val mAdsCatagory: String? = null,
        val mAdsProductCode: Int? = null,
        val mAdsUpTime: Double? = null,
        val mAdsViewCount: Double? = null
)

data class ListItems(
        val mItemCode: Int? = null,
        val mItemStatus: String? = null,
        val mProductCode: Int? = null,
        val mShoppingListID: Int? = null
)

data class StoreManager(
        val mStoreOwnerCode: String? = null,
        val mOwnerFirstName: String? = null,
        val mOwnerLastName: String? = null,
        val mOwnerPhoneNumber: String? = null
)

data class Store(
        val mShopCode: Int? = null,
        val mShopName: String? = null,
        val mShopAddressOne: String? = null,
        val mShopAddressTwo: String? = null,
        val mShopCity: String? = null,
        val mShopOwnerCode: String? = null,
        val mShopProductsCatalogCode: Int? = null
)