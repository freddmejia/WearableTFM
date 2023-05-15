package tm.wearable.wearabletfm.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tm.wearable.wearabletfm.data.model.AuthInterceptor
import tm.wearable.wearabletfm.data.model.TokenManager
import tm.wearable.wearabletfm.data.repository.api.UserServiceRemote
import tm.wearable.wearabletfm.data.repository.datasource.remote.UserRemoteDataSource
import tm.wearable.wearabletfm.data.repository.repo.UserRepository
import javax.inject.Named
import javax.inject.Singleton
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.repository.api.MedicineServiceRemote
import tm.wearable.wearabletfm.data.repository.datasource.remote.MedicineRemoteDataSource
import tm.wearable.wearabletfm.data.repository.repo.MedicineRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerSharedPreferences(@ApplicationContext appContext: Context) : SharedPreferences =
        appContext.applicationContext.getSharedPreferences(appContext.getString(R.string.shared_preferences), Context.MODE_PRIVATE)

    @Provides
    fun provideTokenManager(sharedPreferences: SharedPreferences):
            TokenManager = TokenManager(sharedPreferences)

    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager):
            AuthInterceptor = AuthInterceptor(tokenManager)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Singleton
    @Provides
    @Named("api")
    fun provideRetrofit(gson: Gson,okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(Utils.domainApi)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    //user
    @Provides
    fun provideUserService(@Named("api") retrofit: Retrofit): UserServiceRemote = retrofit.create(UserServiceRemote::class.java)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userServiceRemote: UserServiceRemote) = UserRemoteDataSource(userServiceRemote)

    @Singleton
    @Provides
    fun provideUserRepository(userDataSourceRemote: UserRemoteDataSource) = UserRepository(userDataSourceRemote)

    //medicine
    @Provides
    fun provideMedicineService(@Named("api") retrofit: Retrofit): MedicineServiceRemote = retrofit.create(MedicineServiceRemote::class.java)

    @Singleton
    @Provides
    fun provideMedicineRemoteDataSource(medicineServiceRemote: MedicineServiceRemote) = MedicineRemoteDataSource(medicineServiceRemote)

    @Singleton
    @Provides
    fun provideMedicineRepository(medicineRemoteDataSource: MedicineRemoteDataSource) = MedicineRepository(medicineRemoteDataSource)


}