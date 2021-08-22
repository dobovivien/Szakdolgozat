package com.example.koltsegvetes_tervezo;

public class CustomItem {

    private String SpinnerItemName;
    private int SpinnerItemImage;

    public CustomItem(String spinnerItemName, int spinnerItemImage) {
        SpinnerItemName = spinnerItemName;
        SpinnerItemImage = spinnerItemImage;
    }

    public String getSpinnerItemName() {
        return SpinnerItemName;
    }

    public int getSpinnerItemImage() {
        return SpinnerItemImage;
    }
}
