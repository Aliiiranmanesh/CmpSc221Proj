package Owner;

import Building.Property;

public class LandLord {
    private String name;
    private Property[] Properties;

    public LandLord(String name) {
        this.name = name;
    }

    public LandLord(String name, Property[] Properties) {
        this.name = name;
        this.Properties = Properties;
    }

    public LandLord() {
        this.name = "";
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
}
