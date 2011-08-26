#!/bin/sh

#Hello Layouts

for var in GridView ListView LinearLayout RelativeLayout TableLayout TabLayout DatePicker TimePicker FormStuff Spinner AutoComplete Gallery GoolgeMapView WebView
do
android create project --target 1  --path ./Hello$var --activity Hello$var --package com.example.Hello$var
done


