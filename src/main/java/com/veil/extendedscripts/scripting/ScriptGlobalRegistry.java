package com.veil.extendedscripts.scripting;

import noppes.npcs.client.gui.util.script.interpreter.type.TypeInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ScriptGlobalRegistry {
    private static final List<ScriptGlobalDescriptor> DESCRIPTORS = new CopyOnWriteArrayList<ScriptGlobalDescriptor>();
    private static final Map<String, DynamicScriptGlobalObject> RUNTIME_OBJECTS = new ConcurrentHashMap<String, DynamicScriptGlobalObject>();

    private ScriptGlobalRegistry() {}

    public static void register(ScriptGlobalDescriptor descriptor) {
        if (descriptor == null) {
            return;
        }
        String globalName = descriptor.getGlobalName();
        if (globalName == null || globalName.trim().isEmpty()) {
            return;
        }

        synchronized (DESCRIPTORS) {
            for (int i = 0; i < DESCRIPTORS.size(); i++) {
                ScriptGlobalDescriptor existing = DESCRIPTORS.get(i);
                if (existing != null && globalName.equals(existing.getGlobalName())) {
                    DESCRIPTORS.set(i, descriptor);
                    RUNTIME_OBJECTS.remove(globalName);
                    return;
                }
            }
            DESCRIPTORS.add(descriptor);
        }
    }

    public static List<ScriptGlobalDescriptor> getDescriptors() {
        return Collections.unmodifiableList(new ArrayList<>(DESCRIPTORS));
    }

    public static ScriptGlobalDescriptor findByName(String globalName) {
        if (globalName == null || globalName.isEmpty()) {
            return null;
        }

        for (ScriptGlobalDescriptor descriptor : DESCRIPTORS) {
            if (descriptor != null && globalName.equals(descriptor.getGlobalName())) {
                return descriptor;
            }
        }
        return null;
    }

    public static DynamicScriptGlobalObject getOrCreateRuntimeObject(ScriptGlobalDescriptor descriptor) {
        if (descriptor == null || descriptor.getGlobalName() == null) {
            return null;
        }
        return RUNTIME_OBJECTS.computeIfAbsent(descriptor.getGlobalName(), key -> new DynamicScriptGlobalObject(descriptor));
    }

    public static String toIdentifier(String text) {
        StringBuilder out = new StringBuilder(text.length());
        boolean lastUnderscore = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            boolean allowed = Character.isLetterOrDigit(c) || c == '_' || c == '$';

            if (allowed) {
                out.append(c);
                lastUnderscore = false;
            } else if (!lastUnderscore) {
                out.append('_');
                lastUnderscore = true;
            }
        }

        int start = 0;
        int end = out.length();
        while (start < end && out.charAt(start) == '_') {
            start++;
        }
        while (end > start && out.charAt(end - 1) == '_') {
            end--;
        }

        return start >= end ? "" : out.substring(start, end);
    }

    public static TypeInfo toTypeInfo(String typeName) {
        String normalized = typeName.trim().toLowerCase();
        if ("number".equals(normalized) || "int".equals(normalized) || "integer".equals(normalized) || "double".equals(normalized) || "float".equals(normalized) || "long".equals(normalized) || "short".equals(normalized) || "byte".equals(normalized)) {
            return TypeInfo.NUMBER;
        }

        if ("string".equals(normalized)) {
            return TypeInfo.STRING;
        }

        if ("boolean".equals(normalized) || "bool".equals(normalized)) {
            return TypeInfo.BOOLEAN;
        }

        return TypeInfo.ANY;
    }
}
