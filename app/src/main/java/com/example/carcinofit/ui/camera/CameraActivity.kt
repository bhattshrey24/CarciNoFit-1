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
import com.example.carcinofit.databinding.CameraActivityBinding
import com.example.carcinofit.other.Constants
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null // stores the captured Image
    private lateinit var outputDirectory: File

    private var myLayoutManager: RecyclerView.LayoutManager? = null
    private var myAdapter: RecyclerView.Adapter<ResultAdapterClass.MyViewHolder>? = null

    private val binding: CameraActivityBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.camera_activity, null, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        myLayoutManager = LinearLayoutManager(this)
        var myRecyclerView = findViewById<RecyclerView>(R.id.myResultRecyclerView)
        myRecyclerView.layoutManager = myLayoutManager
        myAdapter = ResultAdapterClass()
        myRecyclerView.adapter = myAdapter

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
                    val msg = "Photo saved"

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
                        Toast.makeText(
                            this@CameraActivity,
                            "Sending to server for response",
                            Toast.LENGTH_SHORT
                        ).show()

                        gettingResponse()
                    }

                    binding.btnAgainTakePhoto.setOnClickListener {
                        binding.viewFinder.visibility = View.VISIBLE
                        binding.myImageView.visibility = View.GONE
                        //startCamera()
                    }


//                    Toast.makeText(
//                        this@CameraActivity,
//                        "$msg $savedUri",
//                        Toast.LENGTH_SHORT
//                    ).show()

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constants.TAG, "onError : ${exception.message}", exception)
                }
            }
        )
    }

    private fun gettingResponse() {

        binding.myImageView.visibility = View.GONE
        binding.viewFinder.visibility = View.GONE
        binding.btnTakePhoto.visibility = View.GONE
        binding.btnAgainTakePhoto.visibility = View.GONE
        binding.btnPhotoAccepted.visibility = View.GONE

        binding.resultProgressBar.visibility = View.VISIBLE

        val handler = Handler()
        handler.postDelayed({
            binding.dummyConstraintView.visibility=View.VISIBLE
            binding.resultProgressBar.visibility = View.GONE
            Toast.makeText(
                this@CameraActivity,
                "Response Received",
                Toast.LENGTH_SHORT
            ).show()
        },5000L)


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
