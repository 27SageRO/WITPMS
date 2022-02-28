package com.wittyly.witpms.ui.customviews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.POJO.HashtagMentionModel;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.presenter.GetMentionHashtagPresenter;
import com.wittyly.witpms.presenter.GetUserByNamePresenter;
import com.wittyly.witpms.ui.adapter.MentionsAdapter;
import com.wittyly.witpms.util.Utilities;
import com.wittyly.witpms.view.GetMentionHashtagView;
import com.wittyly.witpms.view.GetUserView;

import java.util.ArrayList;

import io.realm.RealmObject;

public class MentionsEditText extends ConstraintLayout implements TextWatcher, GetUserView, GetMentionHashtagView, MentionsAdapter.MentionsListener {

    private SegoeEditText editText;
    private CardView mentionsContainer;

    private RecyclerView mRecyclerView;
    private MentionsAdapter mAdapter;

    private Params params;

    private boolean trigger;
    private String mentionValue;

    private MentionsEditTextListener listener;

    private GetUserByNamePresenter getUserByNamePresenter;
    private GetMentionHashtagPresenter getMentionHashtagPresenter;

    private char triggerSymbol;
    private final static short LINE_HEIGHT = 24;
    private final static short ITEM_HEIGHT = 68;
    private final static short ASCII_AT_MARK = 64;
    private final static short ASCII_SPACE = 32;
    private final static short ASCII_HASHTAG = 35;

