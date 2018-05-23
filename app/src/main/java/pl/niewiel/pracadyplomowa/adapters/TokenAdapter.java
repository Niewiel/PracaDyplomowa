package pl.niewiel.pracadyplomowa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.ViewHolders.TestViewHolder;
import pl.niewiel.pracadyplomowa.ViewHolders.TokenViewHolder;
import pl.niewiel.pracadyplomowa.database.model.Token;
import pl.niewiel.pracadyplomowa.database.service.TestService;

public class TokenAdapter extends ArrayAdapter<Token> {
    public TokenAdapter(@NonNull Context context, int resource, @NonNull List<Token> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final TokenViewHolder viewHolder;
        final Token token = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.row, parent, false);
            viewHolder = new TokenViewHolder();
            viewHolder.access_token = (TextView) convertView.findViewById(R.id.row_access_token);
            viewHolder.expires_in = (TextView) convertView.findViewById(R.id.row_expires_in);
            viewHolder.token_type = (TextView) convertView.findViewById(R.id.row_token_type);
            viewHolder.refresh_token = (TextView) convertView.findViewById(R.id.row_refresh_token);


            viewHolder.access_token.setText(token.getAccess_token());
            viewHolder.expires_in.setText(token.getExpires_in());
            viewHolder.token_type.setText(token.getToken_type());
            viewHolder.refresh_token.setText(token.getRefresh_token());

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TokenViewHolder) convertView.getTag();
            viewHolder.access_token.setText(token.getAccess_token());
            viewHolder.expires_in.setText(token.getExpires_in());
            viewHolder.token_type.setText(token.getToken_type());
            viewHolder.refresh_token.setText(token.getRefresh_token());
        }
        return convertView;
    }
}
