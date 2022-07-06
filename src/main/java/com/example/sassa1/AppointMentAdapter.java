package com.example.sassa1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;



public class AppointMentAdapter extends ArrayAdapter<Appointment>
{
        private Context context;
        private List<Appointment> appointmentList;

        public AppointMentAdapter(Context context, List<Appointment> appointmentList)
        {
            super(context, R.layout.row_layout, appointmentList);
            this.context = context;
            this.appointmentList = appointmentList;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_layout, parent, false);

            TextView tvAppointmentDate = convertView.findViewById(R.id.tvDisplayDate);

            TextView etNames = convertView.findViewById(R.id.etNames);

            String fullname =appointmentList.get(position).getFullNames();
            String surname = appointmentList.get(position).getSurname();
            String name  = AppClass.getInitials(fullname) + " " + surname;
            tvAppointmentDate.setText(appointmentList.get(position).getBookDate());
            etNames.setText(name);

            return convertView;
        }

    }



