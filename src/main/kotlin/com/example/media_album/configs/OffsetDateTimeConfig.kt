package com.example.media_album.config

import com.netflix.graphql.dgs.DgsScalar
import graphql.language.StringValue
import graphql.schema.*
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * ✅Custom Scalar cho GraphQL để serialize/deserialize OffsetDateTime hoặc Instant
 */
@DgsScalar(name = "DateTime")
class OffsetDateTimeConfig : Coercing<OffsetDateTime, String> {

    @Deprecated("Deprecated in Java")
    override fun serialize(dataFetcherResult: Any): String {
        return when (dataFetcherResult) {
            is OffsetDateTime -> dataFetcherResult.toInstant().toString()
            is Instant -> dataFetcherResult.toString()
            is String -> dataFetcherResult
            else -> throw CoercingSerializeException("Cannot serialize value $dataFetcherResult as OffsetDateTime")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun parseValue(input: Any): OffsetDateTime {
        return when (input) {
            is String -> OffsetDateTime.parse(input)
            else -> throw CoercingParseValueException("Cannot parse value $input as OffsetDateTime")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun parseLiteral(input: Any): OffsetDateTime {
        val value = (input as? StringValue)?.value
            ?: throw CoercingParseLiteralException("Expected AST type 'StringValue' for OffsetDateTime")
        return OffsetDateTime.parse(value)
    }
}
