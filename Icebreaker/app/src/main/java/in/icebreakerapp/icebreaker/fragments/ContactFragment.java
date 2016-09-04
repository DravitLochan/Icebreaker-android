package in.icebreakerapp.icebreaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import in.icebreakerapp.icebreaker.ChatActivity;
import in.icebreakerapp.icebreaker.R;
import in.icebreakerapp.icebreaker.adapters.ContactsAdapter;
import in.icebreakerapp.icebreaker.adapters.HomeChatAdapter;
import in.icebreakerapp.icebreaker.helpers.MessageDb;

/**
 * Created by anip on 05/09/16.
 */
public class ContactFragment extends Fragment {
    public RecyclerView recyclerView;
    private MessageDb db;
    public static ContactsAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ContactFragment(){

    }
    public static ContactFragment newInstance(){
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("hell","entered fragment");
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_view);
        db =new MessageDb(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ContactsAdapter(getActivity(),db.getContact());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == getActivity().RESULT_OK) {
            adapter.notifyDataSetChanged();
            Log.i("hell","enterd on fragn");

        }
    }
}
