package com.watirna.shop.datamodel.error

class APIError(responseData: ResponseData) {
    class ResponseData(val message: String)
}