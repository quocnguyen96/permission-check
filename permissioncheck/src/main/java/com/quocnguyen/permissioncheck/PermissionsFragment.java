package com.quocnguyen.permissioncheck;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

public class PermissionsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 1009;


    private PermissionCheck.PermissionGrantedListener mPermissionGrantedListener;

    public PermissionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(PermissionCheck.PermissionGrantedListener listener, List<String> permissions) {
        mPermissionGrantedListener = listener;

        if (checkPermission(permissions) && mPermissionGrantedListener != null) {
            mPermissionGrantedListener.onGranted(true);
        } else {
            //Request permission popup
            requestPermissions(permissions.toArray(new String[]{}), PERMISSIONS_REQUEST_CODE);
        }
    }

    public boolean checkPermission(List<String> permissions) {
        boolean isGranted = true;

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE && isAllPermissionGranted(grantResults)) {
            if (mPermissionGrantedListener != null) {
                mPermissionGrantedListener.onGranted(true);
            }
        } else {
            if (mPermissionGrantedListener != null) {
                mPermissionGrantedListener.onGranted(false);
            }
        }
    }


    private boolean isAllPermissionGranted(int[] grantResults) {
        int results = 0;
        for (int i : grantResults) {
            results = results + i;
        }
        return results == 0;
    }
}
