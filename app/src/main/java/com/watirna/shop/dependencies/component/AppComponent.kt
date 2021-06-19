package com.watirna.shop.dependencies.component

import com.watirna.app.ui.changepasswordactivity.ChangePasswordActivity
import com.watirna.app.ui.changepasswordactivity.ChangePasswordViewModel
import com.watirna.shop.base.BaseRepository
import com.watirna.shop.dependencies.modules.AppContainerModule
import com.watirna.shop.dependencies.modules.ApplicationModule
import com.watirna.shop.dependencies.modules.NetworkModule
import com.watirna.shop.fcm.MyFirebaseMessagingService
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.*
import com.watirna.shop.views.accept.AcceptOrderFragment
import com.watirna.shop.views.account.AccountFragment
import com.watirna.shop.views.adapters.*
import com.watirna.shop.views.dashboard.DashboardActivity
import com.watirna.shop.views.dashboard.DashboardViewModel
import com.watirna.shop.views.editrestaurant.EditRestaurantActivity
import com.watirna.shop.views.editrestaurant.EditRestaurantViewModel
import com.watirna.shop.views.edittime.EditTimeViewModel
import com.watirna.shop.views.forgotPasswordActivity.ForgotPasswordActivity
import com.watirna.shop.views.forgotPasswordActivity.ForgotPasswordViewModel
import com.watirna.shop.views.history.historycancelled.FragmentCancelOrders
import com.watirna.shop.views.history.historycancelled.FragmentCancelViewModel
import com.watirna.shop.views.history.historyongoing.FragmentOngoingOrders
import com.watirna.shop.views.history.historyongoing.FragmentOngoingViewModel
import com.watirna.shop.views.history.historypast.FragmentPastOrders
import com.watirna.shop.views.history.historypast.FragmentPastViewModel
import com.watirna.shop.views.login.LoginActivity
import com.watirna.shop.views.login.LoginViewModel
import com.watirna.shop.views.orderdetail.OrderDetailActivity
import com.watirna.shop.views.orderdetail.OrderDetailViewModel
import com.watirna.shop.views.requestdetail.RequestDetailActivity
import com.watirna.shop.views.requestdetail.RequestDetailViewModel
import com.watirna.shop.views.orders.incoming.IncomingViewModel
import com.watirna.shop.views.product.ProductMainFragment
import com.watirna.shop.views.product.ProductMainViewModel
import com.watirna.shop.views.product.addons.AddOnListViewModel
import com.watirna.shop.views.product.addons.create.AddAddOnViewModel
import com.watirna.shop.views.product.addons.edit.EditAddOnViewModel
import com.watirna.shop.views.product.category.CategoriesViewModel
import com.watirna.shop.views.product.category.create.CreateCategoryViewModel
import com.watirna.shop.views.product.category.edit.EditCategoryViewModel
import com.watirna.shop.views.product.categorySelection.CategorySelectionViewModel
import com.watirna.shop.views.product.productDML.ProductListViewModel
import com.watirna.shop.views.product.productDML.addonSelection.AddOnSelectionViewModel
import com.watirna.shop.views.product.productDML.create.ProductCreateActivity
import com.watirna.shop.views.product.productDML.create.ProductCreateViewModel
import com.watirna.shop.views.product.productDML.edit.editproduct.EditProduct
import com.watirna.shop.views.product.productDML.edit.editproduct.EditProductViewModel
import com.watirna.shop.views.product.productDML.price.PriceSelectionActivity
import com.watirna.shop.views.profile.ProfileViewModel
import com.watirna.shop.views.revenue.RevenueViewModel
import com.watirna.shop.views.selectcuisine.SelectCuisine
import com.watirna.shop.views.selectcuisine.SelectCuisineViewModel
import com.watirna.shop.views.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        NetworkModule::class,
        ApplicationModule::class,
        AppContainerModule::class
    )
)
interface AppComponent {

    fun inject(commonMethods: CommanMethods)

