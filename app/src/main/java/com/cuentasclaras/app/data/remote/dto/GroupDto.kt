package com.cuentasclaras.app.data.remote.dto

import com.cuentasclaras.app.data.local.entity.GroupEntity

data class GroupDto(
    val id: String,
    val name: String,
    val description: String,
    val membersCount: Int,
    val userBalance: Double,
    val updatedAt: String
) {
    fun toEntity(): GroupEntity {
        return GroupEntity(
            id = id,
            name = name,
            description = description,
            membersCount = membersCount,
            userBalance = userBalance,
            updatedAt = updatedAt
        )
    }
}