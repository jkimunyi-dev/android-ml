package com.example.android_ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val img: ImageView = findViewById(R.id.imageFace)

        val face = R.drawable.sb2

        try {
            val bitmap: Bitmap? = BitmapFactory.decodeResource(resources, face)

            img.setImageResource(face)
            Toast.makeText(this, "Image set successfully", Toast.LENGTH_LONG).show()

            val btn: Button = findViewById(R.id.btnTest)
            btn.setOnClickListener{
                val highAccuracyOpts = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build()

                val detector = FaceDetection.getClient(highAccuracyOpts)

                val image = InputImage.fromBitmap(bitmap!!,0)

                val result = detector.process(image)
                    .addOnSuccessListener { faces ->
                        // Task completed successfully
                        bitmap?.apply {
                            img.setImageBitmap(drawWithRectangle(faces))
                        }
                    }
                    .addOnFailureListener{e->{

                    }}
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error setting image: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

//Draw REctangle functionality
private fun Bitmap.drawWithRectangle(faces: List<Face>): Bitmap?{
    val bitmap = copy(config,true)
    val canvas = Canvas(bitmap)

    for (face in faces){
        val bounds = face.boundingBox
        Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            isAntiAlias = true

            // Draw Rectangle on canvas
            canvas.drawRect(
                bounds,
                this
            )
        }
    }
    return bitmap
}