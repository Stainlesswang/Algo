package edu.tjut.algo.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.tjut.algo.R;
import edu.tjut.algo.data.Item;
import edu.tjut.algo.data.TestData;

/**
 * Created by Administrator on 2018/4/8.
 */

public class DataViewAdapter extends BaseAdapter {
    private TestData testData;
    private  Item item;
    public DataViewAdapter(TestData testData){
        this.testData=testData;
    }
    @Override
    public int getCount() {
        return testData.getNumItems();
    }

    @Override
    public Object getItem(int position) {
        item= testData.getItems().get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dataitem,null);
        TextView txt_id= (TextView) view.findViewById(R.id.data_id);
        TextView txt_weight= (TextView) view.findViewById(R.id.data_weight);
        TextView txt_value= (TextView) view.findViewById(R.id.data_value);
//            txt_id.setText("当前数据为:"+"第"+testData.getItems().get(position).getDataID()+"组");
//            txt_weight.setText("物品数量："+testData.getNumItems());
//            txt_value.setText("背包容量："+testData.getCapacity());

            txt_id.setText("编号："+(position+1));
            txt_weight.setText("重量："+testData.getItems().get(position).getWeight());
            txt_value.setText("价值："+testData.getItems().get(position).getValue());

        return view;
    }
}
