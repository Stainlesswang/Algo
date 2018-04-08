package edu.tjut.algo.adpter;

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
        TextView txt_bestStr= (TextView) view.findViewById(R.id.result_bestStr);
        TextView txt_percent= (TextView) view.findViewById(R.id.result_percent);
        TextView txt_time= (TextView) view.findViewById(R.id.result_time);
        txt_bestStr.setText("最优解："+resultDatas.get(position).getBestStr());
        txt_percent.setText("匹配度："+resultDatas.get(position).getPercent()+"%");
        txt_time.setText("耗费时间："+resultDatas.get(position).getTime());
        return view;
    }
}
