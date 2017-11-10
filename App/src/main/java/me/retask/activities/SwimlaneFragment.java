package me.retask.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import me.retask.R;
import me.retask.dal.RetaskContract;

public class SwimlaneFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static String ARG_STATUS = "status";

    private OnTaskThumbnailClickListener onTaskThumbnailClickListener;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(!(activity instanceof OnTaskThumbnailClickListener)) {
            throw new ClassCastException("Hosting activity supposed to implement OnTaskThumbnailClickListener");
        }

        onTaskThumbnailClickListener = (OnTaskThumbnailClickListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View swimlaneView = inflater.inflate(R.layout.swimlane_view, container, false);
        getLoaderManager().initLoader(0, null, this);

        cursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.task_thumbnail_view,
                null,
                new String[] { RetaskContract.Task.DESCRIPTION },
                new int[] { R.id.taskReprTextView },
                0);

        ListView tasksListView = (ListView)swimlaneView.findViewById(R.id.taskThumbailsListView);
        tasksListView.setAdapter(cursorAdapter);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                onTaskThumbnailClickListener.onTaskThumbnailClick(id);
            }
        });

        return swimlaneView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                RetaskContract.Task.CONTENT_URI,
                new String[] { RetaskContract.Task._ID, RetaskContract.Task.DESCRIPTION },
                RetaskContract.Task.STATUS + "=?",
                new String[] { Integer.toString(getArguments().getInt(ARG_STATUS)) },
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int loaderId = cursorLoader.getId();
        if(loaderId != 0) {
            throw new IllegalArgumentException("Unknown loader id " + Integer.toString(loaderId));
        }

        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        int loaderId = cursorLoader.getId();
        if(loaderId != 0) {
            throw new IllegalArgumentException("Unknown loader id " + Integer.toString(loaderId));
        }

        cursorAdapter.swapCursor(null);
    }

    public static interface OnTaskThumbnailClickListener {
        void onTaskThumbnailClick(long taskId);
    }
}
