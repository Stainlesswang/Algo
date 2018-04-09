package edu.tjut.algo.fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import junit.framework.Test;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import edu.tjut.algo.R;
import edu.tjut.algo.adpter.DataViewAdapter;
import edu.tjut.algo.adpter.ResultViewAdapter;
import edu.tjut.algo.data.ResultData;
import edu.tjut.algo.data.TestData;
import edu.tjut.algo.sa.SimulatedAnnealing;
import edu.tjut.algo.util.AlgoUtil;
import edu.tjut.algo.util.TextEnum;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner sp_selectData=null;
    private Button  btn_do=null;
    private ListView listView_data=null;
    private ListView listView_result=null;
    private Handler handler;
    private TestData testData;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view=  inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
//        Toast.makeText(getActivity(),"当前点击了:"+position,Toast.LENGTH_LONG).show();
        sp_selectData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //默认测试数据时第一组数据
                 testData=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P01);
                testData=getTxtData(position);
                DataViewAdapter dataViewAdapter =new DataViewAdapter(testData);
                listView_data.setAdapter(dataViewAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle bundle=msg.getData();
                ResultData resultData=new ResultData(bundle.getInt("dataId"),bundle.getString("bestStr"),bundle.getFloat("time"),
                        bundle.getInt("method"),bundle.getDouble("percent"));
                if(resultData.save()){
                    refreshResultListView();
                    Toast.makeText(getActivity(),"数据保存成功了！"+resultData.toString(),Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        final Runnable  mRunnable = new Runnable() {
            @Override
            public void run() {
                    TestData data=getTxtData(sp_selectData.getSelectedItemPosition());
                    SimulatedAnnealing simulatedAnnealing=new SimulatedAnnealing(data);
                    ResultData resultData=simulatedAnnealing.make();
                    Bundle bundle=new Bundle();
                    bundle.putInt("dataId",resultData.getDataId());
                    bundle.putString("bestStr",resultData.getBestStr());
                    bundle.putFloat("time",resultData.getTime());
                    bundle.putInt("method",resultData.getMethod());
                    bundle.putDouble("percent",resultData.getPercent());
                Message message=new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        btn_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击运行，开启新的线程，并将结果返回到当前界面显示出来 并且存储到数据库中
                Thread thread=new Thread(mRunnable);
                thread.start();
            }
        });
        return view;

    }

    private TestData getTxtData(int position){
        System.out.println("==========================______"+position);
        TestData testData1 = null;
        switch (position){
            case 0:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P01);
                return testData1;
            case 1:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P02);
                return testData1;
            case 2:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P03);
                return testData1;
            case 3:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P04);
                return testData1;
            case 4:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P05);
                return testData1;
            case 5:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P06);
                return testData1;
            case 6:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P07);
                return testData1;
            case 7:
                testData1=AlgoUtil.getDataFromTxt(getContext(),TextEnum.TXT_P08);
                return testData1;
        }
        return null;
    }
    private void refreshResultListView(){
        ArrayList<ResultData> resultDatas= (ArrayList<ResultData>) DataSupport.findAll(ResultData.class);
        ResultViewAdapter resultViewAdapter=new ResultViewAdapter(resultDatas);
        listView_result.setAdapter(resultViewAdapter);
    }
    private void  init(View view){
        sp_selectData= (Spinner) view.findViewById(R.id.sp_selectData);
        btn_do= (Button) view.findViewById(R.id.btn_do);
        listView_data= (ListView) view.findViewById(R.id.list_data);
        listView_result= (ListView) view.findViewById(R.id.list_result);
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
}
