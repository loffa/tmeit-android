package se.tmeit.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.widget.ListAdapter;
import android.widget.Toast;

import se.tmeit.app.services.Repository;
import se.tmeit.app.storage.Preferences;

/**
 * Base class containing some shared functionality for list fragments that load from the repository.
 */
public abstract class ListFragmentBase extends ListFragment implements MainActivity.HasTitle {
    private final Handler mHandler = new Handler();
    private boolean mIsLoaded;
    private Parcelable mListState;
    private Preferences mPrefs;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(getStateKey());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPrefs = new Preferences(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mIsLoaded) {
            String username = mPrefs.getAuthenticatedUser(), serviceAuth = mPrefs.getServiceAuthentication();
            getDataFromRepository(new Repository(username, serviceAuth));
        } else {
            initializeList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (null != getView()) {
            outState.putParcelable(getStateKey(), getListView().onSaveInstanceState());
        }
    }

    protected void finishInitializeList(ListAdapter adapter) {
        setListAdapter(adapter);

        if (null != mListState) {
            getListView().onRestoreInstanceState(mListState);
            mListState = null;
        }
    }

    /**
     * Override this to call the appropriate method on the repository.
     */
    protected abstract void getDataFromRepository(Repository repository);

    protected Preferences getPreferences() {
        return mPrefs;
    }

    /**
     * Override this to return a unique string key for this fragment.
     */
    protected abstract String getStateKey();

    /**
     * Override this to create your list adapter. You must call finishInitializeList from this method.
     */
    protected abstract void initializeList();

    /**
     * Call this from the error handler of your repository result handler.
     */
    protected void onRepositoryError(final int errorMessage) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();
                if (null != activity && isVisible()) {
                    initializeList();
                    Toast toast = Toast.makeText(activity, getString(errorMessage), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    /**
     * Call this method from the success handler of your repository result handler.
     */
    protected void onRepositorySuccess() {
        mIsLoaded = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != getActivity() && isVisible()) {
                    initializeList();
                }
            }
        });
    }

    protected void saveInstanceState() {
        mListState = getListView().onSaveInstanceState();
    }
}