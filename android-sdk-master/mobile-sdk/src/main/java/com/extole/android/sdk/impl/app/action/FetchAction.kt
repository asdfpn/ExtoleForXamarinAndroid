package com.extole.android.sdk.impl.app.action

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID
import com.extole.android.sdk.Action
import com.extole.android.sdk.Action.ActionType.FETCH
import com.extole.android.sdk.impl.ExtoleInternal
import com.extole.android.sdk.impl.app.AppEvent
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class FetchAction(
    @SerializedName("zones") val zones: List<String>,
    @SerializedName("data") val data: Map<String, String>? = emptyMap()
) : Action {

    override suspend fun execute(event: AppEvent, extole: ExtoleInternal) {
        withContext(Dispatchers.IO) {
            val allData = prepareRequestData(extole)
            val prefetchedZones = extole.fetchServices().getZoneService().getZones(
                zones.toSet(),
                allData,
                extole.labels()
            )
            prefetchedZones.getAll().forEach { entry ->
                entry.value?.let { extole.zonesResponse().add(entry.key, it) }
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun prepareRequestData(extole: ExtoleInternal): MutableMap<String, String> {
        val deviceId = Secure.getString(
            extole.context().getAppContext().contentResolver,
            ANDROID_ID
        )
        val allData = extole.data().toMutableMap()
        allData.putAll(data ?: emptyMap())
        allData["device_id"] = deviceId
        allData["os"] = Build.VERSION.RELEASE
        return allData
    }

    override fun getType(): Action.ActionType = FETCH

    override fun toString(): String {
        return "Action: ${getType()}, zones: $zones"
    }
}
