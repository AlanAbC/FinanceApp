package com.claresti.financeapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claresti.financeapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentEstadisticas extends Fragment {

    private OnFragmentInteractionListener mListener;

    IValueFormatter formatter = new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "$" + new DecimalFormat("###,###,##0.00").format(value);
        }
    };

    PieChart prueba;

    public FragmentEstadisticas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentEstadisticas.
     */
    public static FragmentEstadisticas newInstance() {
        FragmentEstadisticas fragment = new FragmentEstadisticas();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        prueba = view.findViewById(R.id.prueba);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<PieEntry> entries = new ArrayList<>();


        PieEntry p = new PieEntry(18.3f, "Otro");

        entries.add(new PieEntry(18.5f, "Putas"));
        entries.add(new PieEntry(26.7f, "Alcohol"));
        entries.add(new PieEntry(24.0f, "Cigarros"));
        entries.add(new PieEntry(30.8f, "Condones"));
        entries.add(p);

        PieDataSet set = new PieDataSet(entries, "");
        set.setLabel("algo");
        set.setValueTextSize(11f);
        set.setSelectionShift(4f);
        PieData data = new PieData(set);
        set.setColors(new int[]{R.color.blue,
                Color.CYAN,
                Color.GREEN,
                Color.RED,
                Color.MAGENTA
        });
        data.setValueFormatter(formatter);
        data.setHighlightEnabled(true);
        prueba.setData(data);
        prueba.setCenterText("k");
        Description description = new Description();
        description.setText("Gastos por Categoria");
        prueba.setDescription(description);
        prueba.invalidate(); // refresh
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
