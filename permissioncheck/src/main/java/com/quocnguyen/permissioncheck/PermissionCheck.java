package com.quocnguyen.permissioncheck;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionCheck {
    static final String TAG = "Permissions";

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private Activity activity;
        private List<String> permissions;

        Builder(Activity activity) {
            if (activity == null) {
                throw new IllegalStateException("Activity must not be null");
            }
            this.activity = activity;
        }

        public Builder check(String... permissions) {
            if (this.permissions == null) {
                this.permissions = new ArrayList<>();
            }
            if (permissions != null) {
                this.permissions.addAll(Arrays.asList(permissions));
            }
            return this;
        }

        public void request(PermissionGrantedListener listener) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (listener != null) {
                    listener.onGranted(true);
                }
            } else {
                PermissionsFragment fragment = findPermissionsFragment(activity);
                if (fragment != null) {
                    fragment.requestPermissions(listener, permissions);
                } else {
                    Toast.makeText(activity, "Can't request permission", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private PermissionsFragment findPermissionsFragment(Activity activity) {
            if (activity instanceof FragmentActivity) {
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                PermissionsFragment fragment = (PermissionsFragment)
                        fragmentManager.findFragmentByTag(TAG);
                if (fragment == null) {
                    fragment = new PermissionsFragment();
                    fragmentManager.beginTransaction()
                            .add(fragment, TAG)
                            .commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                }
                return fragment;
            }
            return null;
        }
    }

    public interface PermissionGrantedListener {
        /**
         * Permission grant callback
         *
         * @param granted whether to grant
         */
        void onGranted(boolean granted);
    }
}
