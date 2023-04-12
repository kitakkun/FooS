package com.github.kitakkun.foos.data.model

import com.github.kitakkun.foos.data.model.database.DatabasePost
import com.github.kitakkun.foos.data.model.database.DatabaseReaction
import com.github.kitakkun.foos.data.model.database.DatabaseUser

/**
 * 各データがまとまったリアクションデータ
 * @param user リアクションを行ったユーザ情報
 * @param post リアクションされた投稿情報
 * @param reaction リアクション情報
 */
data class Reaction(
    val user: DatabaseUser,
    val post: DatabasePost,
    val reaction: DatabaseReaction,
)
