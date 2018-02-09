package com.example.mohamedabdelaziz.baking;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mohamed Abd ELaziz on 6/19/2017.
 */

public class mywidgetservice extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new mywidgetfactory(getApplicationContext());
    }
}
