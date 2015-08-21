package com.testingfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;


import java.util.ArrayList;

public class ChipsMultiAutoCompleteTextView extends MultiAutoCompleteTextView implements OnItemClickListener {

    private final String TAG = "ChipsMultiAutoCompleteTextview";

    private Context context;
    private ArrayList<String> disabledUserList = new ArrayList<String>();

    /* Constructor */
    public ChipsMultiAutoCompleteTextView(Context context) {

        super(context);
        init(context);
        this.context = context;
    }

    /* Constructor */
    public ChipsMultiAutoCompleteTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
        this.context = context;
    }

    /* Constructor */
    public ChipsMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init(context);
        this.context = context;
    }

    public ArrayList<String> getDisabledUserList() {

        return disabledUserList;
    }

    /* set listeners for item click and text change */
    public void init(Context context) {

        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
    }

    /*
     * TextWatcher, If user type any country name and press comma then following
     * code will regenerate chips
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if(count >=1){
            // if(s.charAt(start) == ',')
            // setChips(); // generate chips
            // }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };




//    @Override
//    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//
//        return new MyInputConnection(super.onCreateInputConnection(outAttrs),
//                true);
//    }
//
//    private class MyInputConnection extends InputConnectionWrapper {
//
//        public MyInputConnection(InputConnection target, boolean mutable) {
//
//            super(target, mutable);
//        }
//
//        @Override
//        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
//            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
//            if (beforeLength == 1 && afterLength == 0) {
//                // backspace
//                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
//                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
//            }
//
//            return super.deleteSurroundingText(beforeLength, afterLength);
//        }
//
//
//        @Override
//        public boolean sendKeyEvent(KeyEvent event) {
//
//
//            if (event.getAction() == KeyEvent.ACTION_DOWN
//                    && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                // Un-comment if you wish to cancel the backspace:
//                // return false;
//            }
//            return super.sendKeyEvent(event);
//        }
//
//    }


    /* This function has whole logic for chips generate */
    public void setChips() {


        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (getText().toString().contains(",")) // check comma in string
        {
            Log.e("", "im called"+getText());
            SpannableStringBuilder ssb = new SpannableStringBuilder(getText());
            // split string wich comma
            String chips[] = getText().toString().trim().split("@");
            int x = 0;


            for (String c : chips) {

                // inflate chips_edittext layout
                LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                LinearLayout view = (LinearLayout)lf.inflate(R.layout.contact_token, (ViewGroup)
                        ChipsMultiAutoCompleteTextView
                        .this.getParent(), false);
                TextView textView = ((TextView) view.findViewById(R.id.name));
                textView.setText(c.trim()); // set text

                if (width == 720 && height >= 1100)
                    textView.setTextSize(30);

                // setFlags(textView, c); // set flag image
                // capture bitmapt of genreated textview
                int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                textView.measure(spec, spec);
                textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
                Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(b);
                canvas.translate(-textView.getScrollX(), -textView.getScrollY());
                textView.draw(canvas);
                textView.setTag("user");
                textView.setDrawingCacheEnabled(true);
                Bitmap cacheBmp = textView.getDrawingCache();
                Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                textView.destroyDrawingCache(); // destory drawable
                // create bitmap drawable for imagespan
                BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
                bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                // create and set imagespan c
                ssb.setSpan(new ImageSpan(bmpDrawable), x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                x = x + c.length() + 1;

            }
            // set chips span
            setText(ssb);
            // move cursor to last
            setSelection(getText().length());
        }

    }


    /* This function has whole logic for chips generate */
    public void setRedChips() {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

//        if (getText().toString().contains(",")) // check comma in string
//        {

            final SpannableStringBuilder ssb = new SpannableStringBuilder(getText());
            // split string wich comma
            String chips[] = getText().toString().trim().split(",");
            int x = 0;

            for (final String c : chips) {
//				if (c.trim().equalsIgnoreCase(name.trim())) {
                if (disabledUserList.contains(c.trim())) {
                    // inflate chips_edittext_red layout
                    LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    LinearLayout view = (LinearLayout)lf.inflate(R.layout.contact_token, (ViewGroup)
                            ChipsMultiAutoCompleteTextView
                                    .this.getParent(), false);
                    TextView textView = ((TextView) view.findViewById(R.id.name));
                    textView.setText(c.trim()); // set text

                    if (width == 720 && height >= 1100)
                        textView.setTextSize(30);

                    // setFlags(textView, c); // set flag image
                    // capture bitmapt of genreated textview
//                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_archive, 0, 0, 0);
                    int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    textView.measure(spec, spec);
                    textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());

                    Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(b);
                    canvas.translate(-textView.getScrollX(), -textView.getScrollY());
                    textView.draw(canvas);
                    textView.setTag("archived");
                    textView.setDrawingCacheEnabled(true);
                    Bitmap cacheBmp = textView.getDrawingCache();
                    Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                    textView.destroyDrawingCache(); // destory drawable
                    // create bitmap drawable for imagespan
                    BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
                    bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                    // create and set imagespan c
                    final ImageSpan imageSpan = new ImageSpan(bmpDrawable);
                    ssb.setSpan(imageSpan, x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            deleteSpan(c.trim());
                        }
                    }, x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    x = x + c.length() + 1;
//                    ssb.setSpan(new ClickableSpan() {
//
//                        @Override
//                        public void onClick(View widget) {
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setTitle(getResources().getString(R.string.app_name));
//                            builder.setMessage(getResources().getString(R.string.alert_dialog_archive_user));
//                            builder.setCancelable(false);
//                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    dialog.dismiss();
//                                    deleteSpan(disabledUserList.get(disabledUserList.indexOf(c.trim())));
//                                }
//                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                                public void onClick(DialogInterface dialog, int id) {
//
//                                    dialog.cancel();
//                                }
//                            });

                            // create alert dialog
//                            AlertDialog alertDialog = builder.create();
//                            // show it
//                            alertDialog.show();
//
//                            // ssb.clear();
//                            // setText(ssb);
//                        }
//                    }, x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    x = x + c.length() + 1;
                } else {
//					setChips();
                    // inflate chips_edittext layout
                    LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    LinearLayout view = (LinearLayout)lf.inflate(R.layout.contact_token, (ViewGroup)
                            ChipsMultiAutoCompleteTextView
                                    .this.getParent(), false);
                    TextView textView = ((TextView) view.findViewById(R.id.name));
                    textView.setText(c.trim()); // set text

                    // if (width == 720 && height >= 1100)
                    // textView.setTextSize(30);

                    // setFlags(textView, c); // set flag image
                    // capture bitmapt of genreated textview
                    int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    textView.measure(spec, spec);
                    textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
                    Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(b);
                    canvas.translate(-textView.getScrollX(), -textView.getScrollY());
                    textView.draw(canvas);
                    textView.setTag("user");
                    textView.setDrawingCacheEnabled(true);
                    Bitmap cacheBmp = textView.getDrawingCache();
                    Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
                    textView.destroyDrawingCache(); // destory drawable
                    // create bitmap drawable for imagespan
                    BitmapDrawable bmpDrawable = new BitmapDrawable(viewBmp);
                    bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
                    // create and set imagespan c
                    ssb.setSpan(new ImageSpan(bmpDrawable), x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            deleteSpan(c.trim());
                        }
                    }, x, x + c.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    x = x + c.length() + 1;
                }

            }
            // set chips span
            setText(ssb);

            // setMovementMethod(LinkMovementMethod.getInstance());
            //setMovementMethod(new MyMovementMethod());



        //}

        // move cursor to last
        setSelection(getText().length());

    }

    private void deleteSpan(final String name) {

        if (getText().toString().contains(",")) // check comma in string
        {

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            // split string which comma
            String chips[] = getText().toString().trim().split(",");

            for (String c : chips) {

                if (!c.trim().equalsIgnoreCase(name.trim())) {
                    ssb.append(c).append(",");
                }
            }

//            if (NewMailActivity.to != null && !NewMailActivity.to.isEmpty()) {
//                NewMailActivity.to = "";
//            }

            setText(ssb);
//			setChips();
            setChips();
            setSelection(getText().length());
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // String chips[] = getText().toString().trim().split(",");

//		setChips(); // call generate chips when user select any item from auto complete
        setRedChips(); // call generate chips when user select any item from auto complete

		/*
         * if(getId() == R.id.new_mail_et_to){ if
		 * (((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() !=
		 * null) { ((AutoTextAdapterDirectoryUser) getAdapter()).to +=
		 * "<ToUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() +
		 * "</ToUserId>"; } else { ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).to += "<ToUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getGroupId() +
		 * "</ToUserId>"; } Log.d(((AutoTextAdapterDirectoryUser)
		 * getAdapter()).to); } else if (getId() == R.id.new_mail_et_cc) { if
		 * (((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() !=
		 * null) { ((AutoTextAdapterDirectoryUser) getAdapter()).cc +=
		 * "<CcUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() +
		 * "</CcUserId>"; } else { ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).cc += "<CcUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getGroupId() +
		 * "</CcUserId>"; } Log.d(((AutoTextAdapterDirectoryUser)
		 * getAdapter()).cc); } else if (getId() == R.id.new_mail_et_bcc) { if
		 * (((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() !=
		 * null) { ((AutoTextAdapterDirectoryUser) getAdapter()).bcc +=
		 * "<BccUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getUserId() +
		 * "</BccUserId>"; } else { ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).bcc += "<BccUserId>" + ((AutoTextAdapterDirectoryUser)
		 * getAdapter()).searchedDirectoryUserList .get(position).getGroupId() +
		 * "</BccUserId>"; } Log.d(((AutoTextAdapterDirectoryUser)
		 * getAdapter()).cc); }
		 */

        // Log.d(getId()+"");
    }

	/*
     * this method set country flag image in textview's drawable component, this
	 * logic is not optimize, you need to change as per your requirement
	 */
    /*
     * public void setFlags(TextView textView,String country){ country =
	 * country.trim(); if(country.equals("India")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.india,
	 * 0); } else if(country.equals("United States")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.unitedstates, 0); } else if(country.equals("Canada")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.canada,
	 * 0); } else if(country.equals("Australia")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.australia, 0); } else if(country.equals("United Kingdom")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.unitedkingdom, 0); } else if(country.equals("Philippines")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.philippines, 0); } else if(country.equals("Japan")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.japan,
	 * 0); } else if(country.equals("Italy")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.japan,
	 * 0); } else if(country.equals("Germany")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.germany, 0); } else if(country.equals("Russia")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.russia,
	 * 0); } else if(country.equals("Malaysia")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.malaysia, 0); } else if(country.equals("France")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.france,
	 * 0); } else if(country.equals("Sweden")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sweden,
	 * 0); } else if(country.equals("New Zealand")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.newzealand, 0); } else if(country.equals("Singapore")){
	 * textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
	 * R.drawable.singapore, 0); } }
	 */

}
