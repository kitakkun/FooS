package com.example.foos.data.model

/**
 * 特定ユーザ2人のフォロー関係を表現するデータクラス
 * @param selfId 自分のユーザID
 * @param otherId 相手のユーザID
 * @param following 相手をフォローしている
 * @param followed 相手にフォローされている
 */
data class FollowState(
    val selfId: String,
    val otherId: String,
    val following: Boolean,
    val followed: Boolean,
)
