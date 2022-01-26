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

package com.android.basicsample.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import com.android.basicsample.AppExecutors;
import com.android.basicsample.api.ApiResponse;
import com.android.basicsample.api.GithubService;
import com.android.basicsample.db.TokenDao;
import com.android.basicsample.vo.Resource;
import com.android.basicsample.vo.request.LoginRequest;
import com.android.basicsample.vo.result.Token;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Auth objects.
 */
@Singleton
public class AuthRepository {
    private final TokenDao tokenDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;

    @Inject
    AuthRepository(TokenDao tokenDao, AppExecutors appExecutors, GithubService githubService) {
        this.tokenDao = tokenDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<Token>> login(LoginRequest loginRequest) {
        return new NetworkBoundResource<Token, Token>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Token item) {
                tokenDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Token data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Token> loadFromDb() {
                return tokenDao.loadToken();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return githubService.login(loginRequest);
            }
        }.asLiveData();
    }

}
