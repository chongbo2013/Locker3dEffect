package com.locker.theme.scenes3d.newscreen.tween;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

/**
 *
 * Created by xff on 2017/12/26.
 */

public class SequenceParserTween extends BaseGroupParserTween{



    @Override
    public BaseTween build() {
        Timeline timeline= Timeline.createSequence()
                .beginSequence();
        for(int i=0;i<child.size();i++) {
            BaseParserTween baseParserTween=child.get(i);
            if(baseParserTween instanceof BaseGroupParserTween){
                timeline.push((Timeline) baseParserTween.build());
            }else{
                timeline.push((Tween)baseParserTween.build());
            }
        }
        timeline.end();
        if (reverse.equals("repeat")) {
            timeline.repeat(repeatCount, repeatDelay);
        } else if (reverse.equals("repeatYoyo")) {
            timeline.repeatYoyo(repeatCount, repeatDelay);
        }
        return timeline;
    }
}
