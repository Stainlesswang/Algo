package edu.tjut.algo.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import edu.tjut.algo.R;
import edu.tjut.algo.data.ResultData;
import edu.tjut.algo.data.TestData;
import edu.tjut.algo.util.AlgoUtil;
import edu.tjut.algo.util.TextEnum;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart lineChart=null;
    private Spinner sp_selectChartData=null;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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


    //重要的获取界面组建的方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //获取视图容器
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
//        根据控件ID获取相应组件
         lineChart= (LineChart)view.findViewById(R.id.chart2);
        sp_selectChartData= (Spinner) view.findViewById(R.id.sp_selectChartData);
        //填充数据
        getChart(lineChart,"1");
        sp_selectChartData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String daID=position+1+"";
                getChart(lineChart,daID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        Toast.makeText(getContext(), "onPause",Toast.LENGTH_LONG).show();

        super.onPause();
    }
    @Override

    public void onHiddenChanged(boolean hidden) {

        if (hidden) {

            Toast.makeText(getContext(), "hidden",Toast.LENGTH_LONG).show();

        } else {
            getChart(lineChart,"1");
        }

    }
    public void getChart(LineChart lineChart,String dataId){
        ArrayList<ResultData> resultDatas1= (ArrayList<ResultData>) DataSupport.where("method=0 and dataId="+dataId).find(ResultData.class);
        ArrayList<ResultData> resultDatas2= (ArrayList<ResultData>) DataSupport.where("method=1 and dataId="+dataId).find(ResultData.class);
        ArrayList<ResultData> resultDatas3= (ArrayList<ResultData>) DataSupport.where("method=2 and dataId="+dataId).find(ResultData.class);
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        if (resultDatas1.size()>0){
            for (int i=0;i<resultDatas1.size();i++){
                values1.add(new Entry(i+1,resultDatas1.get(i).getTime()));
            }
            LineDataSet set1 = new LineDataSet(values1, "遗传算法");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColors(new int[] { R.color.green }, getContext());
            dataSets.add(set1);
        }
        if (resultDatas2.size()>0){
            for (int i=0;i<resultDatas2.size();i++){
                values2.add(new Entry(i+1,resultDatas2.get(i).getTime()));
            }
            LineDataSet set2 = new LineDataSet(values2, "模拟退火算法");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSets.add(set2);
            set2.setColors(new int[] { R.color.orangered }, getContext());
        }
        if (resultDatas3.size()>0){
            for (int i=0;i<resultDatas3.size();i++){
                values3.add(new Entry(i+1,resultDatas3.get(i).getTime()));
            }
            LineDataSet set3 = new LineDataSet(values3, "爬山算法");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            dataSets.add(set3);

        }
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //创建描述信息
        Description description =new Description();
        description.setText("测试图表");
        description.setTextColor(Color.RED);
        description.setTextSize(20);
        lineChart.setDescription(description);//设置图表描述信息
        lineChart.setNoDataText("没有数据");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        //如果数据不为空  则填充数据
        if (dataSets.size()>0){
            LineData data=new LineData(dataSets);
            lineChart.setData(data);
        }else {
            lineChart.clear();
        }
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();//重绘
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
}
