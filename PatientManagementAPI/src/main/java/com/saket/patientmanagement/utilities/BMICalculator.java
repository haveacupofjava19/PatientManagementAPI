package com.saket.patientmanagement.utilities;

import java.util.List;

import com.saket.patientmanagement.entities.ClinicalData;

public class BMICalculator {
	
	public static void calculateBMI(List<ClinicalData> clinicalDataList, ClinicalData data) {
		if (data.getComponentName().equals("hw")) {
			String[] hw = data.getComponentValue().split("/");
			if (hw != null && hw.length > 1) {
				float hwInMetres = Float.parseFloat(hw[0]) * 0.4536F;
				float BMI = Float.parseFloat(hw[1]) / (hwInMetres * hwInMetres);
				ClinicalData bmiData = new ClinicalData();
				bmiData.setComponentName("bmi");
				bmiData.setComponentValue(Float.toString(BMI));
				clinicalDataList.add(bmiData);
			}
		}
	}

}
