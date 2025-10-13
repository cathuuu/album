package com.example.media_album.graphqls.mutation

import com.example.media_album.models.documents.UserDocument
import com.example.media_album.models.dtos.input.CreateUserInput
import com.example.media_album.models.dtos.input.UserInput
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
    fun createUser(@InputArgument userDocument: CreateUserInput): UserDocument? {
        val newUser = UserDocument(
            username = userDocument.username,
            password = userDocument.password,
            fullName = userDocument.fullName,
            gender = userDocument.gender,
            dob = userDocument.dob, // GraphQL auto convert
            email = userDocument.email,
            phone = userDocument.phone
        )
        return userService.save(newUser)
    }
    @DgsMutation
    fun updateUser(@InputArgument userDocument: UserInput) : UserDocument? {
        return userService.updateUser(userDocument)
    }

    @DgsMutation
    fun deleteUser(@InputArgument(name = "id") id: String): Boolean  {
        userService.deleteById(ObjectId(id))
        return true
    }
}