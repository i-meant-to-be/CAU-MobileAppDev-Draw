package com.imeanttobe.drawapplication.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.imeanttobe.drawapplication.R
import java.io.ByteArrayOutputStream

class StorageUtil{
    companion object {
        fun uploadProfilePhotoToStorage(imageUri: Uri, context: Context, uid: String, onComplete: (Uri?) -> Unit) {
            val storageReference = FirebaseStorage.getInstance()
                .reference
                .child("profile_photo/$uid.jpg") // UID 기반 경로 설정

            val uploadTask =
                if (imageUri != Uri.EMPTY) {
                    storageReference.putFile(imageUri)
                } else {
                    // Drawable을 Bitmap으로 변환한 뒤 ByteArray로 만들어 업로드
                    val drawableResId = R.drawable.basic_profile // 기본 이미지 리소스 ID
                    val drawable = ContextCompat.getDrawable(context, drawableResId)
                    val bitmap = (drawable as BitmapDrawable).bitmap

                    // Bitmap을 ByteArray로 변환
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    // Firebase Storage에 업로드
                    storageReference.putBytes(data)
                }

            uploadTask.addOnSuccessListener {
                // 업로드 성공 시 다운로드 URL 가져오기
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onComplete(downloadUri) // 다운로드 URL 반환
//                    Toast.makeText(context,"upload successed",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    onComplete(null) // 실패 시 null 반환
//                    Toast.makeText( context,"downloadUri : upload failed",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                onComplete(null) // 업로드 실패 시 null 반환
//                Toast.makeText(context, "task : upload failed", Toast.LENGTH_SHORT).show()
            }
        }

        fun uploadPictureToStorage(imageUri: Uri, context: Context, uid: String, num:Int, onComplete: (Uri?) -> Unit) {
            val storageReference = FirebaseStorage.getInstance()
                .reference
                .child("post_picture/$uid/$num.jpg") // UID 기반 경로 설정

            if(imageUri == Uri.EMPTY){
                Log.d("StorageUtil", "uploadPicturToStorage : empty imageUri")
            }

            val uploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // 업로드 성공 시 다운로드 URL 가져오기
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onComplete(downloadUri) // 다운로드 URL 반환
                    Toast.makeText(context, context.getString(R.string.upload_complete), Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    onComplete(null) // 실패 시 null 반환
                    // Toast.makeText( context,"downloadUri : upload failed",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                onComplete(null) // 업로드 실패 시 null 반환
                // Toast.makeText(context, "task : upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}