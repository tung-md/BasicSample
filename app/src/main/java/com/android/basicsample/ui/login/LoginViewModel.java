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

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.android.basicsample.repository.AuthRepository;
import com.android.basicsample.util.AbsentLiveData;
import com.android.basicsample.vo.Resource;
import com.android.basicsample.vo.request.DeviceInfo;
import com.android.basicsample.vo.request.LoginRequest;
import com.android.basicsample.vo.result.Token;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<LoginRequest> loginRequest = new MutableLiveData<>();
    private final LiveData<Resource<Token>> token;

    @Inject
    LoginViewModel(AuthRepository authRepository) {
        token = Transformations.switchMap(loginRequest, loginRequest -> {
            if (loginRequest == null) {
                return AbsentLiveData.create();
            } else {
                return authRepository.login(loginRequest);
            }
        });
    }

    @VisibleForTesting
    public void setLogin(String username, String password, String androidId) {

        DeviceInfo deviceInfo = new DeviceInfo(androidId, "DEVICE_TYPE_ANDROID", "N1");
        LoginRequest loginRequest = new LoginRequest(username, password, deviceInfo);
        this.loginRequest.setValue(loginRequest);
    }

    @VisibleForTesting
    public LiveData<Resource<Token>> getToken() {
        return token;
    }
}
