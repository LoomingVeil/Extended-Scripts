package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAnimationPart;

public class AnimationPart implements AbstractAnimationPart {
    public static final AnimationPart Instance = new AnimationPart();
    public final String HEAD = "HEAD";
    public final String BODY = "BODY";
    public final String RIGHT_ARM = "RIGHT_ARM";
    public final String LEFT_ARM = "LEFT_ARM";
    public final String RIGHT_LEG = "RIGHT_LEG";
    public final String LEFT_LEG = "LEFT_LEG";
    public final String FULL_MODEL = "FULL_MODEL";
}
