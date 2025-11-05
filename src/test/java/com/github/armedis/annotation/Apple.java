package com.github.armedis.annotation;

public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

    @FruitProvider(id = 1, name = "HomePlus", address = "Seoul")
    private String appleProvider;

    /**
     * @return the appleName
     */
    public String getAppleName() {
        return appleName;
    }

    /**
     * @param appleName the appleName to set
     */
    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    /**
     * @return the appleColor
     */
    public String getAppleColor() {
        return appleColor;
    }

    /**
     * @param appleColor the appleColor to set
     */
    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    /**
     * @return the appleProvider
     */
    public String getAppleProvider() {
        return appleProvider;
    }

    /**
     * @param appleProvider the appleProvider to set
     */
    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }

}