package com.veil.extendedscripts.constants;

import com.veil.extendedscripts.extendedapi.constants.AbstractAnimationPart;

public class AnimationPart implements AbstractAnimationPart {
    public static final AnimationPart Instance = new AnimationPart();
    public final int HEAD = 0;
    public final int BODY = 1;
    public final int RIGHT_ARM = 2;
    public final int LEFT_ARM = 3;
    public final int RIGHT_LEFT = 4;
    public final int LEFT_LEG = 5;
    public final int FULL_MODEL = 6;
}
