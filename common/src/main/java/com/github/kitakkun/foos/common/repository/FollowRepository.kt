package com.github.kitakkun.foos.common.repository

import com.github.kitakkun.foos.common.model.DatabaseFollow

interface FollowRepository {

    /**
     * 通常のフェッチ処理
     */
    suspend fun fetch(followee: String, follower: String): DatabaseFollow?

    /**
     * 指定したIDがfollowerとして定義されているデータベースエントリを取得
     */
    suspend fun fetchByFollowerId(followerId: String): List<DatabaseFollow>

    /**
     * 指定したID(複数)がfollowerとして定義されているデータベースエントリを取得
     */
    suspend fun fetchByFollowerIds(followerIds: List<String>): List<DatabaseFollow>

    /**
     * 指定したIDがfolloweeとして定義されているデータベースエントリを取得
     */
    suspend fun fetchByFolloweeId(followeeId: String): List<DatabaseFollow>

    /**
     * 指定したID(複数)がfolloweeとして定義されているデータベースエントリを取得
     */
    suspend fun fetchByFolloweeIds(followeeIds: List<String>): List<DatabaseFollow>

    suspend fun create(from: String, to: String)

    suspend fun delete(from: String, to: String)

    suspend fun fetchFollowerCount(userId: String): Int
    suspend fun fetchFollowingCount(userId: String): Int
}
