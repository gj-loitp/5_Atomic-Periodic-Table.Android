package com.mckimquyen.atomicPeriodicTable.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.act.IsotopesActExperimental
import com.mckimquyen.atomicPeriodicTable.pref.AtomicCovalentPref
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusCalPref
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusEmpPref
import com.mckimquyen.atomicPeriodicTable.pref.AtomicVanPref
import com.mckimquyen.atomicPeriodicTable.pref.BoilingPref
import com.mckimquyen.atomicPeriodicTable.pref.DegreePref
import com.mckimquyen.atomicPeriodicTable.pref.DensityPref
import com.mckimquyen.atomicPeriodicTable.pref.ElectronegativityPref
import com.mckimquyen.atomicPeriodicTable.pref.ElementSendAndLoad
import com.mckimquyen.atomicPeriodicTable.pref.FavoriteBarPref
import com.mckimquyen.atomicPeriodicTable.pref.FavoritePhase
import com.mckimquyen.atomicPeriodicTable.pref.FusionHeatPref
import com.mckimquyen.atomicPeriodicTable.pref.MeltingPref
import com.mckimquyen.atomicPeriodicTable.pref.OfflinePreference
import com.mckimquyen.atomicPeriodicTable.pref.SendIso
import com.mckimquyen.atomicPeriodicTable.pref.SpecificHeatPref
import com.mckimquyen.atomicPeriodicTable.pref.VaporizationHeatPref
import com.mckimquyen.atomicPeriodicTable.util.Pasteur
import com.mckimquyen.atomicPeriodicTable.util.ToastUtil
import com.mckimquyen.atomicPeriodicTable.util.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.a_element_info.elementImage
import kotlinx.android.synthetic.main.a_element_info.elementTitle
import kotlinx.android.synthetic.main.a_element_info.frame
import kotlinx.android.synthetic.main.a_element_info.nextBtn
import kotlinx.android.synthetic.main.a_element_info.offlineDiv
import kotlinx.android.synthetic.main.a_element_info.previousBtn
import kotlinx.android.synthetic.main.a_element_info.wikipediaBtn
import kotlinx.android.synthetic.main.v_d_atomic.atomicRadiusEText
import kotlinx.android.synthetic.main.v_d_atomic.atomicRadiusText
import kotlinx.android.synthetic.main.v_d_atomic.covalentRadiusText
import kotlinx.android.synthetic.main.v_d_atomic.electronConfigText
import kotlinx.android.synthetic.main.v_d_atomic.ionChargeText
import kotlinx.android.synthetic.main.v_d_atomic.ionizationEnergiesText
import kotlinx.android.synthetic.main.v_d_atomic.oxView
import kotlinx.android.synthetic.main.v_d_atomic.vanDerWaalsRadiusText
import kotlinx.android.synthetic.main.v_d_electromagnetic.elementElectricalType
import kotlinx.android.synthetic.main.v_d_electromagnetic.elementMagneticType
import kotlinx.android.synthetic.main.v_d_electromagnetic.elementResistivity
import kotlinx.android.synthetic.main.v_d_electromagnetic.elementSuperconductingPoint
import kotlinx.android.synthetic.main.v_d_nuclear.isotopesFrame
import kotlinx.android.synthetic.main.v_d_nuclear.neutronCrossSectionalText
import kotlinx.android.synthetic.main.v_d_nuclear.radioactiveText
import kotlinx.android.synthetic.main.v_d_overview.descriptionName
import kotlinx.android.synthetic.main.v_d_overview.dscBtn
import kotlinx.android.synthetic.main.v_d_overview.electronsEl
import kotlinx.android.synthetic.main.v_d_overview.elementAppearance
import kotlinx.android.synthetic.main.v_d_overview.elementDiscoveredBy
import kotlinx.android.synthetic.main.v_d_overview.elementElectrons
import kotlinx.android.synthetic.main.v_d_overview.elementGroup
import kotlinx.android.synthetic.main.v_d_overview.elementName
import kotlinx.android.synthetic.main.v_d_overview.elementNeutronsCommon
import kotlinx.android.synthetic.main.v_d_overview.elementProtons
import kotlinx.android.synthetic.main.v_d_overview.elementYear
import kotlinx.android.synthetic.main.v_d_properties.elementAtomicNumber
import kotlinx.android.synthetic.main.v_d_properties.elementAtomicWeight
import kotlinx.android.synthetic.main.v_d_properties.elementBlock
import kotlinx.android.synthetic.main.v_d_properties.elementDensity
import kotlinx.android.synthetic.main.v_d_properties.elementElectronegativty
import kotlinx.android.synthetic.main.v_d_properties.elementShellsElectrons
import kotlinx.android.synthetic.main.v_d_properties.modelView
import kotlinx.android.synthetic.main.v_d_properties.spImg
import kotlinx.android.synthetic.main.v_d_properties.spOffline
import kotlinx.android.synthetic.main.v_d_temperatures.elementBoilingCelsius
import kotlinx.android.synthetic.main.v_d_temperatures.elementBoilingFahrenheit
import kotlinx.android.synthetic.main.v_d_temperatures.elementBoilingKelvin
import kotlinx.android.synthetic.main.v_d_temperatures.elementMeltingCelsius
import kotlinx.android.synthetic.main.v_d_temperatures.elementMeltingFahrenheit
import kotlinx.android.synthetic.main.v_d_temperatures.elementMeltingKelvin
import kotlinx.android.synthetic.main.v_d_thermodynamic.phaseIcon
import kotlinx.android.synthetic.main.v_d_thermodynamic.tvFusionHeatText
import kotlinx.android.synthetic.main.v_d_thermodynamic.tvPhaseText
import kotlinx.android.synthetic.main.v_d_thermodynamic.tvSpecificHeatText
import kotlinx.android.synthetic.main.v_d_thermodynamic.tvVaporizationHeatText
import kotlinx.android.synthetic.main.view_detail_emission.ivSpImgFetail
import kotlinx.android.synthetic.main.view_favorite_bar.aCalculatedF
import kotlinx.android.synthetic.main.view_favorite_bar.aCalculatedLay
import kotlinx.android.synthetic.main.view_favorite_bar.aEmpiricalF
import kotlinx.android.synthetic.main.view_favorite_bar.aEmpiricalLay
import kotlinx.android.synthetic.main.view_favorite_bar.boilingF
import kotlinx.android.synthetic.main.view_favorite_bar.boilingLay
import kotlinx.android.synthetic.main.view_favorite_bar.covalentF
import kotlinx.android.synthetic.main.view_favorite_bar.covalentLay
import kotlinx.android.synthetic.main.view_favorite_bar.densityF
import kotlinx.android.synthetic.main.view_favorite_bar.densityLay
import kotlinx.android.synthetic.main.view_favorite_bar.electronegativityF
import kotlinx.android.synthetic.main.view_favorite_bar.electronegativityLay
import kotlinx.android.synthetic.main.view_favorite_bar.fusionHeatF
import kotlinx.android.synthetic.main.view_favorite_bar.fusionHeatLay
import kotlinx.android.synthetic.main.view_favorite_bar.meltingF
import kotlinx.android.synthetic.main.view_favorite_bar.meltingLay
import kotlinx.android.synthetic.main.view_favorite_bar.molarMassF
import kotlinx.android.synthetic.main.view_favorite_bar.molarMassLay
import kotlinx.android.synthetic.main.view_favorite_bar.phaseF
import kotlinx.android.synthetic.main.view_favorite_bar.phaseLay
import kotlinx.android.synthetic.main.view_favorite_bar.specificHeatF
import kotlinx.android.synthetic.main.view_favorite_bar.specificHeatLay
import kotlinx.android.synthetic.main.view_favorite_bar.vanF
import kotlinx.android.synthetic.main.view_favorite_bar.vanLay
import kotlinx.android.synthetic.main.view_favorite_bar.vaporizationHeatF
import kotlinx.android.synthetic.main.view_favorite_bar.vaporizationHeatLay
import kotlinx.android.synthetic.main.view_loading_view.noImg
import kotlinx.android.synthetic.main.view_loading_view.progressBar
import kotlinx.android.synthetic.main.view_oxidiation_states.m1ox
import kotlinx.android.synthetic.main.view_oxidiation_states.m2ox
import kotlinx.android.synthetic.main.view_oxidiation_states.m3ox
import kotlinx.android.synthetic.main.view_oxidiation_states.m4ox
import kotlinx.android.synthetic.main.view_oxidiation_states.m5ox
import kotlinx.android.synthetic.main.view_oxidiation_states.ox0
import kotlinx.android.synthetic.main.view_oxidiation_states.p1ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p2ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p3ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p4ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p5ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p6ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p7ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p8ox
import kotlinx.android.synthetic.main.view_oxidiation_states.p9ox
import kotlinx.android.synthetic.main.view_shell_view.cardModelView
import kotlinx.android.synthetic.main.view_shell_view.configData
import kotlinx.android.synthetic.main.view_shell_view.eConfigData
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.ConnectException
import java.util.Locale
import kotlin.math.pow

