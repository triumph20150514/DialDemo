package com.trimph.dialdemo;

import android.view.animation.LinearInterpolator;

/**
 * author: Trimph
 * data: 2016/12/24.
 * description: 原文 http://inloop.github.io/interpolator/
 */

public class SpringInterpolator extends LinearInterpolator {

    private float factor;

    public SpringInterpolator() {
        this.factor = 2f;
    }

    @Override
    public float getInterpolation(float input) {
//        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        if (input < 0.3) {
            return (float) (Math.pow(2, -10 * input) * Math.tan((input - factor / 5) * (2 * Math.PI) / factor) + 1);
        } else if (input > 0.6) {
            return input;
        } else {
            return 0;
        }
    }
}
