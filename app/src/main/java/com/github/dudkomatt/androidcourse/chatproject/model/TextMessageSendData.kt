/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.github.dudkomatt.androidcourse.chatproject.model

import org.openapitools.client.models.MessageReceiveDataText
import org.openapitools.client.models.TextMessageSendDataImage

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Contextual

/**
 * 
 *
 * @param text 
 * @param image 
 */
@Serializable

data class TextMessageSendData (

    @SerialName(value = "Text")
    val text: MessageReceiveDataText? = null,

    @SerialName(value = "Image")
    val image: TextMessageSendDataImage? = null

) {


}
