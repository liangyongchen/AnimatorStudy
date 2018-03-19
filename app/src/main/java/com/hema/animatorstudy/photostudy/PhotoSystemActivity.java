package com.hema.animatorstudy.photostudy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hema.animatorstudy.R;
import com.hema.baselibrary.base.BaseActivity;
import com.hema.baselibrary.base.EquipmentAuthorization;
import com.hema.baselibrary.util.ImageDisplayUtil;
import com.hema.nicedialoglibrary.BaseNiceDialog;
import com.hema.nicedialoglibrary.NiceDialog;
import com.hema.nicedialoglibrary.ViewConvertListener;
import com.hema.nicedialoglibrary.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2017-10-20
 * 调用系统相册和系统相机
 */
public class PhotoSystemActivity extends BaseActivity {

    @BindView(R.id.ivImage)
    ImageView mIvImage;
    @BindView(R.id.btnPhoto)
    Button mBtnPhoto;

    public final static int REQUEST_PHOTO = 201; // 系统相册
    public final static int REQUEST_CAMERA1 = 202; // 系统相机
    public final static int REQUEST_CAMERA2 = 203; // 系统相机
    Uri photoURI;

    @Override
    public int getContentView() {
        return R.layout.activity_photo_system;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("系统相机拍照/相册");
    }

    @Override
    public void initEvent() {

    }

    @OnClick({R.id.ivImage, R.id.btnPhoto})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage:
                break;
            case R.id.btnPhoto:
                openCameraOrPhoto();
                break;
        }
    }

    // 打开相机/相册
    private void openCameraOrPhoto() {
        NiceDialog.init()
                .setLayoutId(R.layout.nice_dialog)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {

                        // 拍照
                        holder.setOnClickListener(R.id.photograph, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermission(EquipmentAuthorization.PERMISSION_READ_CAMERA, EquipmentAuthorization.PERMISSION_READ_CAMERA_KEY);
                                dialog.dismiss();
                            }
                        });

                        // 相册
                        holder.setOnClickListener(R.id.album, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermission(EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE,
                                        EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE_KEY);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(getSupportFragmentManager());
    }


    @Override
    protected void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        if (requestCode == EquipmentAuthorization.PERMISSION_READ_CAMERA_KEY) {

            // 拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT >= 21) {

                if (intent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try {
                        photoFile = createImageFile(); // 创建临时图片文件
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (photoFile != null) {
                        // FileProvider 是一个特殊的 ContentProvider 的子类，
                        // 它使用 content:// Uri 代替了 file:/// Uri. ，更便利而且安全的为另一个app分享文件
                        photoURI = FileProvider.getUriForFile(this, "com.hema.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, REQUEST_CAMERA2);
                    }

                }
            } else {
                /**
                 * 缩略图
                 */
                if (intent.resolveActivity(getPackageManager()) != null) {// 判断是否有相机应用
                    startActivityForResult(intent, REQUEST_CAMERA1);
                }
            }

        } else if (requestCode == EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE_KEY) {
            // 相册的调用
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*");
            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentFromGallery, REQUEST_PHOTO);
        }
    }

    // 私有

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
        File storageDir = getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                storageDir      /* 文件夹 */
        );
        mCurrentPhotoPath = image.getAbsolutePath(); // 获取图片路径
        return image;
    }

    @Override
    protected void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        if (requestCode == EquipmentAuthorization.PERMISSION_READ_CAMERA_KEY) {
            // 拍照
            againPermission(EquipmentAuthorization.PERMISSION_READ_CAMERA, EquipmentAuthorization.PERMISSION_READ_CAMERA_KEY);
        } else if (requestCode == EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE_KEY) {
            // 相册的调用
            againPermission(EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE,
                    EquipmentAuthorization.ANDROID_PERMISSION_GROUP_STORAGE_KEY);
        }
    }

    // 提示是否再次授权
    private void againPermission(String[] permissions, int requestCode) {
        new AlertDialog.Builder(this)
                .setMessage("\n   亲，需要授权之后才能使用哦！\n\n是否再次授权？\n")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        requestPermission(permissions, requestCode);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;

        switch (requestCode) {
            case REQUEST_PHOTO:
                if (data == null) {
                    return;
                }
                photoResult(requestCode, resultCode, data);
                break;
            case REQUEST_CAMERA1:
                try {

                    if (data == null) {
                        return;
                    }

                    if (resultCode != Activity.RESULT_OK) return;
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= 23) {

                    } else if (Build.VERSION.SDK_INT > 19) {
                        uri = data.getData();
                    } else {
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    }

                    if (data != null) {
                        String photopath = null;
                        Cursor cursor = this.getApplication().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            photopath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            Log.e("photopath", "photopath:------------" + photopath);
                        }
                    }
                } catch (Exception e) {
                    String ss = e.getMessage();
                }
                if (bitmap != null) {
                    mIvImage.setImageBitmap(bitmap);
                }
                // cameraResult(requestCode, resultCode, data);
                break;

            case REQUEST_CAMERA2:
                if (resultCode != Activity.RESULT_OK) return;

                // Get the dimensions of the View
                int targetW = mIvImage.getWidth();
                int targetH = mIvImage.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                Uri uu = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                Uri uri = Uri.parse(mCurrentPhotoPath);
                if (bitmap != null) {
                    mIvImage.setImageBitmap(bitmap);
                }

                break;
        }


    }

    // 相机返回结果处理
    private void cameraResult(int requestCode, int resultCode, Intent data) {
        // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的
        Uri mImageCaptureUri = data.getData();
        if (mImageCaptureUri != null) {
            // 取得图片的绝对路径
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picPath = cursor.getString(columnIndex);   // 图片的绝对路径
            cursor.close();
            // 打印路径
            Log.e("绝对路径：", picPath);
            try {
                // 这个方法是根据Uri获取Bitmap图片的静态方法
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                if (image != null) {
                    android.util.Log.e("path", ":" + data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果为空，那么我们就进行下面的方式获取
        // 能获取到Bitmap，但无论如何无法取得图片路径？？？？？
        else {
            Bundle extras = data.getExtras();
            if (extras != null) {
                // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                Bitmap image = extras.getParcelable("data");
                if (image != null) {
                    android.util.Log.e("path without data", ":" + data);
                }
            }
        }
    }

    // 相册结果
    private void photoResult(int requestCode, int resultCode, Intent data) {
        try {
            Uri uri = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            ImageDisplayUtil.displayFromSDCard(this, uri, mIvImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
