
package com.example.moviesapp.data.network;

import com.example.moviesapp.Model.CastModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastsResponseBody {

    @SerializedName("cast")
    private List<CastModel> mCastModel;

    public List<CastModel> getCast() {
        return mCastModel;
    }

    public void setCast(List<CastModel> castModel) {
        mCastModel = castModel;
    }

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    @SerializedName("id")
    private Long mId;


}
