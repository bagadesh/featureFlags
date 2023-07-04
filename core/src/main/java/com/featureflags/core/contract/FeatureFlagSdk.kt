package com.featureflags.core.contract

/**
 * Created by bagadesh on 26/06/23.
 */
interface FeatureFlagSdk {

    fun getReader(): com.featureflags.core.contract.Reader

    fun getWriter(): com.featureflags.core.contract.Writer

}