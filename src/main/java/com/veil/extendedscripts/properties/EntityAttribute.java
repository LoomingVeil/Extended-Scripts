package com.veil.extendedscripts.properties;

public enum EntityAttribute {
    GRAVITY(Float.class, 1F),
    DOWNWARD_GRAVITY(Float.class, -1F),
    UPWARD_GRAVITY(Float.class, -1F),
    UNDERWATER_GRAVITY(Float.class, 1F),
    UNDERWATER_DOWNWARD_GRAVITY(Float.class, -1F),
    UNDERWATER_UPWARD_GRAVITY(Float.class, -1F),
    JUMP_POWER_VERTICAL(Float.class, 1F),
    JUMP_POWER_HORIZONTAL(Float.class, 1F),
    MAX_FALL_DISTANCE(Float.class, 3F),
    CAN_MOVE(Boolean.class, true);

    private final Class<?> type;
    private final Object defaultValue;

    EntityAttribute(Class<?> type, Object defaultValue) {
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
