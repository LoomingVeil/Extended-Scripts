package com.veil.extendedscripts.scripting;

import com.veil.extendedscripts.ExtendedScripts;

import java.util.List;

public interface ScriptGlobalDescriptor {
    final class MemberEntry {
        public final String key;
        public final Object value;
        public final String typeName;
        public final String documentation;

        public MemberEntry(String key, Object value, String typeName, String documentation) {
            if (key.contains(" ")) {
                ExtendedScripts.logger.warning("A member with name \""+key+"\" was attempted to be added to a dynamic global type. Spaces are not allowed!");
                key = key.replace(" ", "_");
            }
            this.key = key;
            this.value = value;
            this.typeName = typeName;
            this.documentation = documentation;
        }
    }

    /**
     * Gets the type name of the value of this object such as number.
     */
    String getTypeName();

    /**
     * The name of the variable usable in the script editor. Should be capitalized.
     */
    String getGlobalName();

    List<MemberEntry> getMembers();

    default Object resolveMemberValue(String memberName) {
        if (memberName == null || memberName.isEmpty()) {
            return null;
        }
        for (MemberEntry entry : getMembers()) {
            if (entry != null && memberName.equals(entry.key)) {
                return entry.value;
            }
        }
        return null;
    }

    default MemberEntry resolveMember(String memberName) {
        if (memberName == null || memberName.isEmpty()) {
            return null;
        }
        for (MemberEntry entry : getMembers()) {
            if (entry != null && memberName.equals(entry.key)) {
                return entry;
            }
        }
        return null;
    }
}
