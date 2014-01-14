package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.inject.Inject;
import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.inject.ContentViewListener;
import roboguice.inject.RoboInjector;
import roboguice.util.RoboContext;

public abstract class RoboActionBarActivity extends ActionBarActivity implements RoboContext {
    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    @Inject
    ContentViewListener ignored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RoboInjector injector = RoboGuice.getInjector(this);
        injector.injectMembersWithoutViews(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        try {
            RoboGuice.destroyInjector(this);
        } finally {
            super.onDestroy();
        }
    }

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
