package com.lifecycle.demo.inject.data.net.bean

import kotlinx.serialization.Serializable

@Serializable
data class InterrogationDataBean(
    val page_index: Int,
    val page_size: Int,
    val result: MutableList<InterrogationBean>,
    val count: Int
)

@Serializable
class InterrogationBean(
    val images: List<AdditionalImages>,
    val dialogues: List<Dialogue>,
    val evaluate: Evaluate?,
    val description: String,
    val end_time: String,
    val id: String,
    val state: Int,
    val patient_name: String,
    val purpose: String,
    val exam: Exam
) {
}

@Serializable
data class Evaluate(
    val score: Int,
    val id: String,
    val sex: String
) {
}

@Serializable
data class AdditionalImages(
    val file_path: String
)
@Serializable
data class Message(
    val audio_duration: Int,
    val content: String,
    val type: Int
)
@Serializable
data class Exam(
    val age: Int,
    val sex:String,
    val accession_number: String
) {
}

@Serializable
data class Dialogue(
    val messages: List<Message>,
    val time: String
)
