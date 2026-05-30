package com.example.bia.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class OFFResponse(
    val product: OFFProduct? = null,
    val status: Int // 1 = found, 0 = not found
)

@Serializable
data class OFFProduct(
    @SerialName("product_name") val name: String? = null,
    val brands: String? = null,
    @SerialName("nutriments") val nutriments: OFFNutrients? = null
)

@Serializable
data class OFFNutrients(
    @SerialName("energy-kcal_100g") val calories100g: Double? = null,
    @SerialName("proteins_100g") val protein100g: Double? = null,
    @SerialName("carbohydrates_100g") val carbs100g: Double? = null,
    @SerialName("fat_100g") val fat100g: Double? = null
)

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = "code,product_name,brands,nutriments"
    ): OFFResponse

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }
}
