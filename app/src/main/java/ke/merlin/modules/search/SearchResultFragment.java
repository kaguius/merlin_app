package ke.merlin.modules.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.merlin.R;
import ke.merlin.models.customers.Customers;
import ke.merlin.modules.customers.CustomerDataAdapter;
import ke.merlin.utils.MyDateTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<Customers> mArrayList;
    private CustomerDataAdapter mAdapter;

    Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateTypeAdapter()).create();


    public SearchResultFragment() {
        // Required empty public constructor
    }

    public void setArrayList(ArrayList<Customers> list) {
        this.mArrayList = list;
        Log.i("mArrayList size: ", String.valueOf(mArrayList.size()));
   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);
        getActivity().setTitle("Customers");
        setHasOptionsMenu(true);

        initViews(v);

        if (savedInstanceState != null) {
            String values = savedInstanceState.getString("savedArray");
            mArrayList = gson.fromJson(values, new TypeToken<List<Customers>>() {}.getType());
            mAdapter = new CustomerDataAdapter(getActivity(), mArrayList);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter = new CustomerDataAdapter(getActivity(), mArrayList);
            mRecyclerView.setAdapter(mAdapter);
        }


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (mArrayList != null) {
            String values = gson.toJson(mArrayList);
            savedState.putString("savedArray", values);
        }
    }

    private void initViews(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

}
