package com.example.foos.data.model

/**
 * 自分と特定ユーザとのフォロー関係を表現するデータクラス
 * @param following 自分が相手をフォローしている
 * @param followed 自分が相手にフォローされている
 */
data class MyFollowingState(
    val following: Boolean,
    val followed: Boolean,
)
