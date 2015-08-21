package com.testingfragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ContactsCompletionView completionView;
    Person[] people;
    ArrayAdapter<Person> adapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);



        View v = inflater.inflate(R.layout.fragment_main, container, false);
//        final ArrayList<Fragment> list = new ArrayList<Fragment>();
//
//        list.add(new BlankFragment());
//        list.add(new BlankFragment());
//        list.add(new BlankFragment());
//
//        CustomViewPager pager = (CustomViewPager) v.findViewById(R.id.pager_view);
//        pager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int i) {
//                return list.get(i);
//            }
//
//            @Override
//            public int getCount() {
//                return list.size();
//            }
//        });


        final ContactsCompletionView inputEditText = (ContactsCompletionView) v.findViewById(R.id
                .searchView);

        people = new Person[]{
                new Person("Marshall Weir", "marshall@example.com"),
                new Person("Margaret Smith", "margaret@example.com"),
                new Person("Max Jordan", "max@example.com"),
                new Person("Meg Peterson", "meg@example.com"),
                new Person("Amanda Johnson", "amanda@example.com"),
                new Person("Terry Anderson", "terry@example.com")
        };
        ArrayAdapter<Person> adapter = new ArrayAdapter<Person>(getActivity(), android.R.layout
                .simple_dropdown_item_1line, people);
        inputEditText.setAdapter(adapter);
        inputEditText.setThreshold(1); //Set number of characters before the dropdown should be shown
        inputEditText.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);


        //Create a new Tokenizer which will get text after '@' and terminate on ' '
        inputEditText.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {

            @Override
            public CharSequence terminateToken(CharSequence text) {
                int i = text.length();


                while (i > 0 && text.charAt(i - 1) == ' ') {
                    i--;
                }

                if (i > 0 && text.charAt(i - 1) == ' ') {
                    return text;
                } else {
                    if (text instanceof Spanned) {
                        SpannableString sp = new SpannableString(text + " ");
                        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                        return sp;
                    } else {
                        return text + " ";
                    }
                }
            }

            @Override
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;

                while (i > 0 && text.charAt(i - 1) != '@') {
                    i--;
                }

                //Check if token really started with @, else we don't have a valid token
                if (i < 1 || text.charAt(i - 1) != '@') {
                    return cursor;
                }

                return i;
            }

            @Override
            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                int len = text.length();

                while (i < len) {
                    if (text.charAt(i) == ' ') {
                        return i;
                    } else {
                        i++;
                    }
                }

                return len;
            }
        });


//        people = new Person[]{
//                new Person("Marshall Weir", "marshall@example.com"),
//                new Person("Margaret Smith", "margaret@example.com"),
//                new Person("Max Jordan", "max@example.com"),
//                new Person("Meg Peterson", "meg@example.com"),
//                new Person("Amanda Johnson", "amanda@example.com"),
//                new Person("Terry Anderson", "terry@example.com")
//        };
//
//        adapter = new ArrayAdapter<Person>(getActivity(), android.R.layout.simple_list_item_1, people);
//
//        completionView = (ContactsCompletionView)v.findViewById(R.id.searchView);
//        completionView.setAdapter(adapter);
//        completionView.setThreshold(1);
//        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        return v;
    }
}
