package com.example.recipesearch.database.savedrecipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipesearch.model.Components
import com.example.recipesearch.model.Instruction
import com.example.recipesearch.model.Recipe

@Entity(tableName = "recipes")
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "renditions") val renditions: List<Any>,
    @ColumnInfo(name = "total_time_tier") val totalTimeTier: Any?,
    @ColumnInfo(name = "seo_title") val seoTitle: String,
    @ColumnInfo(name = "video_id") val videoId: Any?,
    @ColumnInfo(name = "instructions") val instructions: List<Instruction>?,
    @ColumnInfo(name = "draft_status") val draftStatus: String,
    @ColumnInfo(name = "thumbnail_alt_text") val thumbnailAltText: String,
    @ColumnInfo(name = "credits") val credits: List<Any>,
    @ColumnInfo(name = "promotion") val promotion: String,
    @ColumnInfo(name = "facebook_posts") val facebookPosts: List<Any>,
    @ColumnInfo(name = "brand") val brand: Any?,
    @ColumnInfo(name = "show") val show: Any?,
    @ColumnInfo(name = "is_one_top") val isOneTop: Boolean,
    @ColumnInfo(name = "total_time_minutes") val totalTimeMinutes: Int?,
    @ColumnInfo(name = "servings_noun_plural") val servingsNounPlural: String,
    @ColumnInfo(name = "is_shoppable") val isShoppable: Boolean,
    @ColumnInfo(name = "price") val price: Any?,
    @ColumnInfo(name = "show_id") val showId: Int,
    @ColumnInfo(name = "buzz_id") val buzzId: Any?,
    @ColumnInfo(name = "tips_and_ratings_enabled") val tipsAndRatingsEnabled: Boolean,
    @ColumnInfo(name = "video_url") val videoUrl: Any?,
    @ColumnInfo(name = "approved_at") val approvedAt: Long,
    @ColumnInfo(name = "nutrition_visibility") val nutritionVisibility: String,
    @ColumnInfo(name = "servings_noun_singular") val servingsNounSingular: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "sections") val sections: List<Components>?,
    @ColumnInfo(name = "compilations") val compilations: List<Any>,
    @ColumnInfo(name = "beauty_url") val beautyUrl: Any?,
    @ColumnInfo(name = "original_video_url") val originalVideoUrl: Any?,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "keywords") val keywords: String,
    @ColumnInfo(name = "seo_path") val seoPath: Any?,
    @ColumnInfo(name = "prep_time_minutes") val prepTimeMinutes: Int,
    @ColumnInfo(name = "cook_time_minutes") val cookTimeMinutes: Int,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "inspired_by_url") val inspiredByUrl: Any?,
    @ColumnInfo(name = "topics") val topics: List<Any>,
    @ColumnInfo(name = "video_ad_content") val videoAdContent: Any?,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "user_ratings") val userRatings: Any?,
    @ColumnInfo(name = "brand_id") val brandId: Any?,
    @ColumnInfo(name = "tags") val tags: List<Any>,
    @ColumnInfo(name = "canonical_id") val canonicalId: String,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "nutrition") val nutrition: Any?,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String?,
    @ColumnInfo(name = "yields") val yields: String,
    @ColumnInfo(name = "recipe_api_id") val recipeApiId: Int,
    @ColumnInfo(name = "num_servings") val numServings: Int,
    @ColumnInfo(name = "aspect_ratio") val aspectRatio: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)

fun SavedRecipe.toRecipe(): Recipe {
    return Recipe(
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
