package com.veil.extendedscripts.scripting;

import jdk.nashorn.api.scripting.AbstractJSObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This is a JavaScript object that is put into the script engine that has a variable amount of members.
 */
public class DynamicScriptGlobalObject extends AbstractJSObject {
    private final ScriptGlobalDescriptor descriptor;

    public DynamicScriptGlobalObject(ScriptGlobalDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public Object getMember(String name) {
        Object value = descriptor.resolveMemberValue(name);
        return value != null ? value : super.getMember(name);
    }

    @Override
    public boolean hasMember(String name) {
        return descriptor.resolveMemberValue(name) != null;
    }

    @Override
    public Set<String> keySet() {
        LinkedHashSet<String> keys = new LinkedHashSet<String>();
        for (ScriptGlobalDescriptor.MemberEntry member : descriptor.getMembers()) {
            if (member != null && member.key != null && !member.key.isEmpty()) {
                keys.add(member.key);
            }
        }
        return keys;
    }

    @Override
    public String getClassName() {
        return descriptor.getGlobalName();
    }

    @Override
    public String toString() {
        return "[object " + descriptor.getGlobalName() + "]";
    }
}
