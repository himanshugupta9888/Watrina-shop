package com.watirna.shop.dependencies.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.utils.*
import dagger.Module
import dagger.Provides
import java.util.ArrayList
import javax.inject.Singleton


@Module(includes = arrayOf(ApplicationModule::class))
class  AppContainerModule{

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    internal fun providesSessionManager(): SessionManager {
        return SessionManager()
    }


    @Provides
    @Singleton
    internal fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    internal fun providesStringArrayList(): ArrayList<String> {
        return ArrayList()
    }

    @Provides
    @Singleton
    internal fun providesValidator(): Validator {
        return Validator()
    }

    @Provides
    @Singleton
    internal  fun providesCustomLoader(context:Context):CustomLoaderDialog{
        return  CustomLoaderDialog(context,true)
    }

    @Provides
    @Singleton
    internal  fun providesCommonMethods():CommanMethods{
        return CommanMethods()
    }

    @Provides
    @Singleton
    fun providesDateTimeUtility(): DateTimeUtility {
        return DateTimeUtility()
    }

    @Provides
    @Singleton
    fun providesShopRepository():ShopRepository{
        return  ShopRepository()
    }

    @Provides
    @Singleton
    fun providesImageUtils():ImageUtils{
        return ImageUtils()
    }
}
