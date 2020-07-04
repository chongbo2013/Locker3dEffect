package com.locker.theme.scenes3d.newscreen.tween;

import com.locker.theme.scenes3d.ScriptUtils;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;

/**
 * Created by xff on 2017/12/26.
 */

public class ColorParserTween extends BaseParserTween{

    public BaseTween build() {
        BaseTween baseTween = null;
        if (fromTo.equals("to"))
            baseTween = Tween.to(target, type, duration).target(ScriptUtils.getValue(targetValues[0], Float.class),ScriptUtils.getValue(targetValues[1], Float.class),ScriptUtils.getValue(targetValues[2], Float.class)).delay(delay).ease(equation);
        else
            baseTween = Tween.from(target, type, duration).target(ScriptUtils.getValue(targetValues[0], Float.class),ScriptUtils.getValue(targetValues[1], Float.class),ScriptUtils.getValue(targetValues[2], Float.class)).delay(delay).ease(equation);


        if (reverse.equals("repeat")) {
            baseTween.repeat(repeatCount, repeatDelay);
        } else if (reverse.equals("repeatYoyo")) {
            baseTween.repeatYoyo(repeatCount, repeatDelay);
        }
        return baseTween;
    }
}
