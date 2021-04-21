package com.penzastreetstudios.currencieslist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.QueueViewHolder> {

    private ArrayList<Valute> valutes = new ArrayList<>();

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_card, parent, false);
        return new QueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder holder, int position) {
        holder.bind(valutes.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    class QueueViewHolder extends RecyclerView.ViewHolder {
        private ImageView flagImage;
        private TextView nameField;
        private TextView codeField;
        private TextView rateField;

        public QueueViewHolder (View ItemView) {
            super(ItemView);
            flagImage = ItemView.findViewById(R.id.image);
            codeField = ItemView.findViewById(R.id.code);
            nameField = ItemView.findViewById(R.id.name);
            rateField = ItemView.findViewById(R.id.buy_rate);
        }

        public void bind(Valute valute) {
            flagImage.setImageBitmap(valute.flag);
            codeField.setText(valute.code);
            nameField.setText(valute.name);
            rateField.setText(String.format("%.2f", valute.rate));
        }
    }

    public void setItems(ArrayList<Valute> valutes) {
        this.valutes.addAll(valutes);
        notifyDataSetChanged();
    }

    public void clearItems() {
        valutes.clear();
        notifyDataSetChanged();
    }
}
