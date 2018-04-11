package edu.tjut.algo.adpter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.tjut.algo.R;
import edu.tjut.algo.data.ResultData;

/**
 * Created by Administrator on 2018/4/8.
 */

public class ResultViewAdapter extends BaseAdapter {
    private ArrayList<ResultData> resultDatas;
    public ResultViewAdapter(ArrayList<ResultData> resultDatas){
        this.resultDatas=resultDatas;
    }
    @Override
    public int getCount() {
        return resultDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return resultDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return resultDatas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resultdata,null);
        if (position % 2 == 0) {
            int color1 = Color.argb(122,144,144,144);
            view.setBackgroundColor(color1);

        }
        TextView id= (TextView) view.findViewById(R.id.testdata_ID);
        TextView bianhao= (TextView) view.findViewById(R.id.testdata_bianhao);
        TextView resultValue= (TextView) view.findViewById(R.id.testdata_resultValue);

        TextView time= (TextView) view.findViewById(R.id.testdata_time);
        TextView weight= (TextView) view.findViewById(R.id.testdata_weight);
        TextView percent= (TextView) view.findViewById(R.id.testdata_percent);
        id.setText(""+resultDatas.get(position).getDataId());
        bianhao.setText(""+resultDatas.get(position).getBianhao());
        resultValue.setText(""+resultDatas.get(position).getResultValue());

        weight.setText(""+resultDatas.get(position).getTime());
        time.setText(""+resultDatas.get(position).getNowWeight()+"/"+resultDatas.get(position).getCapacity());
        percent.setText(""+resultDatas.get(position).getPercent()+"%");
        return view;
    }
}
