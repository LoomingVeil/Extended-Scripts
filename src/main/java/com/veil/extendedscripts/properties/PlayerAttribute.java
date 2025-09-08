package com.veil.extendedscripts.properties;

/**
 * Attributes exclusive to players
 */
public enum PlayerAttribute {
    FLIGHT_SPEED_HORIZONTAL(Float.class, 1F),
    FLIGHT_SPEED_VERTICAL(Float.class, 1F),
    SWIM_BOOST_WATER(Float.class, 1F),
    SWIM_BOOST_LAVA(Float.class, 1F),
    ARMOR_VALUE(Float.class, 1F),
    CAN_FLY(Boolean.class, false),
    LAST_SEEN_FLYING(Boolean.class, false),
    KEEP_INVENTORY(Boolean.class, false);

    private final Class<?> type;
    private final Object defaultValue;

    PlayerAttribute(Class<?> type, Object defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String asCamelCase() {
        String[] parts = this.name().split("_");
        StringBuilder sb = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            sb.append(parts[i].substring(0, 1).toUpperCase())
                .append(parts[i].substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public String asSnakeCase() {
        return this.name().toLowerCase();
    }

    private String toScreamingSnakeCase(String camelCase) {
        String newString = "";
        for (int i = 0; i < camelCase.length(); i++) {
            String upperChar = (camelCase.charAt(i)+"").toUpperCase();
            if ((camelCase.charAt(i)+"").equals(upperChar)) {
                newString += (camelCase.charAt(i)+"_").toUpperCase();
            } else {
                newString += upperChar;
            }
        }
        return newString;
    }

    public EntityAttribute valueOfFromCamelCaseKey(String key) {
        key = toScreamingSnakeCase(key);
        return EntityAttribute.valueOf(key);
    }
}
