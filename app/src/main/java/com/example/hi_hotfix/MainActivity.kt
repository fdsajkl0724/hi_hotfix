package com.example.hi_hotfix

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun test(view: View) {
        Toast.makeText(this,HotFixTest().test(),Toast.LENGTH_SHORT).show()
    }
    fun fixBug(view: View) {
        val permission =android.Manifest.permission.READ_EXTERNAL_STORAGE
        if (ActivityCompat.checkSelfPermission(this,permission) ==PackageManager.PERMISSION_GRANTED){
            fix()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(permission),1000)
        }
    }

    private fun fix() {

        Hotfix.fix(this, File(Environment.getExternalStorageDirectory(),"patch.dex"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            fix()
        }
    }
}