package com.kohinoor.sandy.kohinnor.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kohinoor.sandy.kohinnor.Adapter.MystudymaterialRecyclerViewAdapter;
import com.kohinoor.sandy.kohinnor.Model.CoursesDataModel;
import com.kohinoor.sandy.kohinnor.Model.StudyDataModel;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StudyMFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<StudyDataModel> mDatabase;
    private OnListFragmentInteractionListener mListener;
    private String mFromWhere;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudyMFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFromWhere = getArguments().getString(getString(R.string.FromWhere));
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studymaterial_list, container, false);

        mDatabase = new ArrayList<>();

        mDatabase.add(new StudyDataModel(StudyDataModel.HEADER_TYPE,getString(R.string.Course_wise_material),0));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.PSI_H),R.mipmap.ic_psi_logo_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Police_H),R.mipmap.ic_mhpolice_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.STI_H),R.mipmap.ic_sti_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Assistant_H),R.mipmap.ic_assistant_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Agri_H),R.mipmap.ic_agri_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.MPSC_H),R.mipmap.ic_mpsc_logo_round));

        mDatabase.add(new StudyDataModel(StudyDataModel.HEADER_TYPE,getString(R.string.Subject_wise_material),0));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Marathi),R.mipmap.ic_marathi_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.English),R.mipmap.ic_english_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.History),R.mipmap.ic_history_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Geography),R.mipmap.ic_geo_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Politics),R.mipmap.ic_politics_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Science),R.mipmap.ic_science_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Math),R.mipmap.ic_math_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Apti),R.mipmap.ic_apti_round));
        mDatabase.add(new StudyDataModel(StudyDataModel.CONTENT_TYPE,getString(R.string.Computer),R.mipmap.ic_comp_round));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MystudymaterialRecyclerViewAdapter(mDatabase, mListener,mFromWhere));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(StudyDataModel item, String l_strFlowFrom);
    }
}
