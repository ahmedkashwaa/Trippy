package com.example.Project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter {


    public Adapter(Context context, ArrayList<Notes> data){
        super(context,0,data);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notes data = (Notes)getItem(position);

     /*   ViewHolder viewHolder = new ViewHolder();
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
            viewHolder.text = convertView.findViewById(R.id.textTitle);
            viewHolder.image = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            convertView.getTag();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(convertView.getResources(), R.id.imageView, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;



        viewHolder.text.setText(data.getTitle());
        //viewHolder.image.setImageResource(data.getPicture());
    //    viewHolder.image.setImageBitmap(
           //     decodeSampledBitmapFromResource(convertView.getResources(), R.id.imageView, 100, 100));


     viewHolder.image.setImageResource(data.getPicture()); */


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.note_list, parent, false);
        TextView textView = convertView.findViewById(R.id.note);
        textView.setText(data.getNote());


        return convertView;
    }

    public static class ViewHolder {
        TextView text;

    }


}
