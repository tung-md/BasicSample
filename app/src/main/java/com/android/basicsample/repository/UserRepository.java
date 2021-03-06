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
import com.android.basicsample.db.UserDao;
import com.android.basicsample.vo.Resource;
import com.android.basicsample.vo.User;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles User objects.
 */
@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao, GithubService githubService) {
        this.userDao = userDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login) {
        return new NetworkBoundResource<User,User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}
