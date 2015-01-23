package net.idolon.crashcatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class CrashDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.crash_dialog);

        Throwable exception = (Throwable) getIntent().getSerializableExtra("e");
        Throwable cause = findCause(exception);


// since API 14 one may also use android.app.ApplicationErrorReport.CrashInfo class 
//  to easily parse exception details into human readable form, like this:
//    
//    CrashInfo crashInfo = new CrashInfo(mException);
//    String exClassName = crashInfo.exceptionClassName;
//    String exStackTrace = crashInfo.stackTrace;

        TextView text = (TextView) findViewById(R.id.text);
        String exName = exception.getClass().getSimpleName();
        String causeName = cause.getClass().getSimpleName();
        CharSequence boldExName = createSpanned(exName, new StyleSpan(Typeface.BOLD));
        CharSequence boldCauseName = createSpanned(causeName, new StyleSpan(Typeface.BOLD));
        CharSequence crashTemplate;
        if (exception == cause) {
            crashTemplate = getText(R.string.msgCrash);
        } else {
            crashTemplate = getText(R.string.msgCrashCause);
        }
        CharSequence crashMessage = TextUtils.replace(crashTemplate,
                new String[] { "%1$s", "%2$s" },
                new CharSequence[] { boldExName, boldCauseName });
        text.setText(crashMessage);
    }

    private Throwable findCause(Throwable exception) {
        Throwable prev = null;
        Throwable cause = exception;
        while (cause.getCause() != null && cause != prev) {
            prev = cause;
            cause = cause.getCause();
        }
        return cause;
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void onCloseClick(View v) {
        finishApplication();
    }

    // Kill the application in the way it won't be auto restarted by Android.
    //
    // Alternatively we may use some bootstrap activity, start it here with CLEAR_TOP flag
    // and then call finish() in its onCreate() method
    private void finishApplication() {
        moveTaskToBack(true);
        finish();
        startActivity(new Intent(this, FirstActivity.class));
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void onIgnoreClick(View view) {
        finish();
    }

    public void onReportClick(View v) {
        TextView text2 = (TextView) findViewById(R.id.text2);
        CharSequence[] mems = getResources().getTextArray(R.array.msgMem);
        int i = (int) (Math.random() * mems.length);
        CharSequence mem = mems[i];
        SpannableStringBuilder sb = new SpannableStringBuilder();
        append(sb, mem, new ForegroundColorSpan(0xFFFFFF00), new StyleSpan(Typeface.BOLD));
        text2.setText(sb);
    }

    private void append(SpannableStringBuilder sb, CharSequence text, Object... spans) {
        int start = sb.length();
        sb.append(text);
        int end = sb.length();
        for (Object span : spans) {
            sb.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    private CharSequence createSpanned(String s, Object... spans) {
        SpannableStringBuilder sb = new SpannableStringBuilder(s);
        for (Object span : spans) {
            sb.setSpan(span, 0, sb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return sb;
    }

}
