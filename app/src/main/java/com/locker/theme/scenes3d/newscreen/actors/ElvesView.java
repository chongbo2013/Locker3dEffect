package com.locker.theme.scenes3d.newscreen.actors;

import com.locker.theme.scenes3d.newscreen.beans.ElvesBean;

/**
 * Spine类型View
 * Created by xff on 2017/12/22.
 */

public class ElvesView extends View<ElvesBean>  {
    public ElvesView(){

    }

    @Override
    protected void load(ElvesBean elvesBean) {
        initAttributes(elvesBean);
    }
}
