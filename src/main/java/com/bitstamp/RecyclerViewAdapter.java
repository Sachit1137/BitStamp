package com.bitstamp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    List<BidAskModel> bidList;

    public RecyclerViewAdapter(List<BidAskModel> bidList) {
        this.bidList = bidList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.bid_ask_table, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            rowViewHolder.bidAmount.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.bidPrice.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.askAmount.setBackgroundResource(R.drawable.table_header_cell_red);
            rowViewHolder.askPrice.setBackgroundResource(R.drawable.table_header_cell_red);

            rowViewHolder.bidAmount.setText("Amount");
            rowViewHolder.bidPrice.setText("Bid(USD)");
            rowViewHolder.bidAmount.setTextColor(Color.BLACK);
            rowViewHolder.bidPrice.setTextColor(Color.BLACK);


            rowViewHolder.askPrice.setText("Ask(USD)");
            rowViewHolder.askAmount.setText("Amount");
            rowViewHolder.askPrice.setTextColor(Color.BLACK);
            rowViewHolder.askAmount.setTextColor(Color.BLACK);

        } else {
            BidAskModel bidAskModel = bidList.get(rowPos-1);

            // Content Cells. Content appear here
            rowViewHolder.bidAmount.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.bidPrice.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.askAmount.setBackgroundResource(R.drawable.table_content_cell_bg );
            rowViewHolder.askPrice.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.bidPrice.setTextColor(Color.GREEN);
            rowViewHolder.askPrice.setTextColor(Color.RED);

            rowViewHolder.bidAmount.setText(""+ bidAskModel.getBidAmount());
            rowViewHolder.bidPrice.setText(""+ bidAskModel.getBidPrice());

            rowViewHolder.askPrice.setText(""+ bidAskModel.getAskPrice());
            rowViewHolder.askAmount.setText(""+ bidAskModel.getAskAmount());

        }
    }

    @Override
    public int getItemCount() {
        return bidList.size()+1; // one more to add header row
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        protected TextView bidAmount;
        protected TextView bidPrice;
        protected TextView askAmount;
        protected TextView askPrice;

        public RowViewHolder(View itemView) {
            super(itemView);

            bidAmount = itemView.findViewById(R.id.bid_amount);
            bidPrice= itemView.findViewById(R.id.bid_price);
            askAmount = itemView.findViewById(R.id.ask_amount);
            askPrice= itemView.findViewById(R.id.ask_price);

        }
    }
}
