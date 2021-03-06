package com.claresti.financeapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategories extends Fragment implements InterfaceDataTransfer, DialogAlert.DialogAlertInterface{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private AdapterCategories adapterCategories;

    private Context context;

    private boolean showNotes = false;

    public FragmentCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCategories.
     */
    public static FragmentCategories newInstance(String param1, String param2) {
        FragmentCategories fragment = new FragmentCategories();
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

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.recycler_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutCategories);
        centerProgress = view.findViewById(R.id.progress_center_categories);
        bottomProgress = view.findViewById(R.id.progress_bottom_categories);

        //eliminamos las animaciones del recycler view
        NoAnimationItemAnimator noAnimationItemAnimator = new NoAnimationItemAnimator();
        recyclerView.setItemAnimator(noAnimationItemAnimator);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerSwipe(view);
        setUpAnimationDecoratorHelper();

        adapterCategories = AdapterCategories.getInstance(context, this);
        adapterCategories.updateContent();
        recyclerView.setAdapter(adapterCategories);

        asignarListeners();
        showNotes = true;
    }

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

    @Override
    public void showDeleteAlert(int position, String titulo, String mensaje) {
        DialogAlert dialogAlert = new DialogAlert(getActivity(), this);
        dialogAlert.show();
        dialogAlert.setTitulo(titulo);
        dialogAlert.setMessage(mensaje);
        dialogAlert.setId(position);
    }

    @Override
    public void showProgressBar(int state) {
        switch (state){
            case 0:{
                break;
            }
            case 1:{
                centerProgress.setVisibility(View.VISIBLE);
                break;
            }
            case 2:{
                bottomProgress.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void hideProgressBar(int state) {
        switch (state){
            case 0:{
                break;
            }
            case 1:{
                centerProgress.setVisibility(View.GONE);
                break;
            }
            case 2:{
                bottomProgress.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public void showSnackbar(String mensaje) {
        if(showNotes)Snackbar.make(getView(), mensaje, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void acceptDialog(int position) {
        AdapterCategories.getInstance(getActivity(), this).deleteItem(position);
    }

    @Override
    public void cancelDialog() {

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
     * Funcion en la que se asignaran los listener a los objetos que se requieran
     */
    private void asignarListeners()
    {
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayout.getItemCount();
                lastVisibleItem = linearLayout.findLastCompletelyVisibleItemPosition();
                if(!AdapterMovements.isLoading && totalItemCount == (lastVisibleItem + 1))
                {
                    adapterCategories.loadMoreItems(bottomProgress);
                }
            }
        });*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!AdapterMovements.isLoading)
                {
                    adapterCategories.updateContent();
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
                    if(position == adapterCategories.getSwipedPosition())//se hace swipe a un movimiento swipeado :v
                    {
                        isSwiped = false;
                        adapterCategories.setSwipedPosition(-1);
                    }
                    else//se hace swipe a un movimiento diferente al ya swipeado
                    {
                        adapterCategories.setSwipedPosition(position);
                    }
                }
                else//Primera vez que se hace swipe a un movimiento
                {
                    isSwiped = true;
                    adapterCategories.setSwipedPosition(position);
                }

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                int position = viewHolder.getAdapterPosition();

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    Toast.makeText(context, "lel", Toast.LENGTH_SHORT).show();
                }

                if(isSwiped)
                {
                    if(position == adapterCategories.getSwipedPosition())//se hace swipe a un movimiento swipeado :v
                    {
                        drawWhiteColor();
                    }
                    else//se hace swipe a un movimiento diferente al ya swipeado
                    {
                        drawSwipedColor();
                    }
                }
                else//Primera vez que se hace swipe a un movimiento
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

    /**
     * Funcion que dibuja un fondo de color cuando un item del recycler view es deslizado
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background = new ColorDrawable(Color.RED);

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

}
