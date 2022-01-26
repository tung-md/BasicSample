/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.basicsample.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.basicsample.R;
import com.android.basicsample.binding.FragmentDataBindingComponent;
import com.android.basicsample.databinding.LoginFragmentBinding;
import com.android.basicsample.ui.common.NavigationController;
import com.android.basicsample.ui.common.RepoListAdapter;
import com.android.basicsample.util.AutoClearedValue;
import com.android.basicsample.vo.Resource;
import com.android.basicsample.vo.result.Token;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.support.DaggerFragment;

import javax.inject.Inject;

public class LoginFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<LoginFragmentBinding> binding;

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LoginFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.login_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        loginViewModel.getToken().observe(getViewLifecycleOwner(), tokenResource -> {
            Log.d("Token Test", "" + tokenResource.data);
        });

        initLoginInputListener();
    }

    private void initLoginInputListener() {
        binding.get().loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin(view);
            }
        });
        binding.get().inputUsername.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Dismiss keyboard
                dismissKeyboard(v.getWindowToken());
                return true;
            }
            return false;
        });
        binding.get().inputPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Dismiss keyboard
                dismissKeyboard(v.getWindowToken());
                return true;
            }
            return false;
        });
    }

    private void doLogin(View v) {
        String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String username = binding.get().inputUsername.getText().toString();
        String password = binding.get().inputPassword.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        loginViewModel.setLogin(username, password, androidId);
    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
