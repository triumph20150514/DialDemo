package com.trimph.dialdemo;

import android.view.animation.Interpolator;

/**
 *
 * author: Trimph
 * data: 2016/12/24.
 * description:
 */

public class SmoothInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return input * input * (3 - 2 * input);
    }
}
