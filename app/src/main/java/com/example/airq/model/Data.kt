package com.example.airq.model

import java.io.Serializable

data class Data(val city:String,val aqi:Double):Serializable,Comparable<Data> {
    override fun compareTo(other: Data): Int {
        if(other.city == this.city && other.aqi == this.aqi)
            return 0
        return 1
    }
}