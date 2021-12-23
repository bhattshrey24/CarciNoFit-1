package com.example.carcinofit.database.models

class ResultDataClass {
//    val listOfObj = arrayOf(
//        ResultDataClassStructure(0.085f, 0.915f, arrayOf("Caffeine","Ethanol","Isobutene"), 3500L),// tot 35
//        ResultDataClassStructure(0.0625f, 0.9375f, arrayOf("Carbromal","Benzofuran"), 3000L),// tot 32
//        ResultDataClassStructure(0.14f, 0.86f, arrayOf("Anilazine","Acrolein","Sulfisoxazole","Sulfallate"), 4500L),// tot 28
//        ResultDataClassStructure(0.048f, 0.952f, arrayOf("Diazinon","Iodoform"), 5600L),// tot 41
//        ResultDataClassStructure(0.027f, 0.973f, arrayOf("Saccharin"), 6500L),// tot 36
//
//
//        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 8000L),// tot 36
//        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 4000L)// tot 36
//
//    )

    val listOfObjOfProd1 = arrayOf(
        ResultDataClassStructure(0.085f, 0.915f, arrayOf("Isobutene","Sulfisoxazole","Meloxicam"), 2500L),// tot 35
        ResultDataClassStructure(0.031f, 0.969f, arrayOf("Isobutene"), 3200L),// tot 32
        ResultDataClassStructure(0.035f, 0.965f, arrayOf("Sulfisoxazole","Meloxicam"), 4500L),// tot 28
        ResultDataClassStructure(0.024f, 0.976f, arrayOf("Meloxicam"), 4600L),// tot 41
    )
    val listOfObjOfProd2 = arrayOf(
        ResultDataClassStructure(0.11f, 0.89f, arrayOf("Sesamol","Symphytine","Theophylline","Aniline"), 3500L),// tot 35
        ResultDataClassStructure(0.032f, 0.968f, arrayOf("Trichlorfon"), 4200L),// tot 32
        ResultDataClassStructure(0.035f, 0.965f, arrayOf("Symphytine","Theophylline"), 5500L),// tot 28
        ResultDataClassStructure(0.073f, 0.927f, arrayOf("Sesamol","Symphytine","Theophylline"), 5600L),// tot 41
    )
    val listOfObjOfNF = arrayOf(
        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 7000L),// tot 36
        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 4200L),// tot 36
        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 6700L),// tot 36
        ResultDataClassStructure(0.0f, 0.1f, arrayOf("Non Found"), 3500L)// tot 36
    )
//    ResultDataClassStructure(0.11f, 0.89f, arrayOf("Hematoxylin,Saccharin,Retrorsine,Triethanolamine"), 7000L)
//    ResultDataClassStructure(0.01f, 0.99f, arrayOf("Diazinon,Dulcin,Iodoform"), 5000L),

}