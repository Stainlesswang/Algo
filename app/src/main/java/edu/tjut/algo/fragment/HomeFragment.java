package edu.tjut.algo.fragment;
import android.app.ProgressDialog;
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
import java.util.Date;

import edu.tjut.algo.R;
import edu.tjut.algo.adpter.DataViewAdapter;
import edu.tjut.algo.adpter.ResultViewAdapter;
import edu.tjut.algo.data.ResultData;
import edu.tjut.algo.data.TestData;
import edu.tjut.algo.ga.GAKnapsack;
import edu.tjut.algo.ga.MyMethod;
import edu.tjut.algo.sa.SimulatedAnnealing;
import edu.tjut.algo.util.AlgoUtil;
import edu.tjut.algo.util.TextEnum;
public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;



    private Spinner sp_selectData=null;
    private Spinner sp_selectMethod=null;
    private Button  btn_do=null;
    private ListView listView_data=null;
    private ListView listView_result=null;
    private Handler handler;
    private TestData testData;
    private ProgressDialog pd;
    private OnFragmentInteractionListener mListener;
    public HomeFragment() {
    }

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
        refreshResultListView();
        //选择测试数据 下拉框
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
                pd.dismiss();// 关闭ProgressDialog
                Bundle bundle=msg.getData();
                ResultData resultData=new ResultData(bundle.getInt("dataId"),bundle.getString("bestStr"),bundle.getFloat("time"),
                        bundle.getInt("method"),bundle.getDouble("percent"),bundle.getInt("resultValue"),
                        bundle.getInt("nowWeight"),bundle.getInt("capacity"),  new Date());
                if(resultData.save()){
                    refreshResultListView();
                    System.out.println("+++++++"+resultData.toString());
                    Toast.makeText(getActivity(),"数据保存成功了！",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        final Runnable  mRunnable = new Runnable() {
            @Override
            public void run() {
                    TestData data=getTxtData(sp_selectData.getSelectedItemPosition());
                    MyMethod myMethod=null;
                    switch (sp_selectMethod.getSelectedItemPosition()){
                        case 0:
                            myMethod=new GAKnapsack(200, 2000, 0.5f, 0.05f, 0.1f, data);//遗传算法
                            break;
                        case 1:
                            myMethod=new SimulatedAnnealing(data);//模拟退火
                            break;
                        case 2:
                            myMethod=new SimulatedAnnealing(data,true);//爬山算法
                            break;

                    }
                    ResultData resultData=myMethod.solve();
                    Bundle bundle=new Bundle();
                    bundle.putInt("dataId",resultData.getDataId());
                bundle.putInt("method",resultData.getMethod());
                bundle.putDouble("percent",resultData.getPercent());
                    bundle.putString("bestStr",resultData.getBestStr());
                    bundle.putFloat("time",resultData.getTime());
                bundle.putInt("resultValue",resultData.getResultValue());
                bundle.putInt("nowWeight",resultData.getNowWeight());
                bundle.putInt("capacity",resultData.getCapacity());
                Message message=new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        //点击执行按钮监听
        btn_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击运行，开启新的线程，并将结果返回到当前界面显示出来 并且存储到数据库中
                pd = ProgressDialog.show(getContext(), "算法进行中", "正在努力计算，请稍后……");
                Thread thread=new Thread(mRunnable);
                thread.start();
            }
        });
        return view;

    }

    private TestData getTxtData(int position){
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
        ArrayList<ResultData> resultDatas= (ArrayList<ResultData>) DataSupport.order("date desc").find(ResultData.class);
        ResultViewAdapter resultViewAdapter=new ResultViewAdapter(resultDatas);
        listView_result.setAdapter(resultViewAdapter);
    }
    private void  init(View view){
        sp_selectMethod=(Spinner) view.findViewById(R.id.sp_selectMethod);
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
