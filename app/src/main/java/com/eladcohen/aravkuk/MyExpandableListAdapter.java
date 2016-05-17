package com.eladcohen.aravkuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

   public class MyExpandableListAdapter extends BaseExpandableListAdapter {
        private String[] groups;

        private String[][] children = {
//
//                {
//                    "foo1",
//                    "foo2",
//                    "foo3"
//                },
//
//                {
//                    "foo1",
//                    "foo2",
//                    "foo3"
//                },
//
//                {
//                    "foo1",
//                    "foo2",
//                    "foo3"
//                }
            };

    //Add these lines to your code    
    private Context context;

    public MyExpandableListAdapter(Context context, BookList bookList) {
    	String[] bookNamesArr = bookList.getBookNamesArr(); 
        this.context = context;
        this.groups = bookList.getBookNamesArr();
        String[][] bookCategories=new String[bookNamesArr.length][];
        for (int i=0 ; i<bookNamesArr.length ; i++)
        {
        	bookCategories[i]=bookList.getBookByIndex(i).getCategoires();
        }
        this.children=bookCategories;
    }

        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_child, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.tvChild);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return convertView;
        }

        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        public int getGroupCount() {
                return groups.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tvGroup);
            textView.setText(getGroup(groupPosition).toString());
            return convertView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }
