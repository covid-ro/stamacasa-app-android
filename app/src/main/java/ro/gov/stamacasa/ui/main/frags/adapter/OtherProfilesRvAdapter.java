package ro.gov.stamacasa.ui.main.frags.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.ui.main.frags.MenuDialogFragmentForOthers;
import ro.gov.stamacasa.ui.tools.TimestampConverter;

public class OtherProfilesRvAdapter extends RecyclerView.Adapter<OtherProfilesRvAdapter.ViewHolder> {

    private List<UserProfile> users;
    private ClickListener listener;
    private Activity mActivity;

    public OtherProfilesRvAdapter(Activity nActivity, List<UserProfile> users, ClickListener listener) {
        this.listener = listener;
        this.users = users;
        this.mActivity = nActivity;
    }

    public interface ClickListener {
        void onUserClick(UserProfile userProfile);
        void onMenuClick(UserProfile userProfile);
    }

    @NonNull
    @Override
    public OtherProfilesRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OtherProfilesRvAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.other_profile_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OtherProfilesRvAdapter.ViewHolder holder, int position) {
        UserProfile userProfile = users.get(position);
        holder.bindView(userProfile);
        holder.otherProfileCv.setOnClickListener(view -> listener.onUserClick(userProfile));
        holder.menuIv.setOnClickListener(view -> new MenuDialogFragmentForOthers(mActivity, userProfile).show(((FragmentActivity) mActivity).getSupportFragmentManager(), "others"));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView otherProfileCv;
        ImageView menuIv;
        TextView userName;
        TextView userCity;
        TextView lastForm;

        ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userCity = itemView.findViewById(R.id.locationTv);
            otherProfileCv = itemView.findViewById(R.id.otherProfileCv);
            menuIv = itemView.findViewById(R.id.menuIv);
            lastForm = itemView.findViewById(R.id.lastForm);
        }

        void bindView(UserProfile userProfile) {
            userName.setText(userProfile.getName());
            userCity.setText(userProfile.getCity());

            if (userProfile.getLastFormDate() != 0) {
                lastForm.setText(new TimestampConverter().getDayYearTime(userProfile.getLastFormDate()));
            } else {
                lastForm.setText("-");
            }
        }
    }
}
