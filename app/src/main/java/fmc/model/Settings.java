package fmc.model;


import java.io.Serializable;

public class Settings implements Serializable {
    private String lifeColor = "Red";
    private boolean lifeLines = false;
    private String familyColor = "Red";
    private boolean familyLines = false;
    private String spouseColor = "Red";
    private boolean spouseLines = false;
    private String mapType = "Normal";

    private boolean resync = false;

    public Settings() {}

    public String getLifeColor() {
        return lifeColor;
    }

    public void setLifeColor(String lifeColor) {
        this.lifeColor = lifeColor;
    }

    public String getFamilyColor() {
        return familyColor;
    }

    public void setFamilyColor(String familyColor) {
        this.familyColor = familyColor;
    }

    public String getSpouseColor() {
        return spouseColor;
    }

    public void setSpouseColor(String spouseColor) {
        this.spouseColor = spouseColor;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public boolean isLifeLines() {
        return lifeLines;
    }

    public void setLifeLines(boolean lifeLines) {
        this.lifeLines = lifeLines;
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isResync() {
        return resync;
    }

    public void setResync(boolean resync) {
        this.resync = resync;
    }

    @Override
    public String toString() {
        return mapType;
    }
}
