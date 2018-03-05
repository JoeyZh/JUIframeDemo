package com.joey.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.joey.ui.CheckedModel;
import com.joey.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckedAdapter extends BaseModelAdapter<CheckedModel> {

    private OnItemCheckListener checkListener;

    /**
     * 支持两次点击取消选中状态的单选容器
     */
    public final static int TYPE_SINGLE_DESELECTED = 0;
    /**
     * 普通状态的，点击选中，再次点击不能取消选中状态
     */
    public final static int TYPE_NORMAL = 1;
    /**
     * 多选状态CheckedModel
     */
    public final static int TYPE_MULTI_SELECTED = 2;

    public int type = TYPE_SINGLE_DESELECTED;

    private HashMap<String, CheckedModel> selectdMap = new HashMap<>();


    public CheckedAdapter(Context context, List<CheckedModel> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    public CheckedAdapter(Context context, List<CheckedModel> dataList, int layoutId, int type) {
        super(context, dataList, layoutId);
        this.type = type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            convertView = View.inflate(context,
                    layoutId,
                    null);
            vHolder = new ViewHolder(convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        BindViewHolder(position, convertView, vHolder);

        return convertView;
    }

    @Override
    public CheckedModel getItem(int i) {
        return (CheckedModel) super.getItem(i);
    }

    private void BindViewHolder(int position, View convertView, ViewHolder holder) {
        CheckedModel item = getItem(position);
        if (TextUtils.isEmpty(item.getName())) {
            setText(holder.cbItem, item.getId());
        } else {
            setText(holder.cbItem, item.getName());
        }
        holder.cbItem.setTag(position);
        if (selectdMap.containsKey(item.getId())) {
            holder.cbItem.setChecked(true);
        } else {
            holder.cbItem.setChecked(false);
        }
    }

    public int getType() {
        return type;
    }

    public void setOnCheckListener(OnItemCheckListener listener) {
        checkListener = listener;
    }

    public void setDataChanged(List<CheckedModel> list) {
        this.data = list;
        clearSelected();
        notifyDataSetChanged();
    }

    public List<CheckedModel> getChecked() {
        List<CheckedModel> selectedArray = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).isChecked()) {
                selectedArray.add(getItem(i));
            }
        }
        return selectedArray;
    }

    public CheckedModel getSelected() {
        if (selectdMap.isEmpty()) {
            return null;
        }
        for (String id : selectdMap.keySet()) {
            return selectdMap.get(id);
        }
        return null;
    }

    public List<CheckedModel> getSelectedList() {
        return new ArrayList<>(selectdMap.values());
    }

    public void clearSelected(int index) {
        clearSelected(index, null);
    }

    public void clearSelected() {
        selectdMap.clear();
        notifyDataSetChanged();
    }

    public void setSelectedByIds(String... ids) {
        for (int i = 0; i < ids.length; i++) {
            setSelectedById(ids[i]);
        }
    }

    public void addSelected(String id, String name) {
        selectdMap.put(id, new CheckedModel(id, name));
        notifyDataSetChanged();
    }

    public void setSelected(int index, View v) {
        CheckedModel model = getItem(index);
        if (model == null)
            return;
        if (type != TYPE_NORMAL && model != null && selectdMap.containsKey(model.getId())) {
            clearSelected(index, v);
            return;
        }
        if (type != TYPE_MULTI_SELECTED) {
            selectdMap.clear();
        }
        selectdMap.put(getItem(index).getId(), getItem(index));
        notifyDataSetChanged();
        if (checkListener != null) {
            checkListener.onItemCheck(index, v, getItem(index));
        }
    }

    public void setSelectedById(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        for (int i = 0; i < getCount(); i++) {
            CheckedModel model = getItem(i);
            if (model == null)
                continue;
            if (id.equals(model.getId())) {
                setSelected(i, null);
                return;
            }
        }
    }

    public void clearSelected(int index, View v) {
        CheckedModel model = getItem(index);
        if (model == null)
            return;
        selectdMap.remove(model.getId());
        notifyDataSetChanged();
        if (checkListener != null) {
            checkListener.onItemDisCheck(index, v, model);
        }

    }

    class ViewHolder {
        private CheckBox cbItem;

        public ViewHolder(final View view) {
            cbItem = (CheckBox) view.findViewById(R.id.cb_check_item);
            cbItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    setSelected(position, v);

                }
            });
        }

    }

    public interface OnItemCheckListener {

        public void onItemCheck(int position, View view, CheckedModel model);

        public void onItemDisCheck(int postion, View view, CheckedModel model);
    }
}