package com.example.carcinofit.ui.camera

import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carcinofit.R
import com.example.carcinofit.adapters.ResultAdapterClass
import com.example.carcinofit.database.models.ResultDataClass
import com.example.carcinofit.database.models.ResultDataClassStructure
import com.example.carcinofit.databinding.CameraActivityBinding
import com.example.carcinofit.other.Constants
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null // stores the captured Image
    private lateinit var outputDirectory: File


    private var myAdapter: RecyclerView.Adapter<ResultAdapterClass.MyViewHolder>? = null

    private val binding: CameraActivityBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.camera_activity, null, false)
    }

    private lateinit var pieChart: PieChart

    //private var dummyTitleLiveData= arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        myLayoutManager = LinearLayoutManager(this)
//        var myRecyclerView = findViewById<RecyclerView>(R.id.myResultRecyclerView)
//        myRecyclerView.layoutManager = myLayoutManager
//        myAdapter = ResultAdapterClass(this,dummyTitleLiveData)
//        myRecyclerView.adapter = myAdapter

        outputDirectory =
            getOutputDirectory() // creates an output directory with name same as out app_name and gives the instance of outputDirectory , so here we are ininitalizing the " outputDirectory"

        if (allPermissionGranted()) {
            //Toast.makeText(this, "We Have the permission", Toast.LENGTH_LONG).show()
            startCamera()
        } else {
            ActivityCompat.requestPermissions( // asking for permission
                this,
                Constants.REQUIRED_PERMISSIONS,
                Constants.REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }

    }

    private fun takePhoto() {
        val imageCapture = imageCapture
            ?: return // if image is not captured then simply return i.e. if "imageCapture == null" then return else assign the image to variable
        val photoFile =
            File(
                outputDirectory, SimpleDateFormat(
                    Constants.FILE_NAME_FORMAT, Locale.getDefault()
                ).format(System.currentTimeMillis()) + ".jpg"
            )
        val outputOption = ImageCapture
            .OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    val states = arrayOf(
                        intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(-android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_pressed)
                    )
                    val colors = intArrayOf(
                        Color.YELLOW,
                        Color.BLACK,
                        Color.GREEN,
                        Color.BLUE
                    )
                    val myList: ColorStateList = ColorStateList(states, colors);
                    binding.btnTakePhoto.backgroundTintList =
                        myList // changes the color after being pressed

                    binding.viewFinder.visibility = View.GONE
                    binding.myImageView.setImageURI(savedUri)

                    binding.btnAgainTakePhoto.visibility = View.VISIBLE
                    binding.btnPhotoAccepted.visibility = View.VISIBLE

                    binding.btnPhotoAccepted.setOnClickListener {
//                        Toast.makeText(
//                            this@CameraActivity,
//                            "Sending to server for response",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        val actionString:String="prod1"
                        gettingResponse(actionString)
                    }

                    binding.btnAgainTakePhoto.setOnClickListener {
                        binding.viewFinder.visibility = View.VISIBLE
                        binding.myImageView.visibility = View.GONE
                        //startCamera()

                        val actionString:String="Not Found"
                        gettingResponse(actionString)
                    }

                    binding.backButton.setOnClickListener{

                        val actionString:String="prod2"
                        gettingResponse(actionString)
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError : ${exception.message}", exception)
                }
            }
        )
    }

    private fun gettingResponse(actionString:String) {
        binding.myImageView.visibility = View.GONE
        binding.viewFinder.visibility = View.GONE
        binding.btnTakePhoto.visibility = View.GONE
        binding.btnAgainTakePhoto.visibility = View.GONE
        binding.btnPhotoAccepted.visibility = View.GONE
        binding.resultProgressBar.visibility = View.VISIBLE

        val dummyObj: ResultDataClass = ResultDataClass()
        pieChart = findViewById(R.id.resultPieChart);
        setupPieChart();

        if(actionString=="prod1"){

            val sizeOfList = dummyObj.listOfObjOfProd1.size - 1
            val randomNum = (0..sizeOfList).random()
            val currentObj: ResultDataClassStructure = dummyObj.listOfObjOfProd1[randomNum]

            loadPieChart(currentObj.percenOfNonToxicEle, currentObj.percenOfToxicEle);
            setRecyclerView(currentObj.listOfToxicEle)
            val handler = Handler()
            handler.postDelayed({
                binding.statsLayout.root.visibility = View.VISIBLE
                binding.resultProgressBar.visibility = View.GONE

            }, currentObj.delay)

        }else if(actionString=="prod2"){

            val sizeOfList = dummyObj.listOfObjOfProd2.size - 1
            val randomNum = (0..sizeOfList).random()
            val currentObj: ResultDataClassStructure = dummyObj.listOfObjOfProd2[randomNum]

            loadPieChart(currentObj.percenOfNonToxicEle, currentObj.percenOfToxicEle);
            setRecyclerView(currentObj.listOfToxicEle)

            val handler = Handler()
            handler.postDelayed({
                binding.statsLayout.root.visibility = View.VISIBLE
                binding.resultProgressBar.visibility = View.GONE
            }, currentObj.delay)

        }else{// not found case
            val sizeOfList = dummyObj.listOfObjOfNF.size - 1
            val randomNum = (0..sizeOfList).random()
            val currentObj: ResultDataClassStructure = dummyObj.listOfObjOfNF[randomNum]

            loadPieChart(currentObj.percenOfNonToxicEle, currentObj.percenOfToxicEle);
            setRecyclerView(currentObj.listOfToxicEle)

            val handler = Handler()

            handler.postDelayed({
                binding.statsLayout.root.visibility = View.VISIBLE
                binding.resultProgressBar.visibility = View.GONE
            }, currentObj.delay)
        }
    }

    //            Toast.makeText(
