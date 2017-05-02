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

package android.service.autofill;

import static android.view.autofill.Helper.sDebug;

import android.annotation.NonNull;
import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a context for each fill request made via {@link
 * AutofillService#onFillRequest(FillRequest, CancellationSignal, FillCallback)}.
 * It contains a snapshot of the UI state, the view ids that were returned by
 * the {@link AutofillService autofill service} as both required to trigger a save
 * and optional that can be saved, and the id of the corresponding {@link
 * FillRequest}.
 * <p>
 * This context allows you to inspect the values for the interesting views
 * in the context they appeared. Also a reference to the corresponding fill
 * request is useful to store meta-data in the client state bundle passed
 * to {@link FillResponse.Builder#setClientState(Bundle)} to avoid interpreting
 * the UI state again while saving.
 */
public final class FillContext implements Parcelable {
    private final int mRequestId;
    private final @NonNull AssistStructure mStructure;

    /** @hide */
    public FillContext(int requestId, @NonNull AssistStructure structure) {
        mRequestId = requestId;
        mStructure = structure;
    }

    private FillContext(Parcel parcel) {
        this(parcel.readInt(), parcel.readParcelable(null));
    }

    /**
     * Gets the id of the {@link FillRequest fill request} this context
     * corresponds to. This is useful to associate your custom client
     * state with every request to avoid reinterpreting the UI when saving
     * user data.
     *
     * @return The request id.
     */
    public int getRequestId() {
        return mRequestId;
    }

    /**
     * @return The screen content.
     */
    public AssistStructure getStructure() {
        return mStructure;
    }

    @Override
    public String toString() {
        if (!sDebug)  return super.toString();

        return "FillContext [reqId=" + mRequestId + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mRequestId);
        parcel.writeParcelable(mStructure, flags);
    }

    public static final Parcelable.Creator<FillContext> CREATOR =
            new Parcelable.Creator<FillContext>() {
        @Override
        public FillContext createFromParcel(Parcel parcel) {
            return new FillContext(parcel);
        }

        @Override
        public FillContext[] newArray(int size) {
            return new FillContext[size];
        }
    };
}
