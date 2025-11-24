package com.eager.questioncloud.payment.document

import com.github.jershell.kbson.KBson
import org.bson.Document
import org.springframework.core.convert.converter.Converter

class QuestionPaymentHistoryDocumentReadConverter : Converter<Document, QuestionPaymentHistoryDocument> {
    private val kBson = KBson()
    
    override fun convert(source: Document): QuestionPaymentHistoryDocument {
        return kBson.parse(QuestionPaymentHistoryDocument.serializer(), source.toBsonDocument())
    }
}