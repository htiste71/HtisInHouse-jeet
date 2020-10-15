package com.htistelecom.htisinhouse.activity.WFMS.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.models.MyTeamModel
import kotlinx.android.synthetic.main.toolbar.*


class TeamMapActivityWFMS : AppCompatActivity(), OnMapReadyCallback {
    lateinit var bitmap: Bitmap
    lateinit var mapFragment: SupportMapFragment
    private var myTeamList = ArrayList<MyTeamModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_map_wfms)
        initViews()

    }

    private fun initViews() {
        ivDrawer.visibility = View.GONE

        tv_title.text = "Team"
        ivBack.setOnClickListener { view -> backToHome() }
        myTeamList = intent.getSerializableExtra("Data") as ArrayList<MyTeamModel>
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(maps: GoogleMap) {
        val mMap = maps


        for (i in 0 until myTeamList.size) {
            val model = myTeamList.get(i)
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            if (!model.latitude.equals("")) {
                latitude = model.latitude.toDouble()
            }
            if (!model.longitude.equals("")) {
                longitude = model.longitude.toDouble()
            }

            var mCheckIn = ""
            var mCheckOut = ""
            if (model.checkInLocation.equals("")) {
                mCheckIn = "Check In: NA"
            } else {
                mCheckIn = "Check In:" + model.checkInLocation
            }
            if (model.checkOutLocation.equals("")) {
                mCheckOut = "Check Out: NA"
            } else {
                mCheckOut = "Check Out:" + model.checkOutLocation
            }
            if (latitude != 0.0 || longitude != 0.0) {
                val marker = MarkerOptions().position(LatLng(latitude, longitude)).title(model.empName + "\n" + mCheckIn + "\n" + mCheckOut)
                maps.addMarker(marker)

                maps.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
                maps.animateCamera(CameraUpdateFactory.zoomTo(17f))


            }


            maps.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoWindow(arg0: Marker): View? {
                    return null
                }

                override fun getInfoContents(marker: Marker): View {
                    val info = LinearLayout(this@TeamMapActivityWFMS)
                    info.orientation = LinearLayout.VERTICAL
                    val title = TextView(this@TeamMapActivityWFMS)
                    title.setTextColor(Color.BLACK)
                    title.text = marker.title

                    info.addView(title)
                    return info
                }
            })

        }
    }

    fun getBitMap(): Bitmap {
        try {

            var drawable = resources.getDrawable(R.drawable.icon_location_map)
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            var canvas = Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

        } catch (ex: Exception) {

        }
        return bitmap;
    }

    override fun onBackPressed() {
      backToHome()
    }

    fun backToHome() {
        startActivity(Intent(this, MainActivityNavigation::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", "Team"))
        finish()
    }
}