//                this@CameraActivity,
//                "Response Received",
//                Toast.LENGTH_SHORT
//            ).show()
    private fun setRecyclerView(dummy: Array<String>) {
        myAdapter = ResultAdapterClass(this, dummy)
        binding.statsLayout.myResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CameraActivity)
            adapter = myAdapter
        }
    }


    private fun loadPieChart(percentageOfNonToxicEle: Float, percentageOfToxicEle: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(percentageOfNonToxicEle, "Non Toxic Elements"))
        entries.add(PieEntry(percentageOfToxicEle, "Toxic Elements"))

        val colors: ArrayList<Int> = ArrayList()

        colors.add(resources.getColor(R.color.secondaryLightColor))
        colors.add(resources.getColor(R.color.secondaryColor))
//        for (color in ColorTemplate.VORDIPLOM_COLORS) {
//            colors.add(color)
//        }

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(colors)
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        pieChart.setData(data)
        pieChart.invalidate()
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun setupPieChart() {
        pieChart.setDrawHoleEnabled(true)
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setCenterTextSize(24f)
        pieChart.getDescription().setEnabled(false)
        val l: Legend = pieChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setEnabled(true)
    }

    private fun startCamera() { // does initial setup for the camera to start

        val cameraProviderFuture = ProcessCameraProvider.getInstance((this))

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also { mPreview ->

                mPreview.setSurfaceProvider(binding.viewFinder.surfaceProvider) // giving surface to camera

            }

            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(3 / 2) // required otherwise file becomes way to large to display
                //.setTargetResolution(Size(300, 700))
                .build()

            val cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA//  maybe give option later for switching to front
            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                ) // binding camera to the activity life cycle cause cameraX is lifecycle aware
            } catch (e: Exception) {
                Log.d(Constants.TAG, "startCamera : Fail buddy", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }


    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }

        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir

    }

    private fun allPermissionGranted() = // checks if all permissions are granted or not
        Constants.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            }
        } else {
            Toast.makeText(this, "We Dont Have the permission", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
