package Owner;

import Building.Property;

public class LandLord {
    private String name;
    private Property[] Properties;
    private String phoneNum;

    public LandLord(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public LandLord(String name, String phoneNum, Property[] Properties) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.Properties = Properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property[] getProperties() {
        return Properties;
    }

    public void setProperties(Property[] properties) {
        Properties = properties;
    }

    public void addProperty(Property property) {
        Property[] temp = new Property[Properties.length + 1];
        for (int i = 0; i < Properties.length; i++) {
            temp[i] = Properties[i];
        }
        temp[Properties.length] = property;
        Properties = temp;
    }

    public void removeProperty(Property property) {
        Property[] temp = new Property[Properties.length - 1];
        for (int i = 0, j = 0; i < Properties.length; i++) {
            if (Properties[i] != property) {
                temp[j] = Properties[i];
                j++;
            }
        }
        Properties = temp;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
