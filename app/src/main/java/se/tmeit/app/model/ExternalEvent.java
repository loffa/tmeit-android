package se.tmeit.app.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Model object for external events.
 */
public final class ExternalEvent {
    private List<ExternalEventAttendee> mAttendees;
    private String mBody;
    private int mId;
    private boolean mIsAttending;
    private boolean mIsNearSignup;
    private boolean mIsPast;
    private boolean mIsPastSignup;
    private String mLastSignupDate;
    private int mNumberOfAttendees;
    private String mStartDate;
    private String mTitle;

    public static ExternalEvent fromJson(JSONObject obj) throws JSONException {
        ExternalEvent event = new ExternalEvent();
        event.setBody(obj.optString(Keys.BODY));
        event.setId(obj.getInt(Keys.ID));
        event.setIsAttending(obj.optBoolean(Keys.IS_ATTENDING));
        event.setIsNearSignup(obj.getBoolean(Keys.IS_NEAR_SIGNUP));
        event.setIsPast(obj.getBoolean(Keys.IS_PAST));
        event.setIsPastSignup(obj.getBoolean(Keys.IS_PAST_SIGNUP));
        event.setLastSignupDate(obj.getString(Keys.LAST_SIGNUP));
        event.setNumberOfAttendees(obj.getInt(Keys.ATTENDEES));
        event.setStartDate(obj.getString(Keys.START_DATE));
        event.setTitle(obj.getString(Keys.TITLE));

        JSONArray attendees = obj.optJSONArray(Keys.ATTENDEES);
        if (null != attendees) {
            event.setAttendees(ExternalEventAttendee.fromJsonArray(attendees));
            event.setNumberOfAttendees(event.getAttendees().size());
        } else {
            event.setNumberOfAttendees(obj.getInt(Keys.ATTENDEES));
        }

        return event;
    }

    public List<ExternalEventAttendee> getAttendees() {
        return mAttendees;
    }

    public void setAttendees(List<ExternalEventAttendee> attendees) {
        mAttendees = attendees;
    }

    public String getBody() {
        return mBody;
    }

    private void setBody(String body) {
        mBody = body;
    }

    public int getId() {
        return mId;
    }

    private void setId(int id) {
        mId = id;
    }

    public String getLastSignupDate() {
        return mLastSignupDate;
    }

    private void setLastSignupDate(String date) {
        mLastSignupDate = date;
    }

    public int getNumberOfAttendees() {
        return mNumberOfAttendees;
    }

    private void setNumberOfAttendees(int value) {
        mNumberOfAttendees = value;
    }

    public String getStartDate() {
        return mStartDate;
    }

    private void setStartDate(String date) {
        mStartDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        mTitle = title;
    }

    public boolean isAttending() {
        return mIsAttending;
    }

    public boolean isNearSignup() {
        return mIsNearSignup;
    }

    public boolean isPast() {
        return mIsPast;
    }

    public boolean isPastSignup() {
        return mIsPastSignup;
    }

    private void setIsAttending(boolean value) {
        mIsAttending = value;
    }

    private void setIsNearSignup(boolean value) {
        mIsNearSignup = value;
    }

    private void setIsPast(boolean value) {
        mIsPast = value;
    }

    private void setIsPastSignup(boolean value) {
        mIsPastSignup = value;
    }

    public static class Keys {
        public static final String ATTENDEES = "attendees";
        public static final String BODY = "body";
        public static final String ID = "id";
        public static final String IS_ATTENDING = "is_attending";
        public static final String IS_NEAR_SIGNUP = "is_near_signup";
        public static final String IS_PAST = "is_past";
        public static final String IS_PAST_SIGNUP = "is_past_signup";
        public static final String LAST_SIGNUP = "last_signup";
        public static final String START_DATE = "start_date";
        public static final String TITLE = "title";

        private Keys() {
        }
    }

    public static class RepositoryData {
        private final ExternalEvent mExternalEvent;
        private final ExternalEventAttendee mBlankAttendee;

        public RepositoryData(ExternalEvent externalEvent, ExternalEventAttendee blankAttendee) {
            mExternalEvent = externalEvent;
            mBlankAttendee = blankAttendee;
        }

        public ExternalEvent getExternalEvent() {
            return mExternalEvent;
        }

        public ExternalEventAttendee getBlankAttendee() {
            return mBlankAttendee;
        }
    }
}
