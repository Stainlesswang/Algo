package edu.tjut.algo.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.tjut.algo.R;
import edu.tjut.algo.data.TestData;

/**
 * Created by thinkpad on 2018-4-17.
 */

public class InputDataViewAdapter extends BaseAdapter {
    private TestData testDat=null;
    public InputDataViewAdapter(TestData testData){
        this.testDat=testData;
    }
    @Override
    public int getCount() {
        return testDat.getNumItems();
    }

    @Override
    public Object getItem(int position) {
        return testDat.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_inputdata,null);
        TextView textView_inputDataId= (TextView) view.findViewById(R.id.textView_inputDataId);
        TextView textView_inputDataWeigh= (TextView) view.findViewById(R.id.textView_inputDataWeigh);
        TextView textView_inputDataValue= (TextView) view.findViewById(R.id.textView_inputDataValue);
        textView_inputDataId.setText("物品编号： "+position);
        textView_inputDataWeigh.setText(testDat.getItems().get(position).getWeight()+"kg");
        textView_inputDataValue.setText(testDat.getItems().get(position).getValue()+"$");
        return view;
    }
}
