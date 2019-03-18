package com.bethena.permissionrequesttest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.reactivex.functions.Consumer;

public class PermissionTestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_phone;
    TextView tv_location;

    RxPermissions rxPermissions;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_test);

        findViewById(R.id.btn_request_storage).setOnClickListener(this);
        findViewById(R.id.btn_request_phone).setOnClickListener(this);
        findViewById(R.id.btn_request_location).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_to_setting).setOnClickListener(this);

        tv_phone = findViewById(R.id.tv_phone);
        tv_location = findViewById(R.id.tv_location);

        rxPermissions = new RxPermissions(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("我要权限(这是我们自己的弹窗，要求用户去系统页面打开权限)");
        builder.setMessage("求求大佬给个权限，点点权限，打开一下。");
        builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toSettingPage();
            }
        });
        builder.setNegativeButton("滚", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
    }

    private void toSettingPage() {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        mIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(mIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_storage:
                rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                storagePermission(permission);
                            }
                        });
                break;
            case R.id.btn_request_phone:
                rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                phonePermission(permission);
                            }
                        });
                break;
            case R.id.btn_request_location:
                rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                locationPermission(permission);
                            }
                        });
                break;
            case R.id.btn_three:
                rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if(permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                    storagePermission(permission);
                                }else if(permission.name.equals(Manifest.permission.READ_PHONE_STATE)){
                                    phonePermission(permission);
                                }else if(permission.name.equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                                    locationPermission(permission);
                                }
                            }
                        });
                break;
            case R.id.btn_to_setting:
                toSettingPage();
                break;
        }
    }


    private void storagePermission(Permission permission){
        if (permission.granted) {
            String path = savePic();
            Toast.makeText(PermissionTestActivity.this, "已经获得权限并向你的根目录放了个文件：" + path, Toast.LENGTH_LONG).show();
        } else if (permission.shouldShowRequestPermissionRationale) {
            String path = savePic();
            Toast.makeText(PermissionTestActivity.this, "被拒绝了/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
        } else {
            alertDialog.show();
        }
    }
    private void phonePermission(Permission permission){
        if (permission.granted) {
            String imei = getImei();
            Toast.makeText(PermissionTestActivity.this, "已经获得权限，手机imei：" + imei, Toast.LENGTH_LONG).show();
            tv_phone.setText("手机imei : " + imei);
        } else if (permission.shouldShowRequestPermissionRationale) {
            String imei = getImei();
            Toast.makeText(PermissionTestActivity.this, "被拒绝了/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
        } else {
            alertDialog.show();
        }
    }

    private void locationPermission(Permission permission){
        if (permission.granted) {
            openGPSSettings();
        } else if (permission.shouldShowRequestPermissionRationale) {
            openGPSSettings();
            Toast.makeText(PermissionTestActivity.this, "被拒绝了/(ㄒoㄒ)/~~", Toast.LENGTH_LONG).show();
        } else {
            alertDialog.show();
        }
    }

    public String savePic() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Angogo/";
        if(!new File(path).exists()){
            new File(path).mkdirs();
        }
        String filePath = path + UUID.randomUUID().toString().replace("-", "");
        File file = new File(filePath);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filePath = null;
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return filePath;
        }
    }


    @SuppressLint("MissingPermission")
    public String getImei() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String id;
        try{
            id = tm.getDeviceId();
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, " GPS模块正常 ", Toast.LENGTH_SHORT).show();
            getLocation();
        } else {
//            Toast.makeText(this, " GPS模块失败 ", Toast.LENGTH_SHORT).show();

        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
// 获取位置管理服务
            LocationManager locationManager;
            String serviceName = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) this.getSystemService(serviceName);
// 查找到服务信息
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

            String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
            Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
            updateToNewLocation(location);
// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
            locationManager.requestLocationUpdates(provider, 100 * 1000, 500, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateToNewLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateToNewLocation(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            tv_location.setText(" 维度： " + latitude + " \n经度 " + longitude);
        } else {
            tv_location.setText(" 无法获取地理信息 ");
        }

    }
}
