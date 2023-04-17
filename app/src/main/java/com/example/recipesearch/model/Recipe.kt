package com.example.recipesearch.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    val renditions: List<Any>,
    @SerializedName("total_time_tier")
    val totalTimeTier: Any,
    @SerializedName("seo_title")
    val seoTitle: String,
    @SerializedName("video_id")
    val videoId: Any?,
    val instructions: List<Any>,
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
    val price: Any,
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
    val name: String,
    @SerializedName("created_at")
    val createdAt: Long,
    val sections: List<Any>,
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
    val description: String,
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
    val thumbnailUrl: String,
    val yields: String,
    val id: Int,
    @SerializedName("num_servings")
    val numServings: Int,
    @SerializedName("aspect_ratio")
    val aspectRatio: String,
    @SerializedName("updated_at")
    val updatedAt: Long
)