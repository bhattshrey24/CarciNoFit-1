package com.example.carcinofit.database.models

import java.util.ArrayList

class ResultDataClassStructure (
    val percenOfToxicEle:Float,

    val percenOfNonToxicEle:Float,

    val listOfToxicEle: Array<String>,
    val delay:Long)
