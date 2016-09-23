package org.hanqim.mcpdict.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.hanqim.mcpdict.Configuration;
import org.hanqim.mcpdict.R;
import org.hanqim.mcpdict.editor.Orthography;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class SearchResultCursorAdapter extends CursorAdapter implements Masks {

    private Context context;
    private int layout;
    private LayoutInflater inflater;
    private boolean showFavoriteButton;
    private static final Configuration config = Configuration.getInstance();

    public SearchResultCursorAdapter(Context context, int layout, Cursor cursor, boolean showFavoriteButton) {
        super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
        this.context = context;
        this.layout = layout;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showFavoriteButton = showFavoriteButton;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        final char unicode;
        String string;
        StringBuilder sb;
        int tag = 0;

        Fetcher fetcher = new Fetcher(cursor);
        ViewHolder holder = new ViewHolder(view);

        // Unicode
        string = fetcher.get("unicode");
        holder.unicodePoint.setText("U+" + string);
        tag |= MASK_UNICODE;

        // Chinese character
        unicode = (char) Integer.parseInt(string, 16);
        string = String.valueOf(unicode);
        holder.hanzi.setText(string);
        tag |= MASK_HZ;

        // Variants
        string = fetcher.get("variants");
        if (string == null) {
            holder.variants.setVisibility(View.GONE);
        } else {
            sb = new StringBuilder();
            for (String s : string.split(" ")) {
                sb.append((char) Integer.parseInt(s, 16));
            }
            holder.variants.setText("(" + sb.toString() + ")");
            holder.variants.setVisibility(View.VISIBLE);
        }

        // Middle Chinese
        string = fetcher.get("mc");
        holder.middleChinese.setText(middleChineseDisplayer.display(string));
        if (string != null) {
            holder.middleChineseDetail.setText(middleChineseDetailDisplayer.display(string));
            tag |= MASK_MC;
        }
        else {
            holder.middleChineseDetail.setText("");
        }

        // Mandarin
        string = fetcher.get("pu");
        holder.mandarin.setText(mandarinDisplayer.display(string));
        if (string != null) tag |= MASK_PU;

        // Cantonese
        string = fetcher.get("ct");
        holder.cantonese.setText(cantoneseDisplayer.display(string));
        if (string != null) tag |= MASK_CT;

        // Shanghai
        string = fetcher.get("sh");
        setRichText(holder.shanghai, shanghaiDisplayer.display(string));
        if (string != null) tag |= MASK_SH;

        // Minnan
        string = fetcher.get("mn");
        setRichText(holder.minnan, minnanDisplayer.display(string));
        if (string != null) tag |= MASK_MN;

        // Korean
        string = fetcher.get("kr");
        holder.korean.setText(koreanDisplayer.display(string));
        if (string != null) tag |= MASK_KR;

        // Vietnamese
        string = fetcher.get("vn");
        holder.vietnamese.setText(vietnameseDisplayer.display(string));
        if (string != null) tag |= MASK_VN;

        // Japanese go-on
        string = fetcher.get("jp_go");
        setRichText(holder.japaneseGo, japaneseDisplayer.display(string));
        if (string != null) tag |= MASK_JP_GO;

        // Japanese kan-on
        string = fetcher.get("jp_kan");
        setRichText(holder.japaneseKan, japaneseDisplayer.display(string));
        if (string != null) tag |= MASK_JP_KAN;

        int i = 0;
        // Japanese tou-on
        string = fetcher.get("jp_tou");
        if (string != null) {
            holder.setExtraImage(i, R.drawable.lang_jp_tou);
            setRichText(holder.japaneseExtras[i], japaneseDisplayer.display(string));
            i++;
            tag |= MASK_JP_TOU;
        }

        // Japanese kwan'you-on
        string = fetcher.get("jp_kwan");
        if (string != null) {
            holder.setExtraImage(i, R.drawable.lang_jp_kwan);
            setRichText(holder.japaneseExtras[i], japaneseDisplayer.display(string));
            i++;
            tag |= MASK_JP_KWAN;
        }

        // Japanese other pronunciations
        string = fetcher.get("jp_other");
        if (string != null) {
            holder.setExtraImage(i, R.drawable.lang_jp_other);
            setRichText(holder.japaneseExtras[i], japaneseDisplayer.display(string));
            i++;
            tag |= MASK_JP_OTHER;
        }

        for (int j = 0; j < i; j++) {
            holder.setJapaneseExtra(j, true);
        }

        for (int j = i; j < 3; j++) {
            holder.setJapaneseExtra(j, false);
        }

        // "Favorite" button
        boolean favorite = fetcher.getInt("is_favorite") == 1;
        Button button = (Button) view.findViewById(R.id.button_favorite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) view.getTag();
                if ((tag & MASK_FAVORITE) == 0) {
                    /**
                     * favorite
                     */

                    //FavoriteDialogs.add(unicode);
                }
                else {
                    //FavoriteDialogs.view(unicode, view);
                }
            }
        });
        if (showFavoriteButton) {
            button.setBackgroundResource(favorite ? R.drawable.ic_star_yellow : R.drawable.ic_star_white);
        }
        else {
            button.setVisibility(View.GONE);
        }
        if (favorite) {
            tag |= MASK_FAVORITE;
        }

        // Favorite comment
        string = fetcher.get("comment");
        holder.comment.setText(string);

        // Set the view's tag to indicate which readings exist
        view.setTag(tag);
    }

    public void setRichText(TextView view, String richTextString) {
        StringBuilder sb = new StringBuilder();
        List<Integer> stars = new ArrayList<Integer>();
        List<Integer> slashes = new ArrayList<Integer>();

        for (int i = 0; i < richTextString.length(); i++) {
            char c = richTextString.charAt(i);
            switch (c) {
                case '*': stars.add(sb.length()); break;
                case '/': slashes.add(sb.length()); break;
                default : sb.append(c); break;
            }
        }

        view.setText(sb.toString(), TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) view.getText();
        for (int i = 1; i < stars.size(); i += 2) {
            spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), stars.get(i-1), stars.get(i), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        for (int i = 1; i < slashes.size(); i += 2) {
            spannable.setSpan(new ForegroundColorSpan(0xFF808080), slashes.get(i-1), slashes.get(i), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }


    private abstract static class Displayer {
        protected static final String NULL_STRING = "-";

        public String display(String s) {
            if (s == null) return NULL_STRING;
            s = lineBreak(s);
            // Find all regions of [a-z0-9]+ in s, and apply displayer to each of them
            StringBuilder sb = new StringBuilder();
            int L = s.length(), p = 0;
            while (p < L) {
                int q = p;
                while (q < L && Character.isLetterOrDigit(s.charAt(q))) q++;
                if (q > p) {
                    String t1 = s.substring(p, q);
                    String t2 = displayOne(t1);
                    sb.append(t2 == null ? t1 : t2);
                    p = q;
                }
                while (p < L && !Character.isLetterOrDigit(s.charAt(p))) p++;
                sb.append(s.substring(q, p));
            }
            // Add spaces as hints for line wrapping
            s = sb.toString().replace(",", ", ")
                    .replace("(", " (")
                    .replace("]", "] ")
                    .replaceAll(" +", " ")
                    .replace(" ,", ",")
                    .trim();
            return s;
        }

        public String lineBreak(String s) {return s;}
        public abstract String displayOne(String s);
    }

    private final Displayer middleChineseDisplayer = new Displayer() {
        public String lineBreak(String s) {return s.replace(",", "\n");}
        public String displayOne(String s) {return Orthography.MiddleChinese.display(s);}
    };

    private final Displayer middleChineseDetailDisplayer = new Displayer() {
        public String lineBreak(String s) {return s.replace(",", "\n");}
        public String displayOne(String s) {return "(" + Orthography.MiddleChinese.detail(s) + ")";}
        public String display(String s) {return " " + super.display(s);}
    };

    private final Displayer mandarinDisplayer = new Displayer() {
        public String displayOne(String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Resources r = context.getResources();
            int style = Integer.parseInt(sp.getString(r.getString(R.string.pref_key_mandarin_display), "0"));
            return Orthography.Mandarin.display(s, style);
        }
    };

    private final Displayer cantoneseDisplayer = new Displayer() {
        public String displayOne(String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Resources r = context.getResources();
            int system = Integer.parseInt(sp.getString(r.getString(R.string.pref_key_cantonese_romanization), "0"));
            return Orthography.Cantonese.display(s, system);
        }
    };

    private final Displayer shanghaiDisplayer = new Displayer() {
        public String displayOne(String s) {
            return Orthography.Shanghai.display(s);
        }
    };

    private final Displayer minnanDisplayer = new Displayer() {
        public String displayOne(String s) {
            return Orthography.Minnan.display(s);
        }
    };

    private final Displayer koreanDisplayer = new Displayer() {
        public String displayOne(String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Resources r = context.getResources();
            int style = Integer.parseInt(sp.getString(r.getString(R.string.pref_key_korean_display), "0"));
            return Orthography.Korean.display(s, style);
        }
    };

    private final Displayer vietnameseDisplayer = new Displayer() {
        public String displayOne(String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Resources r = context.getResources();
            int style = Integer.parseInt(sp.getString(r.getString(R.string.pref_key_vietnamese_tone_position), "0"));
            return Orthography.Vietnamese.display(s, style);
        }
    };

    private final Displayer japaneseDisplayer = new Displayer() {
        public String lineBreak(String s) {
            if (s.charAt(0) == '[') {
                s = '[' + s.substring(1).replace("[", "\n[");
            }
            return s;
        }

        public String displayOne(String s) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Resources r = context.getResources();
            int style = Integer.parseInt(sp.getString(r.getString(R.string.pref_key_japanese_display), "0"));
            return Orthography.Japanese.display(s, style);
        }
    };

    static class Fetcher {
        Cursor cursor;
        Fetcher(Cursor cursor) {
            this.cursor = cursor;
        }
        String get(String col) {
            return cursor.getString(cursor.getColumnIndex(col));
        }
        int getInt(String col) {
            return cursor.getInt(cursor.getColumnIndex(col));
        }
        boolean setCursor(Cursor cursor) {
            if(null==cursor) return false;

            this.cursor = cursor;
            return true;
        }
    }

    static final class ViewHolder {
        @BindView(R.id.text_unicode)
        TextView unicodePoint;
        @BindView(R.id.text_hz)
        TextView hanzi;
        @BindView(R.id.text_variants)
        TextView variants;
        @BindView(R.id.text_comment)
        TextView comment;
        @BindView(R.id.text_mc)
        TextView middleChinese;
        @BindView(R.id.text_mc_detail)
        TextView middleChineseDetail;
        @BindView(R.id.text_pu)
        TextView mandarin;
        @BindView(R.id.text_ct)
        TextView cantonese;
        @BindView(R.id.text_sh)
        TextView shanghai;
        @BindView(R.id.text_mn)
        TextView minnan;
        @BindView(R.id.text_kr)
        TextView korean;
        @BindView(R.id.text_vn)
        TextView vietnamese;
        @BindView(R.id.text_jp_go)
        TextView japaneseGo;
        @BindView(R.id.text_jp_kan)
        TextView japaneseKan;
        // Japanese extras
        @BindViews({R.id.image_jp_extra_1, R.id.image_jp_extra_2, R.id.image_jp_extra_3})
        ImageView[] imageViewJPExtras;
        @BindViews({R.id.text_jp_extra_1, R.id.text_jp_extra_2, R.id.text_jp_extra_3})
        TextView[] japaneseExtras;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void setJapaneseExtra(int i, boolean mode) {
            imageViewJPExtras[i].setVisibility(mode ? View.VISIBLE : View.GONE);
            japaneseExtras[i].setVisibility(mode ? View.VISIBLE : View.GONE);
        }

        void setExtraImage(int i, int resId) {
            imageViewJPExtras[i].setImageResource(resId);
        }
    }
}