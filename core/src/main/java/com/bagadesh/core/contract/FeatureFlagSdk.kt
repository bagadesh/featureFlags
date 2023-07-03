package com.bagadesh.core.contract

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeatureFlagSdk {

    fun getReader(): Reader

    fun getWriter(): Writer

}