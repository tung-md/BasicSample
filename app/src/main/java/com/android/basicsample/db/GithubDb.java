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

package com.android.basicsample.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.android.basicsample.vo.Contributor;
import com.android.basicsample.vo.Repo;
import com.android.basicsample.vo.RepoSearchResult;
import com.android.basicsample.vo.User;

/**
 * Main database description.
 */
@Database(entities = {User.class, Repo.class, Contributor.class,
        RepoSearchResult.class}, version = 3)
public abstract class GithubDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public RepoDao repoDao();
}
