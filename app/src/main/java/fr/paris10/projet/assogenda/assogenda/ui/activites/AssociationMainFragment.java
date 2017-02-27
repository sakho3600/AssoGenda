package fr.paris10.projet.assogenda.assogenda.ui.activites;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import fr.paris10.projet.assogenda.assogenda.R;
import fr.paris10.projet.assogenda.assogenda.model.Association;

/**
 * A simple {@link Fragment} subclass.
 * Create an instance of this fragment.
 */
public class AssociationMainFragment extends Fragment implements View.OnClickListener {

    public OnFragmentInteractionListener mListener;
    public CustomAssociationAdapter associationAdapter;
    public ListView listView;
    public DatabaseReference databaseReference;
    public ArrayList<Association> items;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AssociationMainFragment.
     */
    public static AssociationMainFragment newInstance() {
        return new AssociationMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(this.getClass().getCanonicalName(), "Entre dans onCreate");
        items = new ArrayList<>();
        associationAdapter = new CustomAssociationAdapter(getActivity(), items);
        databaseReference = FirebaseDatabase.getInstance().getReference("association");

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(mFirebaseAuth.getCurrentUser() != null) {
            databaseReference.orderByChild("president").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Association association = snapshot.getValue(Association.class);
                    items.add(association);
                    associationAdapter.notifyDataSetChanged();
                    Log.i(this.getClass().getCanonicalName(), snapshot.getKey() + " Association : " + association);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(this.getClass().getCanonicalName(), "Entre dans onCreateView");
        View v = inflater.inflate(R.layout.fragment_association_main, container, false);
        listView = (ListView) v.findViewById(R.id.fragment_association_main_listView);
        listView.setAdapter(associationAdapter);
        Button createAssociationButton =
                (Button) v.findViewById(R.id.fragment_association_main_button_create_association);
        createAssociationButton.setOnClickListener(this);
        return v;
    }

    //For API Level 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(this.getClass().getCanonicalName(), "Entre dans onAttach Context");
        if (context instanceof CreateAssociationFragment.OnFragmentInteractionListener) {
            mListener = (AssociationMainFragment.OnFragmentInteractionListener) context;
        } else {
            throw new FragmentRuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    //For API Level 22 (deprecated)
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(this.getClass().getCanonicalName(), "Entre dans onAttach Activity");
        try {
            mListener = (AssociationMainFragment.OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new FragmentClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(this.getClass().getCanonicalName(), "Entre dans onDetach");
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Log.i(this.getClass().getCanonicalName(), "Entre dans onClick ");
        mListener.onAssociationDashboardFragmentInteraction();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onAssociationDashboardFragmentInteraction();
    }
}