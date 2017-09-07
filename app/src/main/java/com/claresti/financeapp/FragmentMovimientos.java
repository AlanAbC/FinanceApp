package com.claresti.financeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMovimientos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMovimientos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMovimientos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //RecyclerView
    private RecyclerView recyclerView;

    //variables para carga de items y actualizaciones
    private int lastVisibleItem, totalItemCount;
    private LinearLayoutManager linearLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressBar centerProgress, bottomProgress;

    private AdapterMovements adapterMovements;

    private Context context;

    public FragmentMovimientos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMovimientos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMovimientos newInstance(String param1, String param2) {
        FragmentMovimientos fragment = new FragmentMovimientos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movimientos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_movements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutMovements);
        centerProgress = (ProgressBar) view.findViewById(R.id.progress_center_movements);
        bottomProgress = (ProgressBar) view.findViewById(R.id.progress_bottom_movements);

        adapterMovements = AdapterMovements.getInstance(context, view);
        adapterMovements.updateContent(centerProgress);
        recyclerView.setAdapter(adapterMovements);

        asignarListeners();

        setUpRecyclerSwipe(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Funcion en la que se asignaran los listener a os objetos que se requieran
     */
    private void asignarListeners()
    {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayout.getItemCount();
                lastVisibleItem = linearLayout.findLastCompletelyVisibleItemPosition();
                if(!AdapterMovements.isLoading && totalItemCount == (lastVisibleItem + 1))
                {
                    adapterMovements.loadMoreItems(bottomProgress);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!AdapterMovements.isLoading)
                {
                    adapterMovements.updateContent(centerProgress);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Funcion que implementa la animacion del swipe to dismiss
     */
    public void setUpRecyclerSwipe(final View view)
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            Drawable background = new ColorDrawable(Color.parseColor("#185e1f"));

            boolean isSwiped = false;

            public void drawSwipedColor()
            {
                background = new ColorDrawable(Color.parseColor("#185e1f"));
            }

            public void drawWhiteColor()
            {
                background = new ColorDrawable(Color.WHITE);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                Log.i("MOV", viewHolder.itemView.getRight() + "");
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if(isSwiped)
                {
                    Snackbar.make(view, "blanco", Snackbar.LENGTH_SHORT).show();
                    isSwiped = false;
                    adapterMovements.setSwipedPosition(-1);
                }
                else
                {
                    Snackbar.make(view, "Verde", Snackbar.LENGTH_SHORT).show();
                    isSwiped = true;
                    adapterMovements.setSwipedPosition(position);
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    Toast.makeText(context, "lel", Toast.LENGTH_SHORT).show();
                }

                if(isSwiped)
                {
                    drawWhiteColor();
                }
                else
                {
                    drawSwipedColor();
                }


                // draw red background
                if(((int) dX) < 0)
                {
                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    background.draw(c);
                }
                else
                {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                    background.draw(c);
                }


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
