package com.prac.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Spinner conversionTypeSpinner, sourceUnitSpinner, destinationUnitSpinner;
    private Button convertButton;
    private EditText valueEditText;
    private TextView resultTextView;

    // Conversion type options and unit arrays
    private String[] conversionTypes = {"Length", "Weight", "Temperature"};
    private String[] lengthUnits = {"Inches", "Feet", "Yards", "Miles", "Centimeters"};
    private String[] weightUnits = {"Pounds", "Ounces", "Tons","Grams", "Kilograms" };
    private String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};
    private ArrayAdapter<String> unitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        conversionTypeSpinner = findViewById(R.id.conversionTypeSpinner);
        sourceUnitSpinner = findViewById(R.id.sourceUnitSpinner);
        destinationUnitSpinner = findViewById(R.id.destinationUnitSpinner);
        valueEditText = findViewById(R.id.valueEditText);
        resultTextView = findViewById(R.id.resultTextView);
        convertButton = findViewById(R.id.convertButton);

        // Set up conversion type spinner
        ArrayAdapter<String> conversionTypeAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, conversionTypes);
        conversionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conversionTypeSpinner.setAdapter(conversionTypeAdapter);

        // Set initial spinners for Length conversion
        updateUnitSpinners("Length");

        // Listen for conversion type changes
        conversionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = conversionTypes[position];
                updateUnitSpinners(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Set up convert button click listener
        convertButton.setOnClickListener(v -> {
            String inputValue = valueEditText.getText().toString();
            if (!inputValue.isEmpty()) {
                double value = Double.parseDouble(inputValue);
                double result = convertUnits(value);
                resultTextView.setText(String.valueOf(result));
            }
        });
    }

    /**
     * Update the source and destination spinners based on the conversion type.
     */
    private void updateUnitSpinners(String conversionType) {
        ArrayList<String> unitsList = new ArrayList<>();
        switch (conversionType) {
            case "Length":
                unitsList.addAll(Arrays.asList(lengthUnits));
                break;
            case "Weight":
                unitsList.addAll(Arrays.asList(weightUnits));
                break;
            case "Temperature":
                unitsList.addAll(Arrays.asList(temperatureUnits));
                break;
        }
        unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitsList);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceUnitSpinner.setAdapter(unitAdapter);
        destinationUnitSpinner.setAdapter(unitAdapter);
    }

    /**
     * Convert the input value using the current conversion type and selected units.
     */
    private double convertUnits(double value) {
        String conversionType = conversionTypeSpinner.getSelectedItem().toString();
        String sourceUnit = sourceUnitSpinner.getSelectedItem().toString();
        String destinationUnit = destinationUnitSpinner.getSelectedItem().toString();

        if (conversionType.equals("Length")) {
            return convertLength(value, sourceUnit, destinationUnit);
        } else if (conversionType.equals("Weight")) {
            return convertWeight(value, sourceUnit, destinationUnit);
        } else { // Temperature
            return convertTemperature(value, sourceUnit, destinationUnit);
        }
    }

    /**
     * Convert length values.
     * We convert the source value to centimeters (base unit) and then to the destination unit.
     *
     * Conversion factors:
     * - 1 inch = 2.54 cm
     * - 1 foot = 30.48 cm
     * - 1 yard = 91.44 cm
     * - 1 mile = 1.60934 km = 160934 cm (approx)
     */
    private double convertLength(double value, String sourceUnit, String destinationUnit) {
        // Convert source value to centimeters
        double valueInCm;
        switch (sourceUnit) {
            case "Inches":
                valueInCm = value * 2.54;
                break;
            case "Feet":
                valueInCm = value * 30.48;
                break;
            case "Yards":
                valueInCm = value * 91.44;
                break;
            case "Miles":
                valueInCm = value * 160934;
                break;
            case "Centimeters":
                valueInCm = value;
                break;
            default:
                valueInCm = value;
        }

        // Convert from centimeters to destination unit
        switch (destinationUnit) {
            case "Inches":
                return valueInCm / 2.54;
            case "Feet":
                return valueInCm / 30.48;
            case "Yards":
                return valueInCm / 91.44;
            case "Miles":
                return valueInCm / 160934;
            case "Centimeters":
                return valueInCm;
            default:
                return valueInCm;
        }
    }
    /**
     * Convert weight values.
     * We convert the source value to kilograms (base unit) and then to the destination unit.
     *
     * Conversion factors:
     * - 1 pound = 0.453592 kg
     * - 1 ounce = 28.3495 g = 0.0283495 kg
     * - 1 ton = 907.185 kg
     * - 1 kilogram = 1000 grams
     */
    private double convertWeight(double value, String sourceUnit, String destinationUnit) {
        // Convert source value to kilograms
        double valueInKg;
        switch (sourceUnit) {
            case "Pounds":
                valueInKg = value * 0.453592;
                break;
            case "Ounces":
                valueInKg = value * 0.0283495;
                break;
            case "Tons":
                valueInKg = value * 907.185;
                break;
            case "Grams":
                valueInKg = value / 1000; // Convert grams to kilograms
                break;
            case "Kilograms":
            default:
                valueInKg = value;
        }

        // Convert from kilograms to destination unit
        switch (destinationUnit) {
            case "Pounds":
                return valueInKg / 0.453592;
            case "Ounces":
                return valueInKg / 0.0283495;
            case "Tons":
                return valueInKg / 907.185;
            case "Grams":
                return valueInKg * 1000; // Convert kilograms to grams
            case "Kilograms":
            default:
                return valueInKg;
        }
    }


    /**
     * Convert temperature values.
     * Uses direct formulas:
     * - Celsius to Fahrenheit: F = (C * 1.8) + 32
     * - Fahrenheit to Celsius: C = (F - 32) / 1.8
     * - Celsius to Kelvin: K = C + 273.15
     * - Kelvin to Celsius: C = K - 273.15
     * - Fahrenheit to Kelvin: ((F - 32) / 1.8) + 273.15
     * - Kelvin to Fahrenheit: ((K - 273.15) * 1.8) + 32
     */
    private double convertTemperature(double value, String sourceUnit, String destinationUnit) {
        // If same, no conversion needed
        if (sourceUnit.equals(destinationUnit)) {
            return value;
        }
        // Convert based on source and destination
        if (sourceUnit.equals("Celsius")) {
            if (destinationUnit.equals("Fahrenheit")) {
                return (value * 1.8) + 32;
            } else if (destinationUnit.equals("Kelvin")) {
                return value + 273.15;
            }
        } else if (sourceUnit.equals("Fahrenheit")) {
            if (destinationUnit.equals("Celsius")) {
                return (value - 32) / 1.8;
            } else if (destinationUnit.equals("Kelvin")) {
                return ((value - 32) / 1.8) + 273.15;
            }
        } else if (sourceUnit.equals("Kelvin")) {
            if (destinationUnit.equals("Celsius")) {
                return value - 273.15;
            } else if (destinationUnit.equals("Fahrenheit")) {
                return ((value - 273.15) * 1.8) + 32;
            }
        }
        return value;
    }
}
