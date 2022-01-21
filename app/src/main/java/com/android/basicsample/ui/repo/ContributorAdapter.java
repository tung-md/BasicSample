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

package com.android.basicsample.ui.repo;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import com.android.basicsample.R;
import com.android.basicsample.databinding.ContributorItemBinding;
import com.android.basicsample.ui.common.DataBoundListAdapter;
import com.android.basicsample.vo.Contributor;

import java.util.Objects;

public class ContributorAdapter
        extends DataBoundListAdapter<Contributor, ContributorItemBinding> {

    private final DataBindingComponent dataBindingComponent;
    private final ContributorClickCallback callback;

    public ContributorAdapter(DataBindingComponent dataBindingComponent,
            ContributorClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    protected ContributorItemBinding createBinding(ViewGroup parent) {
        ContributorItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.contributor_item, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Contributor contributor = binding.getContributor();
            if (contributor != null && callback != null) {
                callback.onClick(contributor);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ContributorItemBinding binding, Contributor item) {
        binding.setContributor(item);
    }

    @Override
    protected boolean areItemsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getLogin(), newItem.getLogin());
    }

    @Override
    protected boolean areContentsTheSame(Contributor oldItem, Contributor newItem) {
        return Objects.equals(oldItem.getAvatarUrl(), newItem.getAvatarUrl())
                && oldItem.getContributions() == newItem.getContributions();
    }

    public interface ContributorClickCallback {
        void onClick(Contributor contributor);
    }
}
