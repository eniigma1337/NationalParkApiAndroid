package com.example.advancedandroidproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.advancedandroidproject.models.ParkAsArg
import com.example.advancedandroidproject.Network.NationalParkServiceApi
import com.example.advancedandroidproject.Network.NetworkModule
import com.example.advancedandroidproject.Network.Park
import com.example.advancedandroidproject.Network.ParksResponse
import com.example.advancedandroidproject.databinding.FragmentFindParkBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
//import com.google.type.LatLng
import kotlinx.coroutines.launch
import retrofit2.Response
import com.google.android.gms.maps.model.LatLng


class FindParkFragment : Fragment(R.layout.fragment_find_park), OnMapReadyCallback {

    lateinit var mMap : GoogleMap
    lateinit var mMap2 : GoogleMap

    lateinit var mapFragment: SupportMapFragment
    private var TAG = "Find Park"
    private var selectedPark: MutableList<ParkAsArg> = mutableListOf()
    lateinit var selectedState : String
    private lateinit var binding : FragmentFindParkBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var myarrayAdapter: ArrayAdapter<String>
    val parksList:MutableList<String> = mutableListOf("", "", "")
    var responseFromAPI : ParksResponse? = null
    lateinit var data : List<Park>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindParkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Below is adapter for listview

        this.arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout. simple_list_item_1, parksList)
        binding.listView.adapter = arrayAdapter

        binding.listView.setOnItemClickListener { adapterView, view, i, l ->
            val action = FindParkFragmentDirections.actionFindParkFragmentToParkDetailsFragment(selectedPark[i])
            findNavController().navigate(action)
        }



        //For Map

        mapFragment = childFragmentManager.findFragmentById(binding.container2.id) as SupportMapFragment
        mapFragment?.getMapAsync(this)




        //For Spinner

        var stateName = arrayOf("AL", "AK", "AZ","AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
            "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY")

        //Below is adapter for spinner

        this.myarrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stateName)
        binding.spinner.adapter = myarrayAdapter


        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {



                //For getting state selected

                selectedState = p0?.getItemAtPosition(p2).toString()



            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        //for button click

        binding.button.setOnClickListener {

            mMap.clear()

            lifecycleScope.launch {
//                val responseFromAPI = fetchData()
//                val responseFromAPI = fetchData()
                responseFromAPI = fetchData()
                if (responseFromAPI == null) {
                    return@launch
                }
                Log.d(TAG, "Success: Data retrieved from API")
//                val data = responseFromAPI!!.data
                data = responseFromAPI!!.data
                parksList.clear()

                for ((index, park) in data!!.withIndex()) {
                    val title = park.fullName
                    parksList.add(title)

                    val x = ParkAsArg(index.toString(),park.fullName,"",park.url,park.images[0].url,park.description)
                    selectedPark.add(x)
                    selectedPark[index].fullName = park.fullName
                    selectedPark[index].description = park.description
                    selectedPark[index].url = park.url
                    selectedPark[index].address = "${park.addresses[0].line1}, ${park.addresses[0].city}, ${park.addresses[0].stateCode}, ${park.addresses[0].postalCode} "
                    selectedPark[index].image = park.images[0].url

                    var lat = park.latitude.toDouble()
                    var long = park.longitude.toDouble()
                    mMap.addMarker((MarkerOptions().position(LatLng(lat, long)).title(park.fullName)))


                }

                arrayAdapter.notifyDataSetChanged()
            }



            binding.tv1.text = buildString {
                append("selected state is : ")
                append(selectedState)
            }

        }

    }



    private suspend fun fetchData(): ParksResponse? {
        var apiService: NationalParkServiceApi = NetworkModule.nationalParkServiceApi

        //make api call
        val response: Response<ParksResponse> = apiService.getParks("njD9ILBBCno8T4tubJ3p5IILGs10BOveRmbc3UWd",selectedState)

        if (response.isSuccessful) {
            val dataFromAPI = response.body()   /// myresponseobject
            if (dataFromAPI == null) {
                Log.d("API", "No data from API or some other error")
                return null
            }

            // if you reach this point, then you must have received a response from the api
            Log.d(TAG, "Here is the data from the API")
            Log.d(TAG, dataFromAPI.toString())
            return dataFromAPI
        }
        else {
            // Handle error
            Log.d(TAG, "An error occurred")
            return null
        }
    }




    override fun onMapReady(googleMap: GoogleMap) {

        Log.d("Tag", "+++ Map callback is executing...")
        // initialize the map
        this.mMap = googleMap

        // configure the map's options
        // - set the map type (hybrid, satellite, etc)
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        // - select if traffic data should be displayed
        mMap.isTrafficEnabled = true
        // - add user interface controls to the map (zoom, compass, etc)
        val uiSettings = googleMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        uiSettings.isCompassEnabled = true

    }

}