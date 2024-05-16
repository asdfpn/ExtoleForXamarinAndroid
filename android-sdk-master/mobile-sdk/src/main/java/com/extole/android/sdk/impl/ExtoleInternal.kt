package com.extole.android.sdk.impl

import android.content.Context
import com.extole.android.sdk.Action
import com.extole.android.sdk.Extole
import com.extole.android.sdk.ExtoleLogger
import com.extole.android.sdk.ExtoleSdkException
import com.extole.android.sdk.ExtoleServices
import com.extole.android.sdk.Operation
import com.extole.android.sdk.ProtocolHandler
import com.extole.android.sdk.impl.app.App
import com.extole.android.sdk.impl.app.ExtoleServicesImpl
import org.json.JSONObject

interface ExtoleInternal : Extole {

    companion object {
        fun init(
            programDomain: String? = null,
            appName: String? = null,
            sandbox: String = "prod-prod",
            context: Context,
            labels: Set<String> = emptySet(),
            data: Map<String, String> = emptyMap(),
            appData: Map<String, String> = emptyMap(),
            appHeaders: Map<String, String> = emptyMap(),
            identifier: String? = null,
            listenToEvents: Boolean = true,
            configurationLoader: ((app: App, data: Map<String, Any>) -> List<Operation>)? = null,
            additionalProtocolHandlers: List<ProtocolHandler> = emptyList(),
            disabledActions: Set<Action.ActionType> = emptySet(),
            jwt: String? = null
        ): ExtoleInternal {
            val applicationContext =
                ApplicationContext(context, SharedPreferencesPersistence(context))
            val extole = ExtoleImpl(
                programDomain ?: getProgramDomain(applicationContext),
                appName ?: getAppName(applicationContext),
                sandbox,
                applicationContext,
                labels,
                data.toMutableMap(),
                appData.toMutableMap(),
                appHeaders.toMutableMap(),
                identifier,
                listenToEvents,
                additionalProtocolHandlers,
                configurationLoader,
                disabledActions,
                jwt
            )
            return extole
        }

        private fun getAppName(context: ApplicationContext): String {
            val applicationInfo = context.getApplicationInfo()
            val appName = applicationInfo?.metaData?.getString(Extole.APP_NAME_KEY)
            if (appName.isNullOrEmpty()) {
                throw ExtoleSdkException("Application name is not declared")
            }
            return appName
        }

        private fun getProgramDomain(context: ApplicationContext): String {
            val applicationInfo = context.getApplicationInfo()
            val programDomain = applicationInfo?.metaData?.getString(Extole.PROGRAM_DOMAIN_KEY)
            if (programDomain.isNullOrEmpty()) {
                throw ExtoleSdkException("Program domain is not declared")
            }
            return programDomain
        }
    }

    fun logger(logger: ExtoleLogger)
    fun getHeaders(): Map<String, String>
    fun labels(): Set<String>
    fun data(): MutableMap<String, String>
    fun programDomain(): String
    fun accessToken(): String?
    fun zonesResponse(): Zones
    fun operations(): MutableList<Operation>
    fun jsonConfiguration(): MutableList<JSONObject>
    fun disabledActions(): Set<Action.ActionType>

}
