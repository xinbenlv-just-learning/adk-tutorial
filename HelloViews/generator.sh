#!/bin/sh

#Hello Layouts
for var in GridView LinearLayout RelativeLayout TableLayout TabLayout DatePicker TimePicker FormStuff Spinner AutoComplete Gallery GoolgeMapView WebView
#for var in GridView 
do
android create project --target 1  --path ./Hello$var --activity Hello$var --package com.example.Hello$var
done


