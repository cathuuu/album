package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.input.CreateUserInput
import com.example.media_album.models.dtos.input.UpdateUserInput
import com.example.media_album.services.UserService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.bson.types.ObjectId


@DgsComponent
class UserMutation(
    private val userService: UserService,
) {
    @DgsMutation
    fun createUser(@InputArgument input: CreateUserInput): UserDocument? {
        val roleObjectIds = input.roleIds.map { ObjectId(it) }
        val newUser = UserDocument(
            username = input.username,
            password = input.password,
            fullName = input.fullName,
            gender = input.gender,
            roleIds = roleObjectIds,
            dob = input.dob, // GraphQL auto convert
            email = input.email,
            phone = input.phone
        )
        return userService.save(newUser)
    }
    @DgsMutation
    fun updateUser(@InputArgument input: UpdateUserInput) : UserDocument? {
        return userService.updateUser(input)
    }

    @DgsMutation
    fun deleteUser(@InputArgument(name = "id") id: String): Boolean  {
        userService.deleteById(ObjectId(id))
        return true
    }
}