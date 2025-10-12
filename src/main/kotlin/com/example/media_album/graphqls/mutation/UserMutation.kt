package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.RoleDocument
import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.input.CreateUserInput
import com.example.media_album.services.UserService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument


@DgsComponent
class UserMutation(
    private val userService: UserService,
) {
    @DgsMutation
    fun createUser(@InputArgument user: CreateUserInput): UserDocument? {
        val newUser = UserDocument(
            username = user.username,
            password = user.password,
            fullName = user.fullName,
            gender = user.gender,
            dob = user.dob, // GraphQL auto convert
            email = user.email,
            phone = user.phone
        )
        return userService.save(newUser)
    }
    @DgsMutation
    fun updateUser(@InputArgument userDocument: UserDocument) : UserDocument? {
        return userService.updateUser(userDocument)
    }

    @DgsMutation
    fun deleteUser(@InputArgument userDocument: UserDocument)  {
        userService.deleteById(userDocument.id)
    }
}