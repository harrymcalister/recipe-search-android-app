package com.example.recipesearch.model

import com.example.recipesearch.database.savedrecipe.SavedRecipe
import com.google.gson.annotations.SerializedName

data class Recipe(
    val renditions: List<Any>,
    @SerializedName("total_time_tier")
    val totalTimeTier: Any,
    @SerializedName("seo_title")
    val seoTitle: String,
    @SerializedName("video_id")
    val videoId: Any?,
    val instructions: List<Instruction>,
    @SerializedName("draft_status")
    val draftStatus: String,
    @SerializedName("thumbnail_alt_text")
    val thumbnailAltText: String,
    val credits: List<Any>,
    val promotion: String,
    @SerializedName("facebook_posts")
    val facebookPosts: List<Any>,
    val brand: Any?,
    val show: Any,
    @SerializedName("is_one_top")
    val isOneTop: Boolean,
    @SerializedName("total_time_minutes")
    val totalTimeMinutes: Int,
    @SerializedName("servings_noun_plural")
    val servingsNounPlural: String,
    @SerializedName("is_shoppable")
    val isShoppable: Boolean,
    val price: Any?,
    @SerializedName("show_id")
    val showId: Int,
    @SerializedName("buzz_id")
    val buzzId: Any?,
    @SerializedName("tips_and_ratings_enabled")
    val tipsAndRatingsEnabled: Boolean,
    @SerializedName("video_url")
    val videoUrl: Any?,
    @SerializedName("approved_at")
    val approvedAt: Long,
    @SerializedName("nutrition_visibility")
    val nutritionVisibility: String,
    @SerializedName("servings_noun_singular")
    val servingsNounSingular: String,
    val name: String?,
    @SerializedName("created_at")
    val createdAt: Long,
    val sections: List<Components>,
    val compilations: List<Any>,
    @SerializedName("beauty_url")
    val beautyUrl: Any?,
    @SerializedName("original_video_url")
    val originalVideoUrl: Any?,
    val country: String,
    val keywords: String,
    @SerializedName("seo_path")
    val seoPath: Any?,
    @SerializedName("prep_time_minutes")
    val prepTimeMinutes: Int,
    @SerializedName("cook_time_minutes")
    val cookTimeMinutes: Int,
    val description: String?,
    @SerializedName("inspired_by_url")
    val inspiredByUrl: Any?,
    val topics: List<Any>,
    @SerializedName("video_ad_content")
    val videoAdContent: Any?,
    val language: String,
    @SerializedName("user_ratings")
    val userRatings: Any,
    @SerializedName("brand_id")
    val brandId: Any?,
    val tags: List<Any>,
    @SerializedName("canonical_id")
    val canonicalId: String,
    val slug: String,
    val nutrition: Any,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,
    val yields: String,
    @SerializedName("id")
    val recipeApiId: Int,
    @SerializedName("num_servings")
    val numServings: Int,
    @SerializedName("aspect_ratio")
    val aspectRatio: String,
    @SerializedName("updated_at")
    val updatedAt: Long
)

data class Components(
    @SerializedName("components")
    val ingredients: List<Ingredient>
)

data class Ingredient(
    @SerializedName("raw_text")
    val rawText: String?,
    @SerializedName("ingredient")
    val ingredientName: IngredientName,
    @SerializedName("measurements")
    val measurements: List<Measurement>
)

data class IngredientName(
    @SerializedName("display_singular")
    val displaySingular: String,
    @SerializedName("display_plural")
    val displayPlural: String
)

data class Measurement(
    val unit: MeasurementUnit,
    val quantity: String
)

enum class MeasurementSystem(system: String) {
    METRIC("metric"),
    IMPERIAL("imperial"),
    NONE("none")
}

data class MeasurementUnit(
    val system: MeasurementSystem,
    val name: String,
    val abbreviation: String,
    @SerializedName("display_singular")
    val displaySingular: String,
    @SerializedName("display_plural")
    val displayPlural: String
)

data class Instruction(
    val position: Int,
    @SerializedName("display_text")
    val displayText: String,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("end_time")
    val endTime: Int
)

fun Recipe.toSavedRecipe(): SavedRecipe {
    return SavedRecipe(
        renditions = this.renditions,
        totalTimeTier = this.totalTimeTier,
        seoTitle = this.seoTitle,
        videoId = this.videoId,
        instructions = this.instructions,
        draftStatus = this.draftStatus,
        thumbnailAltText = this.thumbnailAltText,
        credits = this.credits,
        promotion = this.promotion,
        facebookPosts = this.facebookPosts,
        brand = this.brand,
        show = this.show,
        isOneTop = this.isOneTop,
        totalTimeMinutes = this.totalTimeMinutes,
        servingsNounPlural = this.servingsNounPlural,
        isShoppable = this.isShoppable,
        price = this.price,
        showId = this.showId,
        buzzId = this.buzzId,
        tipsAndRatingsEnabled = this.tipsAndRatingsEnabled,
        videoUrl = this.videoUrl,
        approvedAt = this.approvedAt,
        nutritionVisibility = this.nutritionVisibility,
        servingsNounSingular = this.servingsNounSingular,
        name = this.name,
        createdAt = this.createdAt,
        sections = this.sections,
        compilations = this.compilations,
        beautyUrl = this.beautyUrl,
        originalVideoUrl = this.originalVideoUrl,
        country = this.country,
        keywords = this.keywords,
        seoPath = this.seoPath,
        prepTimeMinutes = this.prepTimeMinutes,
        cookTimeMinutes = this.cookTimeMinutes,
        description = this.description,
        inspiredByUrl = this.inspiredByUrl,
        topics = this.topics,
        videoAdContent = this.videoAdContent,
        language = this.language,
        userRatings = this.userRatings,
        brandId = this.brandId,
        tags = this.tags,
        canonicalId = this.canonicalId,
        slug = this.slug,
        nutrition = this.nutrition,
        thumbnailUrl = this.thumbnailUrl,
        yields = this.yields,
        recipeApiId = this.recipeApiId,
        numServings = this.numServings,
        aspectRatio = this.aspectRatio,
        updatedAt = this.updatedAt
    )
}

