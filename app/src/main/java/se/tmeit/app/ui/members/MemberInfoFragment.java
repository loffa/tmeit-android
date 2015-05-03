package se.tmeit.app.ui.members;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.tmeit.app.R;
import se.tmeit.app.model.Member;
import se.tmeit.app.ui.MainActivity;

/**
 * Fragment for an individual member.
 */
public final class MemberInfoFragment extends Fragment implements MainActivity.HasTitle, MainActivity.HasMenu, View.OnClickListener {
    private final static String TAG = MemberInfoFragment.class.getSimpleName();
    private MemberFaceHelper mFaceHelper;

    public static MemberInfoFragment createInstance(Context context, Member member, Member.RepositoryData memberRepository) {
        Bundle bundle = new Bundle();
        bundle.putString(Member.Keys.USERNAME, member.getUsername());
        bundle.putString(Member.Keys.REAL_NAME, member.getRealName());
        bundle.putString(Member.Keys.TITLE_TEXT, member.getTitleText(context, memberRepository));
        bundle.putString(Member.Keys.TEAM_TEXT, member.getTeamText(context, memberRepository));
        bundle.putString(Member.Keys.PHONE, member.getPhone());
        bundle.putString(Member.Keys.EMAIL, member.getEmail());
        bundle.putStringArrayList(Member.Keys.FACES, new ArrayList<>(member.getFaces()));
        bundle.putString(Member.Keys.FLAGS, getDescriptionOfFlags(context, member));
        bundle.putString(Member.Keys.DATE_PRAO, member.getDatePrao());
        bundle.putString(Member.Keys.DATE_MARSKALK, member.getDateMarskalk());
        bundle.putString(Member.Keys.DATE_VRAQ, member.getDateVraq());

        MemberInfoFragment instance = new MemberInfoFragment();
        instance.setArguments(bundle);
        return instance;
    }

    public static String getDescriptionOfFlags(Context context, Member member) {
        List<String> strings = new ArrayList<>();
        if (member.hasFlag(Member.Flags.HAS_STAD)) {
            strings.add(context.getString(R.string.member_stad));
        }
        if (member.hasFlag(Member.Flags.HAS_FEST)) {
            strings.add(context.getString(R.string.member_fest));
        }
        if (member.hasFlag(Member.Flags.ON_PERMIT)) {
            strings.add(context.getString(R.string.member_on_permit));
        }
        if (member.hasFlag(Member.Flags.DRIVERS_LICENSE)) {
            strings.add(context.getString(R.string.member_drivers_license));
        }

        return TextUtils.join(", ", strings);
    }

    @Override
    public int getMenu() {
        return R.menu.menu_member_info;
    }

    @Override
    public int getTitle() {
        return R.string.member_nav_title;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFaceHelper = MemberFaceHelper.getInstance(activity);
    }

    @Override
    public void onClick(View v) {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;

            Bundle args = getArguments();
            List<String> faces = args.getStringArrayList(Member.Keys.FACES);
            Fragment memberImagesFragment = MemberImagesFragment.createInstance(faces);
            mainActivity.openFragment(memberImagesFragment, true);
        } else {
            Log.e(TAG, "Activity holding fragment is not MainActivity!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_info, container, false);

        Bundle args = getArguments();
        TextView realNameText = (TextView) view.findViewById(R.id.member_real_name);
        realNameText.setText(args.getString(Member.Keys.REAL_NAME));

        TextView usernameText = (TextView) view.findViewById(R.id.member_username);
        usernameText.setText(args.getString(Member.Keys.USERNAME));

        TextView titleText = (TextView) view.findViewById(R.id.member_title);
        setTextWithPrefix(titleText, R.string.member_title, args.getString(Member.Keys.TITLE_TEXT));

        TextView teamText = (TextView) view.findViewById(R.id.member_team);
        setTextWithPrefix(teamText, R.string.member_team, args.getString(Member.Keys.TEAM_TEXT));

        ImageView imageView = (ImageView) view.findViewById(R.id.member_face);
        List<String> faces = args.getStringArrayList(Member.Keys.FACES);
        if (!faces.isEmpty()) {
            mFaceHelper.picasso(faces)
                    .placeholder(R.drawable.member_placeholder)
                    .into(imageView);
            imageView.setOnClickListener(this);
        } else {
            imageView.setImageResource(R.drawable.member_placeholder);
        }

        final String email = args.getString(Member.Keys.EMAIL);
        Button emailButton = (Button) view.findViewById(R.id.member_button_email);
        if (!TextUtils.isEmpty(email)) {
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberActions.sendEmailTo(email, MemberInfoFragment.this);
                }
            });
            emailButton.setEnabled(true);
        } else {
            emailButton.setEnabled(false);
        }

        final String phoneNo = args.getString(Member.Keys.PHONE);
        Button smsButton = (Button) view.findViewById(R.id.member_button_message);
        Button callButton = (Button) view.findViewById(R.id.member_button_call);

        if (!TextUtils.isEmpty(phoneNo)) {
            smsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberActions.sendSmsTo(phoneNo, MemberInfoFragment.this);
                }
            });
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberActions.makeCallTo(phoneNo, MemberInfoFragment.this);
                }
            });

            smsButton.setEnabled(true);
            callButton.setEnabled(true);
        } else {
            smsButton.setEnabled(false);
            callButton.setEnabled(false);
        }

        setTextWithPrefixIfNotEmpty(view, R.id.member_flags, R.string.member_info, args.getString(Member.Keys.FLAGS));
        setTextWithPrefixIfNotEmpty(view, R.id.member_prao, R.string.member_prao, args.getString(Member.Keys.DATE_PRAO));
        setTextWithPrefixIfNotEmpty(view, R.id.member_marskalk, R.string.member_marskalk, args.getString(Member.Keys.DATE_MARSKALK));
        setTextWithPrefixIfNotEmpty(view, R.id.member_vraq, R.string.member_vraq, args.getString(Member.Keys.DATE_VRAQ));

        return view;
    }

    @Override
    public boolean onMenuItemSelected(MenuItem item) {
        if (R.id.member_action_add_contact == item.getItemId()) {
            Bundle args = getArguments();
            String realName = args.getString(Member.Keys.REAL_NAME);
            String email = args.getString(Member.Keys.EMAIL);
            String phoneNo = args.getString(Member.Keys.PHONE);
            boolean succeeded = MemberActions.addAsContact(realName, phoneNo, email, getActivity().getContentResolver());

            Toast toast = Toast.makeText(getActivity(),
                    (succeeded ? R.string.member_contact_saved : R.string.member_contact_could_not_saved),
                    (succeeded ? Toast.LENGTH_LONG : Toast.LENGTH_LONG));
            toast.show();
            return true;
        }

        return false;
    }

    private void setTextWithPrefix(TextView textView, int prefixResId, String str) {
        String prefixStr = getString(prefixResId);
        SpannableString teamStr = new SpannableString(prefixStr + " " + str);
        teamStr.setSpan(new RelativeSizeSpan(0.8f), 0, prefixStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        teamStr.setSpan(new ForegroundColorSpan(R.color.color_insektionen), 0, prefixStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(teamStr);
    }

    private void setTextWithPrefixIfNotEmpty(View view, int textViewId, int prefixResId, String str) {
        TextView textView = (TextView) view.findViewById(textViewId);

        if (!TextUtils.isEmpty(str)) {
            setTextWithPrefix(textView, prefixResId, str);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}