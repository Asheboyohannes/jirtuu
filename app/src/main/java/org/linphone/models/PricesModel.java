package org.linphone.models;

public class PricesModel {

    String Key;
    String Value;
    String TvMessage;
    String PriceUnit;

    public void PricesModel() {}

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTvMessage() {
        return TvMessage;
    }

    public void setTvMessage(String tvMessage) {
        TvMessage = tvMessage;
    }

    public String getPriceUnit() {
        return PriceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        PriceUnit = priceUnit;
    }
}
