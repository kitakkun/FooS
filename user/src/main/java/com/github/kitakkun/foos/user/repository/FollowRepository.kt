package com.github.kitakkun.foos.user.repository

import com.github.kitakkun.foos.user.model.DatabaseFollow

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

    suspend fun create(follower: String, followee: String)

    suspend fun delete(follower: String, followee: String)
}
