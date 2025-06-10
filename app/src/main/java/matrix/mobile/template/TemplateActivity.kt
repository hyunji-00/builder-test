/*
 * Copyright (C) Inswave Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Inswave Systems <webmaster@inswave.com>, 2021
 */

package matrix.mobile.template

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import matrix.mobile.MatrixMobile
import matrix.mobile.MatrixMobileCallback
import matrix.mobile.Profile
import matrix.mobile.config.MatrixMobileConfig
import matrix.mobile.config.MatrixMobileError
import matrix.mobile.config.RequestMatrixMobileConfigType
import matrix.mobile.config.ServerGroup
import matrix.mobile.config.WebViewEngineOptions
import matrix.mobile.extension.loadStartPage
import matrix.mobile.template.databinding.LoadingLayoutBinding
import matrix.mobile.template.databinding.TemplateMainBinding
import matrix.mobile.utils.WLog
import matrix.mobile.utils.closeApplication
import matrix.mobile.utils.launchSafety

/**
 * MatrixMobile TemplateActivity
 */
class TemplateActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "TemplateActivity"
    }

    private lateinit var binding: TemplateMainBinding
    private lateinit var matrixMobile: MatrixMobile

    private var mainWebView: WebView? = null

    // 서버 선택 화면 표출 여부.
    private var useServerSelectScreen = false


    /**
     * MatrixMobile Callback 객체.
     */
    private val matrixMobileCallback = object : MatrixMobileCallback {

        /**
         * matrixMobile.showServerSelect 호출 후 선택 한 그룹이 해당 함수로 콜백이 온다.
         */
        override fun onMatrixGroupSelect(group: ServerGroup) {
            WLog.d(TAG, "onMatrixGroupSelect group=$group")
            createMatrixMobile(group)
        }

        /**
         * matrixMobile.create 성공 시 해당 함수로 콜백이 온다.
         * matrixMobile.start 로 matrixMobile 를 시작한다.
         */
        override fun onMatrixCreated(tag: String) {
            WLog.d(TAG, "onMatrixCreated tag=$tag")
            matrixMobile.start(tag)
        }

        /**
         * matrixMobile.start 로 matrixMobile 가 정상적으로 작동이 되면 해당 함수로 콜백이 온다.
         * 해당 시점부터 웹뷰를 생성하여 사용이 가능하다.
         */
        override fun onMatrixStarted(tag: String) {
            WLog.d(TAG, "onMatrixStarted tag=$tag")
            matrixMobile.makeWebView(tag)
        }

        /**
         * matrixMobile.makeWebView 가 정상적으로 작동이 되면 해당 함수로 콜백이 온다.
         * 콜백온 matrixWebView 객체를 사용하여 이후 로직을 구현한다.
         */
        override fun onMatrixWebViewCreated(tag: String, matrixWebView: WebView) {
            WLog.d(TAG, "onMatrixWebViewCreated tag=$tag")

            mainWebView = matrixWebView
            binding.containerMainWeb01.visibility = View.VISIBLE
            binding.containerMainWeb01.addView(mainWebView)
            mainWebView?.loadStartPage()
        }

        /**
         * 웹으로 부터 Loading 화면을 닫으라는 호출이 오면 해당 함수가 호출된다.
         * 해당 함수에서 Loading 화면이 있는 경우 보이지 않게 처리를 해준다.
         */
        override fun onDismissLoadingLayout() {
            WLog.d(TAG, "onDismissLoadingLayout")
            binding.containerMainLoading.visibility = View.GONE
        }

        /**
         * 기기의 back key 를 누르면 해당함수로 호출이 온다.
         * $h.backKeyPressedListener 의 listener 객체로 back key 이벤트가 같이 전달된다.
         */
        override fun onBackPressed() {
            WLog.d(TAG, "onBackPressed")

            // 웹에서 앱 종료처리 및 백키에 대한 처리를 하는 경우 아래 코드는 제거하도록 한다.
            // 기본 값 웹에서 back key 를 관리하도록 되어 있어 해당 부분을 주석처리
//            runOnUiThread {
//                DialogUtils.exit(
//                    dialog = MaterialAlertDialogBuilder(this@TemplateActivity),
//                    title = getString(R.string.error_finish),
//                    message = getString(R.string.exit_or_not),
//                    onPositiveButtonClicked = {
//                        closeApplication()
//                    }
//                )
//            }
        }

        /**
         * MatrixMobile 동작중 오류 발생 시 해당 함수로 호출이 온다.
         * 에러 케이스에 따라 처리를 하면 된다.
         */
        override fun onMatrixError(tag: String, error: MatrixMobileError) {
            WLog.e(TAG, "onMatrixError [$tag] $error")

            runOnUiThread {
                DialogUtils.forceExit(
                    dialog = MaterialAlertDialogBuilder(this@TemplateActivity),
                    title = "",
                    message = "${error.errorMessage} (${error.errorReason} - $tag)",
                    onPositiveButtonClicked = {
                        closeApplication()
                    }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TemplateMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱에 더 넓은 화면 콘텐츠 표시 기능 off. android 15 이상 부터 기본 활성화.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            disableEdgeToEdge(binding.root)
        }

        // WLog 보여줄지 여부.
        MatrixMobileConfig.loggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        // [2023.10.18] jm.lee - 서버 버전에 따라 값 설정
        // ALL  matrixMobile server version - 1.3.6.5 이상 지원.
        // EACH matrixMobile server version - 1.3.6.0 이상 지원.
        MatrixMobileConfig.requestMatrixMobileConfigType = RequestMatrixMobileConfigType.EACH

        // 상황에 맞게 Loading 화면을 만들어서 사용하면 된다.
        LoadingLayoutBinding.inflate(layoutInflater)
            .also { loadingViewBinding ->
                binding.containerMainLoading.addView(loadingViewBinding.root)
                binding.containerMainLoading.visibility = View.VISIBLE
            }

        // 아래와 같이 MatrixMobileConfig.yaml 에 active 로 설정되어 있는 profile 의 정보를 사용할 수 있다.
        Profile.getActiveProfileInfo(this@TemplateActivity)
            .also { profile ->
                useServerSelectScreen = profile.get("useServerSelectScreen").asBoolean
            }

        // MatrixMobile 객체 생성.
        matrixMobile = MatrixMobile(this, matrixMobileCallback)

        showServerSelect()
    }

    /**
     * 더 넓은 화면에 콘텐츠 표시.
     *
     * Android 15 이상 부터 기본 활성화에 따라 대응이 안된 사이트의 경우
     * 웹 화면이 status, navigation 영역과 겹칠 수 있어, 설정을 disable 처리한다.
     *
     * 기능과 상관없이 화면의 영역이므로, 각 프로젝트에 맞게 설정 여부를 판단하여 처리 하면 된다.
     */
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun disableEdgeToEdge(rootView: View) {
        val windowInsetsController = WindowCompat.getInsetsController(window, rootView)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.layoutParams = (view.layoutParams as FrameLayout.LayoutParams).apply {
                topMargin = insets.top
                bottomMargin = insets.bottom
            }

            windowInsetsController.apply {
                isAppearanceLightStatusBars = true
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * MatrixMobile 구동하기 위한 내부 설정을 세팅하고, 실행한다.
     */
    private fun showServerSelect() {
        val serverGroup = matrixMobile.getServerGroupFromConfig()
            ?.onEach {
                WLog.d(TAG, "groupName: ${it.groupName}, serverList: ${it.serverList}")
            }

        when (useServerSelectScreen) {

            // 서버 선택화면을 보여준다.
            true -> matrixMobile.showServerSelect()

            // MatrixMobileConfig.yaml 의 active 된 profile 의 server 의 0번쨰 group 의 서버정보들로 MatrixMobile 초기작업을 시작한다.
            false -> if (serverGroup != null) createMatrixMobile(serverGroup[0])
        }
    }

    private fun createMatrixMobile(serverGroup: ServerGroup) {
        serverGroup.serverList.forEach { data ->
            lifecycleScope.launchSafety(
                coroutineContext = Dispatchers.IO,
                block = {
                    WLog.d(TAG, "create server data: $data")
                    matrixMobile.create(
                        tag = data.name,
                        serverData = data,

                        // 일반 요청
                        engineOptions = WebViewEngineOptions(okHttpClientBuilder = CustomOkHttpClient.getBuilder(develop = MatrixMobileConfig.loggable))

                        // 인증서 pinning 필요 시.
//                        engineOptions = WebViewEngineOptions(
//                            okHttpClientBuilder = CustomOkHttpClient.createPinnedClientBuilder(
//                                develop = MatrixMobileConfig.loggable,
//                                hostname = URI(data.url).host
//                            )
//                        )
                    )
                },
                handleException = {
                    WLog.e(TAG, "failed to matrixMobile create.", it)
                }
            )
        }
    }

    override fun onDestroy() {
        if (binding.containerMainWeb01.childCount > 0) {
            binding.containerMainWeb01.removeAllViews()
            mainWebView?.destroy()
        }

        super.onDestroy()
    }
}