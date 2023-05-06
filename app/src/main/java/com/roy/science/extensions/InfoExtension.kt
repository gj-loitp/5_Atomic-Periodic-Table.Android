package com.roy.science.extensions

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.roy.science.R
import com.roy.science.activities.IsotopesActivityExperimental
import com.roy.science.preferences.AtomicCovalentPreference
import com.roy.science.preferences.AtomicRadiusCalPreference
import com.roy.science.preferences.AtomicRadiusEmpPreference
import com.roy.science.preferences.AtomicVanPreference
import com.roy.science.preferences.BoilingPreference
import com.roy.science.preferences.DegreePreference
import com.roy.science.preferences.DensityPreference
import com.roy.science.preferences.ElectronegativityPreference
import com.roy.science.preferences.ElementSendAndLoad
import com.roy.science.preferences.FavoriteBarPreferences
import com.roy.science.preferences.FavoritePhase
import com.roy.science.preferences.FusionHeatPreference
import com.roy.science.preferences.MeltingPreference
import com.roy.science.preferences.SpecificHeatPreference
import com.roy.science.preferences.VaporizationHeatPreference
import com.roy.science.preferences.offlinePreference
import com.roy.science.preferences.sendIso
import com.roy.science.utils.Pasteur
import com.roy.science.utils.ToastUtil
import com.roy.science.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_element_info.elementTitle
import kotlinx.android.synthetic.main.activity_element_info.element_image
import kotlinx.android.synthetic.main.activity_element_info.frame
import kotlinx.android.synthetic.main.activity_element_info.nextBtn
import kotlinx.android.synthetic.main.activity_element_info.offline_div
import kotlinx.android.synthetic.main.activity_element_info.previousBtn
import kotlinx.android.synthetic.main.activity_element_info.wikipedia_btn
import kotlinx.android.synthetic.main.d_atomic.atomic_radius_e_text
import kotlinx.android.synthetic.main.d_atomic.atomic_radius_text
import kotlinx.android.synthetic.main.d_atomic.covalent_radius_text
import kotlinx.android.synthetic.main.d_atomic.electron_config_text
import kotlinx.android.synthetic.main.d_atomic.ion_charge_text
import kotlinx.android.synthetic.main.d_atomic.ionization_energies_text
import kotlinx.android.synthetic.main.d_atomic.oxView
import kotlinx.android.synthetic.main.d_atomic.van_der_waals_radius_text
import kotlinx.android.synthetic.main.d_electromagnetic.element_electrical_type
import kotlinx.android.synthetic.main.d_electromagnetic.element_magnetic_type
import kotlinx.android.synthetic.main.d_electromagnetic.elementResistivity
import kotlinx.android.synthetic.main.d_electromagnetic.element_superconducting_point
import kotlinx.android.synthetic.main.d_nuclear.isotopesFrame
import kotlinx.android.synthetic.main.d_nuclear.neutronCrossSectionalText
import kotlinx.android.synthetic.main.d_nuclear.radioactiveText
import kotlinx.android.synthetic.main.d_overview.descriptionName
import kotlinx.android.synthetic.main.d_overview.dscBtn
import kotlinx.android.synthetic.main.d_overview.electronsEl
import kotlinx.android.synthetic.main.d_overview.element_appearance
import kotlinx.android.synthetic.main.d_overview.element_discovered_by
import kotlinx.android.synthetic.main.d_overview.element_electrons
import kotlinx.android.synthetic.main.d_overview.element_group
import kotlinx.android.synthetic.main.d_overview.elementName
import kotlinx.android.synthetic.main.d_overview.element_neutrons_common
import kotlinx.android.synthetic.main.d_overview.element_protons
import kotlinx.android.synthetic.main.d_overview.element_year
import kotlinx.android.synthetic.main.d_properties.element_atomic_number
import kotlinx.android.synthetic.main.d_properties.element_atomic_weight
import kotlinx.android.synthetic.main.d_properties.element_block
import kotlinx.android.synthetic.main.d_properties.element_density
import kotlinx.android.synthetic.main.d_properties.element_electronegativty
import kotlinx.android.synthetic.main.d_properties.elementShellsElectrons
import kotlinx.android.synthetic.main.d_properties.model_view
import kotlinx.android.synthetic.main.d_properties.spImg
import kotlinx.android.synthetic.main.d_properties.spOffline
import kotlinx.android.synthetic.main.d_temperatures.element_boiling_celsius
import kotlinx.android.synthetic.main.d_temperatures.element_boiling_fahrenheit
import kotlinx.android.synthetic.main.d_temperatures.element_boiling_kelvin
import kotlinx.android.synthetic.main.d_temperatures.element_melting_celsius
import kotlinx.android.synthetic.main.d_temperatures.element_melting_fahrenheit
import kotlinx.android.synthetic.main.d_temperatures.element_melting_kelvin
import kotlinx.android.synthetic.main.d_thermodynamic.fusion_heat_text
import kotlinx.android.synthetic.main.d_thermodynamic.phase_icon
import kotlinx.android.synthetic.main.d_thermodynamic.phase_text
import kotlinx.android.synthetic.main.d_thermodynamic.specific_heat_text
import kotlinx.android.synthetic.main.d_thermodynamic.vaporization_heat_text
import kotlinx.android.synthetic.main.detail_emission.sp_img_detail
import kotlinx.android.synthetic.main.favorite_bar.a_calculated_f
import kotlinx.android.synthetic.main.favorite_bar.a_calculated_lay
import kotlinx.android.synthetic.main.favorite_bar.a_empirical_f
import kotlinx.android.synthetic.main.favorite_bar.a_empirical_lay
import kotlinx.android.synthetic.main.favorite_bar.boiling_f
import kotlinx.android.synthetic.main.favorite_bar.boiling_lay
import kotlinx.android.synthetic.main.favorite_bar.covalent_f
import kotlinx.android.synthetic.main.favorite_bar.covalent_lay
import kotlinx.android.synthetic.main.favorite_bar.density_f
import kotlinx.android.synthetic.main.favorite_bar.density_lay
import kotlinx.android.synthetic.main.favorite_bar.electronegativity_f
import kotlinx.android.synthetic.main.favorite_bar.electronegativity_lay
import kotlinx.android.synthetic.main.favorite_bar.fusion_heat_f
import kotlinx.android.synthetic.main.favorite_bar.fusion_heat_lay
import kotlinx.android.synthetic.main.favorite_bar.melting_f
import kotlinx.android.synthetic.main.favorite_bar.melting_lay
import kotlinx.android.synthetic.main.favorite_bar.molar_mass_f
import kotlinx.android.synthetic.main.favorite_bar.molar_mass_lay
import kotlinx.android.synthetic.main.favorite_bar.phase_f
import kotlinx.android.synthetic.main.favorite_bar.phase_lay
import kotlinx.android.synthetic.main.favorite_bar.specific_heat_f
import kotlinx.android.synthetic.main.favorite_bar.specific_heat_lay
import kotlinx.android.synthetic.main.favorite_bar.van_f
import kotlinx.android.synthetic.main.favorite_bar.van_lay
import kotlinx.android.synthetic.main.favorite_bar.vaporization_heat_f
import kotlinx.android.synthetic.main.favorite_bar.vaporization_heat_lay
import kotlinx.android.synthetic.main.loading_view.no_img
import kotlinx.android.synthetic.main.loading_view.pro_bar
import kotlinx.android.synthetic.main.oxidiation_states.m1ox
import kotlinx.android.synthetic.main.oxidiation_states.m2ox
import kotlinx.android.synthetic.main.oxidiation_states.m3ox
import kotlinx.android.synthetic.main.oxidiation_states.m4ox
import kotlinx.android.synthetic.main.oxidiation_states.m5ox
import kotlinx.android.synthetic.main.oxidiation_states.ox0
import kotlinx.android.synthetic.main.oxidiation_states.p1ox
import kotlinx.android.synthetic.main.oxidiation_states.p2ox
import kotlinx.android.synthetic.main.oxidiation_states.p3ox
import kotlinx.android.synthetic.main.oxidiation_states.p4ox
import kotlinx.android.synthetic.main.oxidiation_states.p5ox
import kotlinx.android.synthetic.main.oxidiation_states.p6ox
import kotlinx.android.synthetic.main.oxidiation_states.p7ox
import kotlinx.android.synthetic.main.oxidiation_states.p8ox
import kotlinx.android.synthetic.main.oxidiation_states.p9ox
import kotlinx.android.synthetic.main.shell_view.card_model_view
import kotlinx.android.synthetic.main.shell_view.config_data
import kotlinx.android.synthetic.main.shell_view.e_config_data
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
            val element= jsonObject.optString("element", "---")
            val description = jsonObject.optString("description", "---")
            val url = jsonObject.optString("link", "---")
            val short = jsonObject.optString("short", "---")
            val elementElectrons = jsonObject.optString("element_electrons", "---")
            val elementShellElectrons = jsonObject.optString("element_shells_electrons", "---")
            val elementYear = jsonObject.optString("element_year", "---")
            val elementDiscoveredBy = jsonObject.optString("element_discovered_name", "---")
            val elementProtons = jsonObject.optString("element_protons", "---")
            val elementNeutronsCommon = jsonObject.optString("element_neutron_common", "---")
            val elementGroup = jsonObject.optString("element_group", "---")
            val elementElectronegativity = jsonObject.optString("element_electronegativty", "---")
            val wikipedia = jsonObject.optString("wikilink", "---")
            val elementBoilingKelvin = jsonObject.optString("element_boiling_kelvin", "---")
            val elementBoilingCelsius = jsonObject.optString("element_boiling_celsius", "---")
            val elementBoilingFahrenheit = jsonObject.optString("element_boiling_fahrenheit", "---")
            val elementMeltingKelvin = jsonObject.optString("element_melting_kelvin", "---")
            val elementMeltingCelsius = jsonObject.optString("element_melting_celsius", "---")
            val elementMeltingFahrenheit = jsonObject.optString("element_melting_fahrenheit", "---")
            val elementAtomicNumber = jsonObject.optString("element_atomic_number", "---")
            val elementAtomicWeight = jsonObject.optString("element_atomicmass", "---")
            val elementDensity = jsonObject.optString("element_density", "---")
            val elementModelUrl = jsonObject.optString("element_model", "---")
            val elementAppearance = jsonObject.optString("element_appearance", "---")
            val elementBlock = jsonObject.optString("element_block", "---")
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
            electronsEl.text = elementElectrons
            element_year.text = elementYear
            elementShellsElectrons.text = elementShellElectrons
            element_discovered_by.text = elementDiscoveredBy
            element_electrons.text = elementElectrons
            element_protons.text = elementProtons
            element_neutrons_common.text = elementNeutronsCommon
            element_group.text = elementGroup
            element_boiling_kelvin.text = elementBoilingKelvin
            element_boiling_celsius.text = elementBoilingCelsius
            element_boiling_fahrenheit.text = elementBoilingFahrenheit
            element_electronegativty.text = elementElectronegativity
            element_melting_kelvin.text = elementMeltingKelvin
            element_melting_celsius.text = elementMeltingCelsius
            element_melting_fahrenheit.text = elementMeltingFahrenheit
            element_atomic_number.text = elementAtomicNumber
            element_atomic_weight.text = elementAtomicWeight
            element_density.text = elementDensity
            element_block.text = elementBlock
            element_appearance.text = elementAppearance

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

            phase_text.text = phaseText
            fusion_heat_text.text = fusionHeat
            specific_heat_text.text = specificHeatCapacity
            vaporization_heat_text.text = vaporizationHeat

            electron_config_text.text = electronConfig
            ion_charge_text.text = ionCharge
            ionization_energies_text.text = ionizationEnergies
            atomic_radius_text.text = atomicRadius
            atomic_radius_e_text.text = atomicRadiusE
            covalent_radius_text.text = covalentRadius
            van_der_waals_radius_text.text = vanDerWaalsRadius

            //Shell View items
            config_data.text = elementShellElectrons
            e_config_data.text = electronConfig

            //Electromagnetic Properties Items
            element_electrical_type.text = electricalType
            element_magnetic_type.text = magneticType
            element_superconducting_point.text = superconductingPoint + " (K)"

            if (phaseText.toString() == "Solid") {
                phase_icon.setImageDrawable(getDrawable(R.drawable.solid))
            }
            if (phaseText.toString() == "Gas") {
                phase_icon.setImageDrawable(getDrawable(R.drawable.gas))
            }
            if (phaseText.toString() == "Liquid") {
                phase_icon.setImageDrawable(getDrawable(R.drawable.liquid))
            }

            if (oxidationNeg1.contains(0.toString())) { ox0.text = "0"
                ox0.background.setTint(getColor(R.color.non_metals)) }
            if (oxidationNeg1.contains(1.toString())) { m1ox.text = "-1"
                m1ox.background.setTint(getColor(R.color.noble_gas)) }
            if (oxidationNeg1.contains(2.toString())) { m2ox.text = "-2"
                m2ox.background.setTint(getColor(R.color.noble_gas)) }
            if (oxidationNeg1.contains(3.toString())) { m3ox.text = "-3"
                m3ox.background.setTint(getColor(R.color.noble_gas)) }
            if (oxidationNeg1.contains(4.toString())) { m4ox.text = "-4"
                m4ox.background.setTint(getColor(R.color.noble_gas)) }
            if (oxidationNeg1.contains(5.toString())) { m5ox.text = "-5"
                m5ox.background.setTint(getColor(R.color.noble_gas)) }


            if (oxidationPos1.contains(1.toString())) { p1ox.text = "+1"
                p1ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(2.toString())) { p2ox.text = "+2"
                p2ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(3.toString())) { p3ox.text = "+3"
                p3ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(4.toString())) { p4ox.text = "+4"
                p4ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(5.toString())) { p5ox.text = "+5"
                p5ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(6.toString())) { p6ox.text = "+6"
                p6ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(7.toString())) { p7ox.text = "+7"
                p7ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(8.toString())) { p8ox.text = "+8"
                p8ox.background.setTint(getColor(R.color.alkali_metals)) }
            if (oxidationPos1.contains(9.toString())) { p9ox.text = "+9"
                p9ox.background.setTint(getColor(R.color.alkali_metals)) }

            //set element data for favorite bar
            molar_mass_f.text = elementAtomicWeight
            phase_f.text = phaseText
            electronegativity_f.text = elementElectronegativity
            density_f.text = elementDensity

            val degreePreference = DegreePreference(this)
            val degreePrefValue = degreePreference.getValue()

            if (degreePrefValue == 0) {
                boiling_f.text = elementBoilingKelvin
                melting_f.text = elementMeltingKelvin
            }
            if (degreePrefValue == 1) {
                boiling_f.text = elementBoilingCelsius
                melting_f.text = elementMeltingCelsius
            }
            if (degreePrefValue == 2) {
                boiling_f.text = elementBoilingFahrenheit
                melting_f.text = elementMeltingFahrenheit
            }

            if (url == "empty") {
                Utils.fadeInAnim(no_img, 150)
                pro_bar.visibility = View.GONE
            } else {
                Utils.fadeInAnim(pro_bar, 150)
                no_img.visibility = View.GONE
            }

            fusion_heat_f.text = fusionHeat
            specific_heat_f.text = specificHeatCapacity
            vaporization_heat_f.text = vaporizationHeat
            a_empirical_f.text = atomicRadiusE
            a_calculated_f.text = atomicRadius
            covalent_f.text = covalentRadius
            van_f.text = vanDerWaalsRadius

            val offlinePreferences = offlinePreference(this)
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
            val ElementSendAndLoadPreference = ElementSendAndLoad(this)
            val ElementSendAndLoadValue = ElementSendAndLoadPreference.getValue()
            val name = ElementSendAndLoadValue

            ToastUtil.showToast(this, "$stringText$name")
        }
    }

    private fun loadImage(url: String?) {
        try { Picasso.get().load(url.toString()).into(element_image) }
        catch(e: ConnectException) {
            offline_div.visibility = View.VISIBLE
            frame.visibility = View.GONE
        }
    }

    private fun loadSp(url: String?) {
        val hUrl = "http://www.jlindemann.se/atomic/emission_lines/"
        val ext = ".gif"
        val fURL = hUrl + url + ext
        try {
            Picasso.get().load(fURL).into(spImg)
            Picasso.get().load(fURL).into(sp_img_detail)
        }

        catch(e: ConnectException) {
            spImg.visibility = View.GONE
            spOffline.text = "No Data"
            spOffline.visibility = View.VISIBLE
        }
    }

    private fun loadModelView(url: String?) {
        Picasso.get().load(url.toString()).into(model_view)
        Picasso.get().load(url.toString()).into(card_model_view)
    }

    fun wikiListener(url: String?) {
        wikipedia_btn.setOnClickListener {
            val PACKAGE_NAME = "com.android.chrome"
            val customTabBuilder = CustomTabsIntent.Builder()

            customTabBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorLightPrimary))
            customTabBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorLightPrimary))
            customTabBuilder.setShowTitle(true)

            val CustomTab = customTabBuilder.build()
            val intent = CustomTab.intent
            intent.data = Uri.parse(url)

            val packageManager = packageManager
            val resolveInfoList = packageManager.queryIntentActivities(CustomTab.intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                if (TextUtils.equals(packageName, PACKAGE_NAME))
                    CustomTab.intent.setPackage(PACKAGE_NAME)
            }
            CustomTab.intent.data?.let { it1 -> CustomTab.launchUrl(this, it1) }
        }
    }

    fun favoriteBarSetup() {
        //Favorite Molar
        val molarPreference = FavoriteBarPreferences(this)
        var molarPrefValue = molarPreference.getValue()
        if (molarPrefValue == 1) {
            molar_mass_lay.visibility = View.VISIBLE
        }
        if (molarPrefValue == 0) {
            molar_mass_lay.visibility = View.GONE
        }

        //Favorite Phase
        val phasePreferences = FavoritePhase(this)
        var phasePrefValue = phasePreferences.getValue()
        if (phasePrefValue == 1) {
            phase_lay.visibility = View.VISIBLE
        }
        if (phasePrefValue == 0) {
            phase_lay.visibility = View.GONE
        }

        //Electronegativity Phase
        val electronegativityPreferences = ElectronegativityPreference(this)
        var electronegativityPrefValue = electronegativityPreferences.getValue()
        if (electronegativityPrefValue == 1) {
            electronegativity_lay.visibility = View.VISIBLE
        }
        if (electronegativityPrefValue == 0) {
            electronegativity_lay.visibility = View.GONE
        }

        //Density
        val densityPreference = DensityPreference(this)
        var densityPrefValue = densityPreference.getValue()
        if (densityPrefValue == 1) {
            density_lay.visibility = View.VISIBLE
        }
        if (densityPrefValue == 0) {
            density_lay.visibility = View.GONE
        }

        //Boiling
        val boilingPreference = BoilingPreference(this)
        var boilingPrefValue = boilingPreference.getValue()
        if (boilingPrefValue == 1) {
            boiling_lay.visibility = View.VISIBLE
        }
        if (boilingPrefValue == 0) {
            boiling_lay.visibility = View.GONE
        }

        //Melting
        val meltingPreference = MeltingPreference(this)
        val meltingPrefValue = meltingPreference.getValue()
        if (meltingPrefValue == 1) { melting_lay.visibility = View.VISIBLE }
        if (meltingPrefValue == 0) { melting_lay.visibility = View.GONE }

        //Empirical
        val empiricalPreference = AtomicRadiusEmpPreference(this)
        val empiricalPrefValue = empiricalPreference.getValue()
        if (empiricalPrefValue == 1) { a_empirical_lay.visibility = View.VISIBLE }
        if (empiricalPrefValue == 0) { a_empirical_lay.visibility = View.GONE }

        //Calculated
        val calculatedPreference = AtomicRadiusCalPreference(this)
        val calculatedPrefValue = calculatedPreference.getValue()
        if (calculatedPrefValue == 1) { a_calculated_lay.visibility = View.VISIBLE }
        if (calculatedPrefValue == 0) { a_calculated_lay.visibility = View.GONE }

        //Covalent
        val covalentPreference = AtomicCovalentPreference(this)
        val covalentPrefValue = covalentPreference.getValue()
        if (covalentPrefValue == 1) { covalent_lay.visibility = View.VISIBLE }
        if (covalentPrefValue == 0) { covalent_lay.visibility = View.GONE }

        //Van Der Waals
        val vanPreference = AtomicVanPreference(this)
        val vanPrefValue = vanPreference.getValue()
        if (vanPrefValue == 1) { van_lay.visibility = View.VISIBLE }
        if (vanPrefValue == 0) { van_lay.visibility = View.GONE }

        //Fusion Heat
        val fusionHeatPreference = FusionHeatPreference(this)
        var fusionHeatValue = fusionHeatPreference.getValue()
        if (fusionHeatValue == 1) {
            fusion_heat_lay.visibility = View.VISIBLE
        }
        if (fusionHeatValue == 0) {
            fusion_heat_lay.visibility = View.GONE
        }

        //Specific Heat
        val specificHeatPreference = SpecificHeatPreference(this)
        var specificHeatValue = specificHeatPreference.getValue()
        if (specificHeatValue == 1) {
            specific_heat_lay.visibility = View.VISIBLE
        }
        if (specificHeatValue == 0) {
            specific_heat_lay.visibility = View.GONE
        }

        //Vaporization Heat
        val vaporizationHeatPreference = VaporizationHeatPreference(this)
        var vaporizationHeatValue = vaporizationHeatPreference.getValue()
        if (vaporizationHeatValue == 1) {
            vaporization_heat_lay.visibility = View.VISIBLE
        }
        if (vaporizationHeatValue == 0) {
            vaporization_heat_lay.visibility = View.GONE
        }
    }
}
