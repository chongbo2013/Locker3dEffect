package com.locker.theme.scenes3d.newscreen.tween;

import java.util.ArrayList;
import java.util.List;

/**动态创建动画
 * Created by xff on 2017/12/26.
 */

public class BaseGroupParserTween extends BaseParserTween{
    public List<BaseParserTween> child=new ArrayList<>();



    public BaseGroupParserTween push(BaseParserTween tween) {
            child.add(tween);
            return this;
    }
}