abstract class InfoExt : AppCompatActivity(), View.OnApplyWindowInsetsListener {
    companion object {
        private const val TAG = "BaseActivity"
    }

    private var systemUiConfigured = false

    override fun attachBaseContext(context: Context) {
        val override = Configuration(context.resources.configuration)
        override.fontScale = 1.0f
        applyOverrideConfiguration(override)
        super.attachBaseContext(context)
    }


    override fun onStart() {
        super.onStart()
        val content = findViewById<View>(android.R.id.content)
        content.setOnApplyWindowInsetsListener(this)

        if (!systemUiConfigured) {
            systemUiConfigured = true
        }
    }

    open fun onApplySystemInsets(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int,
    ) = Unit

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        Pasteur.info(TAG, "height: ${insets.systemWindowInsetBottom}")
        onApplySystemInsets(
            insets.systemWindowInsetTop,
            insets.systemWindowInsetBottom,
            insets.systemWindowInsetLeft,
            insets.systemWindowInsetRight
        )
        return insets.consumeSystemWindowInsets()
    }

    @SuppressLint("SetTextI18n")
    fun readJson() {
        val jsonString: String?
        oxView.refreshDrawableState()

        try {
            //Setup json reader
            val elementSendAndLoadPreference = ElementSendAndLoad(this)
            val elementSendAndLoadValue = elementSendAndLoadPreference.getValue()
            if (elementSendAndLoadValue == "hydrogen") {
                previousBtn.visibility = View.GONE
            } else {
                previousBtn.visibility = View.VISIBLE
            }
            if (elementSendAndLoadValue == "oganesson") {
                nextBtn.visibility = View.GONE
            } else {
                nextBtn.visibility = View.VISIBLE
            }
            val ext = ".json"
            val elementJson = "$elementSendAndLoadValue$ext"

            //Read json
            val inputStream: InputStream = assets.open(elementJson)
            jsonString = inputStream.bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val jsonObject: JSONObject = jsonArray.getJSONObject(0)

            //optStrings from jsonObject or fallback
            val element = jsonObject.optString("element", "---")
            val description = jsonObject.optString("description", "---")
            val url = jsonObject.optString("link", "---")
            val short = jsonObject.optString("short", "---")
            val sElementElectrons = jsonObject.optString("element_electrons", "---")
            val elementShellElectrons = jsonObject.optString("element_shells_electrons", "---")
            val sElementYear = jsonObject.optString("element_year", "---")
            val sElementDiscoveredBy = jsonObject.optString("element_discovered_name", "---")
            val sElementProtons = jsonObject.optString("element_protons", "---")
            val sElementNeutronsCommon = jsonObject.optString("element_neutron_common", "---")
            val sElementGroup = jsonObject.optString("element_group", "---")
            val elementElectronegativity = jsonObject.optString("element_electronegativty", "---")
            val wikipedia = jsonObject.optString("wikilink", "---")
            val sElementBoilingKelvin = jsonObject.optString("element_boiling_kelvin", "---")
            val sElementBoilingCelsius = jsonObject.optString("element_boiling_celsius", "---")
            val sElementBoilingFahrenheit =
                jsonObject.optString("element_boiling_fahrenheit", "---")
            val sElementMeltingKelvin = jsonObject.optString("element_melting_kelvin", "---")
            val sElementMeltingCelsius = jsonObject.optString("element_melting_celsius", "---")
            val sElementMeltingFahrenheit =
                jsonObject.optString("element_melting_fahrenheit", "---")
            val sElementAtomicNumber = jsonObject.optString("element_atomic_number", "---")
            val sElementAtomicWeight = jsonObject.optString("element_atomicmass", "---")
            val sElementDensity = jsonObject.optString("element_density", "---")
            val elementModelUrl = jsonObject.optString("element_model", "---")
            val sElementAppearance = jsonObject.optString("element_appearance", "---")
            val sElementBlock = jsonObject.optString("element_block", "---")
//            val elementCrystalStructure = jsonObject.optString("element_crystal_structure", "---")
            val fusionHeat = jsonObject.optString("element_fusion_heat", "---")
            val specificHeatCapacity = jsonObject.optString("element_specific_heat_capacity", "---")
            val vaporizationHeat = jsonObject.optString("element_vaporization_heat", "---")
            val phaseText = jsonObject.optString("element_phase", "---")

            //atomic view
            val electronConfig = jsonObject.optString("element_electron_config", "---")
            val ionCharge = jsonObject.optString("element_ion_charge", "---")
            val ionizationEnergies = jsonObject.optString("element_ionization_energy", "---")
            val atomicRadiusE = jsonObject.optString("element_atomic_radius_e", "---")
            val atomicRadius = jsonObject.optString("element_atomic_radius", "---")
            val covalentRadius = jsonObject.optString("element_covalent_radius", "---")
            val vanDerWaalsRadius = jsonObject.optString("element_van_der_waals", "---")
            val oxidationNeg1 = jsonObject.optString("oxidation_state_neg", "---")
            val oxidationPos1 = jsonObject.optString("oxidation_state_pos", "---")

            //Electromagnetic Properties
            val electricalType = jsonObject.optString("electrical_type", "---")
            val resistivity = jsonObject.optString("resistivity", "---")
            val rMultiplier = jsonObject.optString("resistivity_mult", "---")
            val magneticType = jsonObject.optString("magnetic_type", "---")
            val superconductingPoint = jsonObject.optString("superconducting_point", "---")

            //Nuclear Properties
            val isRadioactive = jsonObject.optString("radioactive", "---")
            val neutronCrossSection = jsonObject.optString("neutron_cross_sectional", "---")

            if (rMultiplier == "---") {
                elementResistivity.text = "---"
            } else {
                val input = resistivity.toFloat() * rMultiplier.toFloat()
                val output = input.pow(-1).toString()
                elementResistivity.text = output.replace("E", "*10^") + " (S/m)"
            }

            descriptionName.setOnClickListener {
                descriptionName.maxLines = 100
                descriptionName.requestLayout()
                dscBtn.text = "collapse"
            }
            dscBtn.setOnClickListener {
                if (dscBtn.text == "..more") {
                    descriptionName.maxLines = 100
                    descriptionName.requestLayout()
                    dscBtn.text = "collapse"
                } else {
                    descriptionName.maxLines = 4
                    descriptionName.requestLayout()
                    dscBtn.text = "..more"
                }
            }

            //set elements
            elementTitle.text = element
            descriptionName.text = description
            elementName.text = element
            electronsEl.text = sElementElectrons
            elementYear.text = sElementYear
            elementShellsElectrons.text = elementShellElectrons
            elementDiscoveredBy.text = sElementDiscoveredBy
            elementElectrons.text = sElementElectrons
            elementProtons.text = sElementProtons
            elementNeutronsCommon.text = sElementNeutronsCommon
            elementGroup.text = sElementGroup
            elementBoilingKelvin.text = sElementBoilingKelvin
            elementBoilingCelsius.text = sElementBoilingCelsius
            elementBoilingFahrenheit.text = sElementBoilingFahrenheit
            elementElectronegativty.text = elementElectronegativity
            elementMeltingKelvin.text = sElementMeltingKelvin
            elementMeltingCelsius.text = sElementMeltingCelsius
            elementMeltingFahrenheit.text = sElementMeltingFahrenheit
            elementAtomicNumber.text = sElementAtomicNumber
            elementAtomicWeight.text = sElementAtomicWeight
            elementDensity.text = sElementDensity
            elementBlock.text = sElementBlock
            elementAppearance.text = sElementAppearance

            //Nuclear Properties
            radioactiveText.text = isRadioactive
            neutronCrossSectionalText.text = neutronCrossSection
            isotopesFrame.setOnClickListener {
                val isoPreference = ElementSendAndLoad(this)
                isoPreference.setValue(element.lowercase(Locale.getDefault())) //Send element number
                val isoSend = SendIso(this)
                isoSend.setValue("true") //Set flag for sent
                val intent = Intent(this, IsotopesActExperimental::class.java)
                startActivity(intent) //Send intent
            }

            tvPhaseText.text = phaseText
            tvFusionHeatText.text = fusionHeat
            tvSpecificHeatText.text = specificHeatCapacity
            tvVaporizationHeatText.text = vaporizationHeat

            electronConfigText.text = electronConfig
            ionChargeText.text = ionCharge
            ionizationEnergiesText.text = ionizationEnergies
            atomicRadiusText.text = atomicRadius
            atomicRadiusEText.text = atomicRadiusE
            covalentRadiusText.text = covalentRadius
            vanDerWaalsRadiusText.text = vanDerWaalsRadius

            //Shell View items
            configData.text = elementShellElectrons
            eConfigData.text = electronConfig

            //Electromagnetic Properties Items
            elementElectricalType.text = electricalType
            elementMagneticType.text = magneticType
            elementSuperconductingPoint.text = "$superconductingPoint (K)"

            if (phaseText.toString() == "Solid") {
                phaseIcon.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_vector_solid))
            }
            if (phaseText.toString() == "Gas") {
                phaseIcon.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_vector_gas))
            }
            if (phaseText.toString() == "Liquid") {
                phaseIcon.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_liquid))
            }

            if (oxidationNeg1.contains(0.toString())) {
                ox0.text = "0"
                ox0.background.setTint(getColor(R.color.non_metals))
            }
            if (oxidationNeg1.contains(1.toString())) {
                m1ox.text = "-1"
                m1ox.background.setTint(getColor(R.color.noble_gas))
            }
            if (oxidationNeg1.contains(2.toString())) {
                m2ox.text = "-2"
                m2ox.background.setTint(getColor(R.color.noble_gas))
            }
            if (oxidationNeg1.contains(3.toString())) {
                m3ox.text = "-3"
                m3ox.background.setTint(getColor(R.color.noble_gas))
            }
            if (oxidationNeg1.contains(4.toString())) {
                m4ox.text = "-4"
                m4ox.background.setTint(getColor(R.color.noble_gas))
            }
            if (oxidationNeg1.contains(5.toString())) {
                m5ox.text = "-5"
                m5ox.background.setTint(getColor(R.color.noble_gas))
            }

            if (oxidationPos1.contains(1.toString())) {
                p1ox.text = "+1"
                p1ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(2.toString())) {
                p2ox.text = "+2"
                p2ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(3.toString())) {
                p3ox.text = "+3"
                p3ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(4.toString())) {
                p4ox.text = "+4"
                p4ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(5.toString())) {
                p5ox.text = "+5"
                p5ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(6.toString())) {
                p6ox.text = "+6"
                p6ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(7.toString())) {
                p7ox.text = "+7"
                p7ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(8.toString())) {
                p8ox.text = "+8"
                p8ox.background.setTint(getColor(R.color.alkali_metals))
            }
            if (oxidationPos1.contains(9.toString())) {
                p9ox.text = "+9"
                p9ox.background.setTint(getColor(R.color.alkali_metals))
            }

            //set element data for favorite bar
            molarMassF.text = sElementAtomicWeight
            phaseF.text = phaseText
            electronegativityF.text = elementElectronegativity
            densityF.text = sElementDensity

            val degreePref = DegreePref(this)
            val degreePrefValue = degreePref.getValue()

            if (degreePrefValue == 0) {
                boilingF.text = sElementBoilingKelvin
                meltingF.text = sElementMeltingKelvin
            }
            if (degreePrefValue == 1) {
                boilingF.text = sElementBoilingCelsius
                meltingF.text = sElementMeltingCelsius
            }
            if (degreePrefValue == 2) {
                boilingF.text = sElementBoilingFahrenheit
                meltingF.text = sElementMeltingFahrenheit
            }

            if (url == "empty") {
                Utils.fadeInAnim(noImg, 150)
                progressBar.visibility = View.GONE
            } else {
                Utils.fadeInAnim(progressBar, 150)
                noImg.visibility = View.GONE
            }

            fusionHeatF.text = fusionHeat
            specificHeatF.text = specificHeatCapacity
            vaporizationHeatF.text = vaporizationHeat
            aEmpiricalF.text = atomicRadiusE
            aCalculatedF.text = atomicRadius
            covalentF.text = covalentRadius
            vanF.text = vanDerWaalsRadius

            val offlinePreferences = OfflinePreference(this)
            val offlinePrefValue = offlinePreferences.getValue()
            if (offlinePrefValue == 0) {
                loadImage(url)
                loadModelView(elementModelUrl)
                loadSp(short)
            }
            wikiListener(wikipedia)
        } catch (e: IOException) {
            elementTitle.text = "Not able to load json"
            val stringText = "Couldn't load element:"
            val elementSendAndLoadPreference = ElementSendAndLoad(this)
            val elementSendAndLoadValue = elementSendAndLoadPreference.getValue()

            ToastUtil.showToast(this, "$stringText$elementSendAndLoadValue")
        }
    }

    private fun loadImage(url: String?) {
        try {
            Log.d(TAG, "roy93 loadImage url $url")
            Picasso.get().load(url.toString()).into(elementImage)
        } catch (e: ConnectException) {
            offlineDiv.visibility = View.VISIBLE
            frame.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadSp(url: String?) {
        val hUrl = "http://www.jlindemann.se/atomic/emission_lines/"
        val ext = ".gif"
        val fURL = hUrl + url + ext
        try {
            Picasso.get().load(fURL).into(spImg)
            Picasso.get().load(fURL).into(ivSpImgFetail)
        } catch (e: ConnectException) {
            spImg.visibility = View.GONE
            spOffline.text = "No Data"
            spOffline.visibility = View.VISIBLE
        }
    }

    private fun loadModelView(url: String?) {
        Picasso.get().load(url.toString()).into(modelView)
        Picasso.get().load(url.toString()).into(cardModelView)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun wikiListener(url: String?) {
        wikipediaBtn.setOnClickListener {
            val pkgName = "com.android.chrome"
            val customTabBuilder = CustomTabsIntent.Builder()

            customTabBuilder.setToolbarColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorLightPrimary
                )
            )
            customTabBuilder.setSecondaryToolbarColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorLightPrimary
                )
            )
            customTabBuilder.setShowTitle(true)

            val customTab = customTabBuilder.build()
            val intent = customTab.intent
            intent.data = Uri.parse(url)

            val packageManager = packageManager
            val resolveInfoList = packageManager.queryIntentActivities(
                customTab.intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                if (TextUtils.equals(packageName, pkgName))
                    customTab.intent.setPackage(pkgName)
            }
            customTab.intent.data?.let { it1 -> customTab.launchUrl(this, it1) }
        }
    }

    fun favoriteBarSetup() {
        //Favorite Molar
        val molarPreference = FavoriteBarPref(this)
        val molarPrefValue = molarPreference.getValue()
        if (molarPrefValue == 1) {
            molarMassLay.visibility = View.VISIBLE
        }
        if (molarPrefValue == 0) {
            molarMassLay.visibility = View.GONE
        }

        //Favorite Phase
        val phasePreferences = FavoritePhase(this)
        val phasePrefValue = phasePreferences.getValue()
        if (phasePrefValue == 1) {
            phaseLay.visibility = View.VISIBLE
        }
        if (phasePrefValue == 0) {
            phaseLay.visibility = View.GONE
        }

        //Electronegativity Phase
        val electronegativityPreferences = ElectronegativityPref(this)
        val electronegativityPrefValue = electronegativityPreferences.getValue()
        if (electronegativityPrefValue == 1) {
            electronegativityLay.visibility = View.VISIBLE
        }
        if (electronegativityPrefValue == 0) {
            electronegativityLay.visibility = View.GONE
        }

        //Density
        val densityPreference = DensityPref(this)
        val densityPrefValue = densityPreference.getValue()
        if (densityPrefValue == 1) {
            densityLay.visibility = View.VISIBLE
        }
        if (densityPrefValue == 0) {
            densityLay.visibility = View.GONE
        }

        //Boiling
        val boilingPreference = BoilingPref(this)
        val boilingPrefValue = boilingPreference.getValue()
        if (boilingPrefValue == 1) {
            boilingLay.visibility = View.VISIBLE
        }
        if (boilingPrefValue == 0) {
            boilingLay.visibility = View.GONE
        }

        //Melting
        val meltingPref = MeltingPref(this)
        val meltingPrefValue = meltingPref.getValue()
        if (meltingPrefValue == 1) {
            meltingLay.visibility = View.VISIBLE
        }
        if (meltingPrefValue == 0) {
            meltingLay.visibility = View.GONE
        }

        //Empirical
        val empiricalPreference = AtomicRadiusEmpPref(this)
        val empiricalPrefValue = empiricalPreference.getValue()
        if (empiricalPrefValue == 1) {
            aEmpiricalLay.visibility = View.VISIBLE
        }
        if (empiricalPrefValue == 0) {
            aEmpiricalLay.visibility = View.GONE
        }

        //Calculated
        val calculatedPreference = AtomicRadiusCalPref(this)
        val calculatedPrefValue = calculatedPreference.getValue()
        if (calculatedPrefValue == 1) {
            aCalculatedLay.visibility = View.VISIBLE
        }
        if (calculatedPrefValue == 0) {
            aCalculatedLay.visibility = View.GONE
        }

        //Covalent
        val covalentPreference = AtomicCovalentPref(this)
        val covalentPrefValue = covalentPreference.getValue()
        if (covalentPrefValue == 1) {
            covalentLay.visibility = View.VISIBLE
        }
        if (covalentPrefValue == 0) {
            covalentLay.visibility = View.GONE
        }

        //Van Der Waals
        val vanPreference = AtomicVanPref(this)
        val vanPrefValue = vanPreference.getValue()
        if (vanPrefValue == 1) {
            vanLay.visibility = View.VISIBLE
        }
        if (vanPrefValue == 0) {
            vanLay.visibility = View.GONE
        }

        //Fusion Heat
        val fusionHeatPref = FusionHeatPref(this)
        val fusionHeatValue = fusionHeatPref.getValue()
        if (fusionHeatValue == 1) {
            fusionHeatLay.visibility = View.VISIBLE
        }
        if (fusionHeatValue == 0) {
            fusionHeatLay.visibility = View.GONE
        }

        //Specific Heat
        val specificHeatPref = SpecificHeatPref(this)
        val specificHeatValue = specificHeatPref.getValue()
        if (specificHeatValue == 1) {
            specificHeatLay.visibility = View.VISIBLE
        }
        if (specificHeatValue == 0) {
            specificHeatLay.visibility = View.GONE
        }

        //Vaporization Heat
        val vaporizationHeatPref = VaporizationHeatPref(this)
        val vaporizationHeatValue = vaporizationHeatPref.getValue()
        if (vaporizationHeatValue == 1) {
            vaporizationHeatLay.visibility = View.VISIBLE
        }
        if (vaporizationHeatValue == 0) {
            vaporizationHeatLay.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            enableAdaptiveRefreshRate()
        }
    }

    private fun enableAdaptiveRefreshRate() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val display: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display // Sử dụng API mới
        } else {
            @Suppress("DEPRECATION")
            wm.defaultDisplay // Fallback cho API thấp hơn
        }


        if (display != null) {
            val supportedModes = display.supportedModes
            val highestRefreshRateMode = supportedModes.maxByOrNull { it.refreshRate }

            if (highestRefreshRateMode != null) {
                window.attributes = window.attributes.apply {
                    preferredDisplayModeId = highestRefreshRateMode.modeId
                }
                println("Adaptive refresh rate applied: ${highestRefreshRateMode.refreshRate} Hz")
            }
        }
    }
}
