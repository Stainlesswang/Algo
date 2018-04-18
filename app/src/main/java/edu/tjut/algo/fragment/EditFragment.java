package edu.tjut.algo.fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import edu.tjut.algo.R;
import edu.tjut.algo.data.TestData;

public class EditFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText weight=null;
    private EditText value=null;
    private Button btn_add=null;
    private Button btn_finshAdd=null;
    private Button btn_inputDo=null;
    private ListView input_list=null;
    private Spinner sp_input_selectMethod=null;
    private EditText input_capacity=null;
    private ArrayList<Integer> weights=new ArrayList<>();
    private ArrayList<Integer> values=new ArrayList<>();
    private LinearLayout linearLayout_inputButton,linearLayout_do;
    private TestData testData=null;
    private OnFragmentInteractionListener mListener;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
        View view=inflater.inflate(R.layout.fragment_edit, container, false);
        initView(view);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(weight.getText()) || "".equals(value.getText())){
                    Toast.makeText(getContext(), "重量和价值必须全部填写",Toast.LENGTH_LONG).show();
                }else {
                    weights.add(Integer.valueOf(weight.getText().toString()));
                    values.add(Integer.valueOf(value.getText().toString()));
                    weight.getText().clear();
                    value.getText().clear();
                    Toast.makeText(getContext(), "请录入下一个，结束请按结束按钮",Toast.LENGTH_LONG).show();

                }
            }
        });
        btn_finshAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏当前LinearLayout
                if (weights.size()>0 && values.size()>0){
                    linearLayout_inputButton.setVisibility(View.GONE);
                    linearLayout_do.setVisibility(View.VISIBLE);
                }

            }
        });
        return view;
    }








    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initView(View view){
         weight= (EditText) view.findViewById(R.id.editText);
         value= (EditText) view.findViewById(R.id.editText2);
         btn_add= (Button) view.findViewById(R.id.btn_add);
         btn_finshAdd= (Button) view.findViewById(R.id.btn_endAdd);
         btn_inputDo= (Button) view.findViewById(R.id.btn_inputDo);
         input_list= (ListView) view.findViewById(R.id.list_inputList);
         sp_input_selectMethod= (Spinner) view.findViewById(R.id.sp_input_selectMethod);
         input_capacity= (EditText) view.findViewById(R.id.input_capacity);
        linearLayout_do= (LinearLayout) view.findViewById(R.id.linearLayout_do);
        linearLayout_inputButton= (LinearLayout) view.findViewById(R.id.linearLayout_inputButton);
    }
}