    private byte mode;
    private final static byte MODE_TICKET = 1;
    private final static byte MODE_TASK = 2;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public MentionsEditText(Context context) {
        super(context);
    }
    public MentionsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MentionsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MentionsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {

        editText            = (SegoeEditText) getChildAt(0);
        mentionsContainer   = (CardView) getChildAt(1);
        mRecyclerView       = (RecyclerView) mentionsContainer.getChildAt(0);

        editText.addTextChangedListener(this);

        mAdapter = new MentionsAdapter(new ArrayList<RealmObject>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy > 0) {
                    visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                    totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount && triggerSymbol == ASCII_HASHTAG) {
                        loading = false;
                        paginatingHashtagMention();
                    }
                }
            }
        });

        params = Params.create();
        trigger = false;
        mentionValue = "";

        mAdapter.setListener(this);

        // Default Mode
        mode = MODE_TICKET;

    }

    //-----------------------------------------------------
    // Generic interfaces
    //-----------------------------------------------------
    public void setListener(MentionsEditTextListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSelectMentionedUser(User user) {

        removeMentions();

        if (listener != null) {
            listener.onSelectMentionedUser(user);
        }

        cleanText();

    }

    @Override
    public void onSelectMentionedTicket(Ticket ticket) {

        removeMentions();

        if (listener != null) {
            listener.onSelectMentionedTicket(ticket);
        }

        cleanText();

    }

    public void setGetUserByNamePresenter(GetUserByNamePresenter getUserByNamePresenter) {
        this.getUserByNamePresenter = getUserByNamePresenter;
        getUserByNamePresenter.setView(this);
    }

    public void setGetMentionHashtagPresenter(GetMentionHashtagPresenter getMentionHashtagPresenter) {
        this.getMentionHashtagPresenter = getMentionHashtagPresenter;
        getMentionHashtagPresenter.setView(this);
    }

    public void setMode(Byte mode) {
        this.mode = mode;
    }

    //-----------------------------------------------------
    // Positioning Logic
    //-----------------------------------------------------
    private void listContainerConstraintFix() {

        unsetListContainerConstraints();

        int currentLine = getCurrentCursorLine(editText);
        int lineIndexCount = getLineIndexCount(editText);

        if (currentLine > 10) {

            setListContainerConstraintBottomToBottom(currentLine, lineIndexCount);

        } else if (currentLine < lineIndexCount) {

            setListContainerConstraintTopToTop(currentLine, lineIndexCount);

        } else {

            setListContainerConstraintTopToBottom(currentLine, lineIndexCount);

        }

    }
    private void unsetListContainerConstraints() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mentionsContainer.getLayoutParams();
        layoutParams.topToBottom = ConstraintLayout.LayoutParams.UNSET;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET;
        mentionsContainer.setLayoutParams(layoutParams);
    }
    private void setListContainerConstraintBottomToBottom(int currentLine, int lineIndexCount) {

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        if (mAdapter.getItemCount() > 4) {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * 4)
            );

        } else {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * mAdapter.getItemCount())
            );

        }

        constraintSet.connect(
                mentionsContainer.getId(),
                ConstraintSet.BOTTOM,
                editText.getId(),
                ConstraintSet.BOTTOM,
                currentLine < lineIndexCount ? Utilities.dpToPx(LINE_HEIGHT * (editText.getLineCount() - currentLine)) : Utilities.dpToPx(LINE_HEIGHT)
        );

        constraintSet.applyTo(this);

    }
    private void setListContainerConstraintTopToTop(int currentLine, int lineIndexCount) {

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        if (mAdapter.getItemCount() > 4) {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * 4)
            );

        } else {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * mAdapter.getItemCount())
            );

        }

        constraintSet.connect(
                mentionsContainer.getId(),
                ConstraintSet.TOP,
                editText.getId(),
                ConstraintSet.TOP,
                Utilities.dpToPx(LINE_HEIGHT * (currentLine + 1))
        );

        constraintSet.applyTo(this);

    }
    private void setListContainerConstraintTopToBottom(int currentLine, int lineIndexCount) {

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        if (mAdapter.getItemCount() > 4) {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * 4)
            );

        } else {

            constraintSet.constrainHeight(
                    mentionsContainer.getId(),
                    Utilities.dpToPx(ITEM_HEIGHT * mAdapter.getItemCount())
            );

        }

        constraintSet.connect(
                mentionsContainer.getId(),
                ConstraintSet.TOP,
                editText.getId(),
                ConstraintSet.BOTTOM,
                0
        );

        constraintSet.applyTo(this);

    }

    //-----------------------------------------------------
    // Views
    //-----------------------------------------------------

    @Override
    public void onDone() {
        if (trigger && mAdapter.getItemCount() > 0) {
            showMentions();
            loading = true;
        } else if (mAdapter.getItemCount() == 0) {
            hideMentions();
        }
    }

    @Override
    public void onHalt(Throwable e) {
        Log.d("Error", e.getMessage());
    }

    @Override
    public void onResult(User user) {
        mAdapter.add(user);
    }

    @Override
    public void onResult(HashtagMentionModel hashtagMentionModel) {
        mAdapter.add(hashtagMentionModel);
    }

    //-----------------------------------------------------
    // Mention Control
    //-----------------------------------------------------
    public void showMentions() {
        mentionsContainer.setVisibility(VISIBLE);
        listContainerConstraintFix();
    }

    public void prepareMentions(String value) {
        if (triggerSymbol == ASCII_AT_MARK) {

            revertMentions();
            params.putString("name", value);
            getUserByNamePresenter.start(params);

        } else if (triggerSymbol == ASCII_HASHTAG) {

            revertMentions();
            params.putString("value", value);
            params.putInt("page", 0);
            getMentionHashtagPresenter.start(params);

        }
    }

    public void revertMentions() {
        mAdapter.clear();
    }

    public void removeMentions() {
        trigger = false;
        loading = false;
        mentionValue = "";
        revertMentions();
        hideMentions();
    }

    public void hideMentions() {
        mentionsContainer.setVisibility(GONE);
    }

    public void paginatingHashtagMention() {
        params.putInt("page", params.getInt("page") + 1);
        getMentionHashtagPresenter.start(params);
    }

    public void cleanText() {
        String text = editText.getStringText();
        int cursorPos = editText.getSelectionStart();
        int diff = 0;

        for (int i = cursorPos - 1; i > -1; i--) {
            char c = text.charAt(i);
            diff++;
            if (c == triggerSymbol) {
                editText.setText(text.substring(0, i) + text.substring(cursorPos, text.length()));
                editText.setSelection(cursorPos - diff);
                return;
            }
        }
    }

    //-----------------------------------------------------
    // Text Change
    //-----------------------------------------------------
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int isRemoving, int isInsert) {

        /*
         * # If mention symbol(@)
         * will be erased.
         **/

        if (isRemoving == 1 && isTriggerSymbol(charSequence.charAt(i)) && mentionValue.length() == 0 && trigger) {
            removeMentions();
        }

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int isRemoving, int isInsert) {

        hideMentions();

        char c = 0;

        if (isRemoving == 0) {
            c = charSequence.charAt(i);
        }

        if (isRemoving == 0 && isTriggerSymbol(c) && !trigger) {

            trigger = true;
            triggerSymbol = c;

        } else if (isRemoving == 0 && !Character.isAlphabetic(c) && trigger) {

            removeMentions();

        } else if (isRemoving == 0 && trigger) {

            mentionValue += charSequence.charAt(i);

        } else if (isRemoving == 1 && trigger) {

            mentionValue = mentionValue.substring(0, mentionValue.length() - isRemoving);

        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (trigger && mentionValue.length() > 0) {
            prepareMentions(mentionValue);
        }

    }

    //-----------------------------------------------------
    // Helper Functions
    //-----------------------------------------------------
    private int getCurrentCursorLine(SegoeEditText editText) {

        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();

        if (!(selectionStart == -1)) {
            return layout.getLineForOffset(selectionStart);
        }

        return -1;

    }

    private int getLineIndexCount(SegoeEditText editText) {
        return editText.getLineCount() - 1;
    }

    public interface MentionsEditTextListener {
        void onSelectMentionedUser(User user);
        void onSelectMentionedTicket(Ticket ticket);
    }

    public boolean isTriggerSymbol(char c) {
        return c == ASCII_AT_MARK || c == ASCII_HASHTAG;
    }

}
