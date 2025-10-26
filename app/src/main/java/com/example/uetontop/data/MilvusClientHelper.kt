//package com.example.uetontop.data
//
//import io.milvus.client.MilvusServiceClient
//import io.milvus.param.ConnectParam
//import io.milvus.param.dml.SearchParam
//import io.milvus.param.collection.LoadCollectionParam
//import io.milvus.grpc.SearchResults
//import java.util.*
//
//class MilvusClientHelper {
//
//    private val milvusClient = MilvusServiceClient(
//        ConnectParam.newBuilder()
//            .withHost("localhost") // ⚠️ đổi thành host Milvus thật của bạn
//            .withPort(19530)
//            .build()
//    )
//
//    suspend fun querySimilarDocuments(queryText: String): List<String> {
//        try {
//            // ⚠️ Load collection trước khi search
//            milvusClient.loadCollection(
//                LoadCollectionParam.newBuilder()
//                    .withCollectionName("mental_health_docs")
//                    .build()
//            )
//
//            // ⚙️ TODO: Tạo vector embedding cho queryText
//            // Ở đây tạm thời để trống, bạn sẽ thêm phần gọi model embedding sau.
//            // Giả sử bạn đã có vectorFloatList: List<List<Float>>
//
//            // val searchParam = SearchParam.newBuilder()
//            //     .withCollectionName("mental_health_docs")
//            //     .withMetricType("L2")
//            //     .withTopK(3)
//            //     .withVectors(vectorFloatList)
//            //     .build()
//
//            // val response = milvusClient.search(searchParam)
//            // val results = (response.data as SearchResults).results
//            // Giả lập kết quả tạm
//            return listOf(
//                "Anxiety can cause sleep disruption and restlessness.",
//                "Mindfulness and breathing exercises may help reduce stress."
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return emptyList()
//        }
//    }
//}
