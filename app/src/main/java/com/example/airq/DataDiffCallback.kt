package com.example.airq

import android.os.Bundle
import androidx.annotation.Nullable

import androidx.recyclerview.widget.DiffUtil
import com.example.airq.model.Data


class DataDiffCallback(oldDataList: List<Data>, newDataList: List<Data>) :
    DiffUtil.Callback() {
    private val mOldDataList: List<Data>
    private val mNewDataList: List<Data>
    override fun getOldListSize(): Int {
        return mOldDataList.size
    }

    override fun getNewListSize(): Int {
        return mNewDataList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldDataList[oldItemPosition].aqi === mNewDataList[newItemPosition].aqi
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result: Int = mNewDataList[newItemPosition].compareTo(mOldDataList[oldItemPosition])
        return result == 0
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        val newModel: Data = mNewDataList[newItemPosition]
        val oldModel: Data = mOldDataList[oldItemPosition]
        val diff = Bundle()
        if (newModel.aqi !== oldModel.aqi) {
            diff.putDouble("aqi", newModel.aqi)
        }
        return if (diff.size() == 0) {
            null
        } else diff
        //        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    init {
        mOldDataList = oldDataList
        mNewDataList = newDataList
    }
}