    fun inject(dateTimeUtility: DateTimeUtility)

    fun inject(splashActivity: SplashActivity)

    fun inject(baseRepository: BaseRepository)

    fun inject(shopRepository: ShopRepository)

    fun inject(sessionManager: SessionManager)

    fun inject(imageUtils: ImageUtils)

    fun inject(constants: Constants)

    fun inject(myFirebaseMessagingService: MyFirebaseMessagingService)

    //Adapters
    fun inject(incomingOrderAdapter: IncomingOrderAdapter)

    fun inject(orderListAdapter: OrderListAdapter)

    fun inject(orderListHistoryDetailsAdapter: OrderListHistoryDetailsAdapter)

    fun inject(historyCommonAdapter: HistoryCommonAdapter)

    fun inject(categoryAdapter: CategoryAdapter)

    //Activity
    fun inject(loginActivity: LoginActivity)

    fun inject(dashboardActivity: DashboardActivity)

    fun inject(orderDetailActivity: RequestDetailActivity)

    fun inject(orderDetailActivity: OrderDetailActivity)

    fun inject(changePasswordActivity: ChangePasswordActivity)

    fun inject(editRestaurantActivity: EditRestaurantActivity)

    fun inject(selectCuisine: SelectCuisine)

    fun inject(forgotPasswordActivity: ForgotPasswordActivity)


    //ViewModel
    fun inject(loginViewModel: LoginViewModel)

    fun inject(incomingViewModel: IncomingViewModel)

    fun inject(dashboardViewModel: DashboardViewModel)

    fun inject(orderDetailViewModel: RequestDetailViewModel)

    fun inject(changePasswordViewModel: ChangePasswordViewModel)

    fun inject(editTimeViewModel: EditTimeViewModel)

    fun inject(fragmentOngoingViewModel: FragmentOngoingViewModel)

    fun inject(fragmentPastViewModel: FragmentPastViewModel)

    fun inject(fragmentCancelViewModel: FragmentCancelViewModel)

    fun inject(profileViewModel: ProfileViewModel)

    fun inject(editRestaurantViewModel: EditRestaurantViewModel)

    fun inject(selectCuisineViewModel: SelectCuisineViewModel)

    fun inject(viewModel: RevenueViewModel)

    fun inject(viewModel: OrderDetailViewModel)

    fun inject(forgotPasswordViewModel: ForgotPasswordViewModel)


    //Fragment
    fun inject(acceptOrderFragment: AcceptOrderFragment)

    fun inject(fragmentPastOrders: FragmentPastOrders)

    fun inject(frgmentOngoingOrders: FragmentOngoingOrders)

    fun inject(fragmentCancelOrders: FragmentCancelOrders)
    fun inject(productMainViewModel: ProductMainViewModel)
    fun inject(addOnListViewModel: AddOnListViewModel)
    fun inject(addAddOnViewModel: AddAddOnViewModel)
    fun inject(editAddOnViewModel: EditAddOnViewModel)
    fun inject(categoriesViewModel: CategoriesViewModel)
    fun inject(createCategoryViewModel: CreateCategoryViewModel)
    fun inject(editCategoryViewModel: EditCategoryViewModel)
    fun inject(productListViewModel: ProductListViewModel)
    fun inject(productCreateViewModel: ProductCreateViewModel)
    fun inject(categorySelectionViewModel: CategorySelectionViewModel)
    fun inject(addOnSelectionViewModel: AddOnSelectionViewModel)
    fun inject(accountFragment: AccountFragment)
    fun inject(productMainFragment: ProductMainFragment)
    fun inject(productCreateActivity: ProductCreateActivity)
    fun inject(priceSelectionActivity: PriceSelectionActivity)
    fun inject(editProductViewModel: EditProductViewModel)
    fun inject(editProduct: EditProduct)
    fun inject(addOnAdapter: AddOnAdapter)
    fun inject(productListAdapter: ProductListAdapter)
}