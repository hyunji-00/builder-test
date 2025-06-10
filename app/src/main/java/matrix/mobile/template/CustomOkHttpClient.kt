/*
 * Copyright (C) Inswave Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Inswave Systems <webmaster@inswave.com>, 2023
 */

package matrix.mobile.template

import matrix.mobile.base.manager.CertificatePinningManager
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.CookieManager
import java.util.concurrent.TimeUnit

/**
 * @author tarkarn
 * @since 2023.04.06
 */
object CustomOkHttpClient {

    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

    fun getBuilder(develop: Boolean = false): OkHttpClient.Builder =
        OkHttpClient
            .Builder()
            .commonSettings(develop)

    /**
     * Pinning 필요 한 경우 사용.
     * 보통의 경우는 getBuilder 사용 할 것.
     *
     * CertificatePinningManager 의 Builder 를 직접 사용 하는 방식
     */
    fun createPinnedClientBuilder(develop: Boolean = false, hostname: String = "mre.inswave.kr"): OkHttpClient.Builder =
        CertificatePinningManager
            .createPinnedOkHttpClientBuilder(hostname)
            .commonSettings(develop)

    /**
     * Pinning 필요 한 경우 사용.
     * 보통의 경우는 getBuilder 사용 할 것.
     *
     * 기존 Builder 에 CertificatePinner 를 추가 하는 방식
     */
    fun createClientBuilderWithPinning(develop: Boolean = false,  hostname: String = "mre.inswave.kr"): OkHttpClient.Builder =
        getBuilder(develop)
            .certificatePinner(
                CertificatePinningManager
                    .createCertificatePinnerBuilder(hostname)
                    .build()
            )

    /**
     * OkHttpClient Builder 공통 설정
     */
    private fun OkHttpClient.Builder.commonSettings(develop: Boolean) = apply {
        if (develop) {
            addInterceptor(httpLoggingInterceptor)
        }

        cookieJar(JavaNetCookieJar(CookieManager()))
        connectTimeout(15, TimeUnit.SECONDS)
        readTimeout(15, TimeUnit.SECONDS)
        retryOnConnectionFailure(false)
    }
}