package com.mckimquyen.atomicPeriodicTable.extensions

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mckimquyen.atomicPeriodicTable.R
import com.mckimquyen.atomicPeriodicTable.activities.IsotopesActivityExperimental
import com.mckimquyen.atomicPeriodicTable.pref.AtomicCovalentPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusCalPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicRadiusEmpPreference
import com.mckimquyen.atomicPeriodicTable.pref.AtomicVanPreference
import com.mckimquyen.atomicPeriodicTable.pref.BoilingPreference
import com.mckimquyen.atomicPeriodicTable.pref.DegreePreference
import com.mckimquyen.atomicPeriodicTable.pref.DensityPreference
import com.mckimquyen.atomicPeriodicTable.pref.ElectronegativityPreference
import com.mckimquyen.atomicPeriodicTable.pref.ElementSendAndLoad
import com.mckimquyen.atomicPeriodicTable.pref.FavoriteBarPreferences
import com.mckimquyen.atomicPeriodicTable.pref.FavoritePhase
import com.mckimquyen.atomicPeriodicTable.pref.FusionHeatPreference
import com.mckimquyen.atomicPeriodicTable.pref.MeltingPreference
import com.mckimquyen.atomicPeriodicTable.pref.OfflinePreference
import com.mckimquyen.atomicPeriodicTable.pref.SpecificHeatPreference
import com.mckimquyen.atomicPeriodicTable.pref.VaporizationHeatPreference
import com.mckimquyen.atomicPeriodicTable.pref.sendIso
import com.mckimquyen.atomicPeriodicTable.util.Pasteur
import com.mckimquyen.atomicPeriodicTable.util.ToastUtil
import com.mckimquyen.atomicPeriodicTable.util.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.a_element_info.*
import kotlinx.android.synthetic.main.v_d_atomic.*
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
import kotlinx.android.synthetic.main.v_d_properties.*
import kotlinx.android.synthetic.main.v_d_temperatures.*
import kotlinx.android.synthetic.main.v_d_thermodynamic.*
import kotlinx.android.synthetic.main.view_detail_emission.ivSpImgFetail
import kotlinx.android.synthetic.main.view_favorite_bar.*
import kotlinx.android.synthetic.main.view_loading_view.noImg
import kotlinx.android.synthetic.main.view_loading_view.progressBar
import kotlinx.android.synthetic.main.view_oxidiation_states.*
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

abstract class InfoExtension : AppCompatActivity(), View.OnApplyWindowInsetsListener {
    companion object {
        private const val TAG = "BaseActivity"
    }

    private var systemUiConfigured = false

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
        right: Int
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
                val isoSend = sendIso(this)
                isoSend.setValue("true") //Set flag for sent
                val intent = Intent(this, IsotopesActivityExperimental::class.java)
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
                phaseIcon.setImageDrawable(getDrawable(R.drawable.ic_vector_solid))
            }
            if (phaseText.toString() == "Gas") {
                phaseIcon.setImageDrawable(getDrawable(R.drawable.ic_vector_gas))
            }
            if (phaseText.toString() == "Liquid") {
                phaseIcon.setImageDrawable(getDrawable(R.drawable.ic_liquid))
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

            val degreePreference = DegreePreference(this)
            val degreePrefValue = degreePreference.getValue()

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
        val molarPreference = FavoriteBarPreferences(this)
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
        val electronegativityPreferences = ElectronegativityPreference(this)
        val electronegativityPrefValue = electronegativityPreferences.getValue()
        if (electronegativityPrefValue == 1) {
            electronegativityLay.visibility = View.VISIBLE
        }
        if (electronegativityPrefValue == 0) {
            electronegativityLay.visibility = View.GONE
        }

        //Density
        val densityPreference = DensityPreference(this)
        val densityPrefValue = densityPreference.getValue()
        if (densityPrefValue == 1) {
            densityLay.visibility = View.VISIBLE
        }
        if (densityPrefValue == 0) {
            densityLay.visibility = View.GONE
        }

        //Boiling
        val boilingPreference = BoilingPreference(this)
        val boilingPrefValue = boilingPreference.getValue()
        if (boilingPrefValue == 1) {
            boilingLay.visibility = View.VISIBLE
        }
        if (boilingPrefValue == 0) {
            boilingLay.visibility = View.GONE
        }

        //Melting
        val meltingPreference = MeltingPreference(this)
        val meltingPrefValue = meltingPreference.getValue()
        if (meltingPrefValue == 1) {
            meltingLay.visibility = View.VISIBLE
        }
        if (meltingPrefValue == 0) {
            meltingLay.visibility = View.GONE
        }

        //Empirical
        val empiricalPreference = AtomicRadiusEmpPreference(this)
        val empiricalPrefValue = empiricalPreference.getValue()
        if (empiricalPrefValue == 1) {
            aEmpiricalLay.visibility = View.VISIBLE
        }
        if (empiricalPrefValue == 0) {
            aEmpiricalLay.visibility = View.GONE
        }

        //Calculated
        val calculatedPreference = AtomicRadiusCalPreference(this)
        val calculatedPrefValue = calculatedPreference.getValue()
        if (calculatedPrefValue == 1) {
            aCalculatedLay.visibility = View.VISIBLE
        }
        if (calculatedPrefValue == 0) {
            aCalculatedLay.visibility = View.GONE
        }

        //Covalent
        val covalentPreference = AtomicCovalentPreference(this)
        val covalentPrefValue = covalentPreference.getValue()
        if (covalentPrefValue == 1) {
            covalentLay.visibility = View.VISIBLE
        }
        if (covalentPrefValue == 0) {
            covalentLay.visibility = View.GONE
        }

        //Van Der Waals
        val vanPreference = AtomicVanPreference(this)
        val vanPrefValue = vanPreference.getValue()
        if (vanPrefValue == 1) {
            vanLay.visibility = View.VISIBLE
        }
        if (vanPrefValue == 0) {
            vanLay.visibility = View.GONE
        }

        //Fusion Heat
        val fusionHeatPreference = FusionHeatPreference(this)
        val fusionHeatValue = fusionHeatPreference.getValue()
        if (fusionHeatValue == 1) {
            fusionHeatLay.visibility = View.VISIBLE
        }
        if (fusionHeatValue == 0) {
            fusionHeatLay.visibility = View.GONE
        }

        //Specific Heat
        val specificHeatPreference = SpecificHeatPreference(this)
        val specificHeatValue = specificHeatPreference.getValue()
        if (specificHeatValue == 1) {
            specificHeatLay.visibility = View.VISIBLE
        }
        if (specificHeatValue == 0) {
            specificHeatLay.visibility = View.GONE
        }

        //Vaporization Heat
        val vaporizationHeatPreference = VaporizationHeatPreference(this)
        val vaporizationHeatValue = vaporizationHeatPreference.getValue()
        if (vaporizationHeatValue == 1) {
            vaporizationHeatLay.visibility = View.VISIBLE
        }
        if (vaporizationHeatValue == 0) {
            vaporizationHeatLay.visibility = View.GONE
        }
    }
}
