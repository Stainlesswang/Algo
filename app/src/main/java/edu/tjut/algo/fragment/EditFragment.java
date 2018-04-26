package edu.tjut.algo.fragment;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import edu.tjut.algo.R;
import edu.tjut.algo.adpter.InputDataViewAdapter;
import edu.tjut.algo.data.ResultData;
import edu.tjut.algo.data.TestData;
import edu.tjut.algo.ga.GAKnapsack;
import edu.tjut.algo.ga.MyMethod;
import edu.tjut.algo.sa.SimulatedAnnealing;

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
    private Button btn_chongzhi=null;
    private ListView input_list=null;
    private Spinner sp_input_selectMethod=null;
    private EditText input_capacity=null;
    private ArrayList<Integer> weights=new ArrayList<>();
    private ArrayList<Integer> values=new ArrayList<>();
    private LinearLayout linearLayout_inputButton,linearLayout_do;
    private TestData testData=null;
    private Handler handler;
    private ProgressDialog pd;

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
        final View view=inflater.inflate(R.layout.fragment_edit, container, false);
        initView(view);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(weight.getText().toString()) || "".equals(value.getText().toString())){
                    Toast.makeText(getContext(), "重量和价值必须全部填写",Toast.LENGTH_LONG).show();
                }else {
                    final  String wei=weight.getText().toString();

                    final  String va=value.getText().toString();

                    try {
                        final int a=Integer.parseInt(wei);
                        final int b=Integer.parseInt(va);
                        weights.add(a);
                        values.add(b);
                    }catch (NumberFormatException e){
                        Toast.makeText(getContext(), "您输入的数太大了  超过了整数表示范围",Toast.LENGTH_LONG).show();

                    }

                    weight.getText().clear();
                    value.getText().clear();
                    testData=new TestData(weights,values,weights.size());
                    InputDataViewAdapter inputDataViewAdapter=new InputDataViewAdapter(testData);
                    input_list.setAdapter(inputDataViewAdapter);
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
        final Runnable  mRunnable = new Runnable() {
            @Override
            public void run() {
                TestData data=new TestData(weights,values,Integer.valueOf(input_capacity.getText().toString()),weights.size());
                MyMethod myMethod=null;
                switch (sp_input_selectMethod.getSelectedItemPosition()){
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
        btn_inputDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ca=input_capacity.getText().toString();
                int a=0;
                for (int i=0;i<weights.size();i++){
                    a+=weights.get(i);
                }
                if ("".equals(ca)||ca.startsWith("0")){
                    Toast.makeText(getContext(), "背包容量错误",Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(ca)<Collections.min(weights)) {
                    Toast.makeText(getContext(), "您的包太小了，一个也装不下，您还是走吧",Toast.LENGTH_LONG).show();

                }else if(Integer.parseInt(ca)>a) {
                    Toast.makeText(getContext(), "您的包足够大了，都拿走！",Toast.LENGTH_LONG).show();

                }else {
                    //点击运行，开启新的线程，并将结果返回到当前界面显示出来 并且存储到数据库中
                    pd = ProgressDialog.show(getContext(), "算法进行中", "正在努力计算，请稍后……");
                    Thread thread=new Thread(mRunnable);
                    thread.start();
                }
            }
        });

        btn_chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("确定要重置当前交界面么？会清空所有数据")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//设置确定按钮
                            @Override//处理确定按钮点击事件
                            public void onClick(DialogInterface dialog, int which) {
                                //清空数据啥的
                                chReplaceFrag();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {//设置取消按钮
                    @Override//取消按钮点击事件
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();//对话框关闭。
                    }
                }).create();
                alert.show();
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
                ArrayList<Integer> list=resultData.getListPosition();
                if (list.size()>0){
                    Toast.makeText(getContext(), "成功找到您需要装的物品（绿色的为选中物品）",Toast.LENGTH_LONG).show();
                    for (int j=0;j<list.size();j++){
                        View  view1=input_list.getChildAt(list.get(j)-input_list.getFirstVisiblePosition());
                        view1.setBackgroundColor(getResources().getColor(R.color.darkseagreen));
                    }
                }else {
                    Toast.makeText(getContext(), "没有匹配的结果，请检查测试数据！",Toast.LENGTH_LONG).show();

                }

                return false;
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
        if (pd!=null){
            pd.dismiss();// 关闭ProgressDialog
        }

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
        btn_chongzhi= (Button) view.findViewById(R.id.btn_chongzhi);
         input_list= (ListView) view.findViewById(R.id.list_inputList);
         sp_input_selectMethod= (Spinner) view.findViewById(R.id.sp_input_selectMethod);
         input_capacity= (EditText) view.findViewById(R.id.input_capacity);
        linearLayout_do= (LinearLayout) view.findViewById(R.id.linearLayout_do);
        linearLayout_inputButton= (LinearLayout) view.findViewById(R.id.linearLayout_inputButton);
    }
    public void chReplaceFrag() {
       weights.clear();
        values.clear();
        linearLayout_do.setVisibility(View.GONE);
        linearLayout_inputButton.setVisibility(View.VISIBLE);
        testData=new TestData(weights,values,weights.size());
        InputDataViewAdapter inputDataViewAdapter=new InputDataViewAdapter(testData);
        input_list.setAdapter(inputDataViewAdapter);
    }

}
