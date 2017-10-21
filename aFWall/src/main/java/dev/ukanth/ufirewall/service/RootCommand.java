package dev.ukanth.ufirewall.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static dev.ukanth.ufirewall.service.RootShellService.NO_TOAST;

/**
 * Created by ukanth on 21/10/17.
 */

public class RootCommand {
    private List<String> commmands;

    public Callback cb = null;
    public int successToast = NO_TOAST;
    public int failureToast = NO_TOAST;
    public boolean reopenShell = false;
    public int retryExitCode = -1;

    public int commandIndex;
    public boolean ignoreExitCode;
    public Date startTime;
    public int retryCount;

    public StringBuilder res;
    public String lastCommand;
    public StringBuilder lastCommandResult;
    public int exitCode;
    public boolean done = false;


    public List<String> getCommmands() {
        return commmands;
    }

    public void setCommmands(List<String> commmands) {
        this.commmands = commmands;
    }

    public static abstract class Callback {
        /**
         * Optional user-specified callback
         */
        public abstract void cbFunc(RootCommand state);
    }

    /**
     * Set callback to run after command completion
     *
     * @param cb Callback object, with cbFunc() populated
     * @return RootCommand builder object
     */
    public RootCommand setCallback(Callback cb) {
        this.cb = cb;
        return this;
    }

    /**
     * Tell RootShell to display a toast message on success
     *
     * @param resId Resource ID of the toast string
     * @return RootCommand builder object
     */
    public RootCommand setSuccessToast(int resId) {
        this.successToast = resId;
        return this;
    }

    /**
     * Tell RootShell to display a toast message on failure
     *
     * @param resId Resource ID of the toast string
     * @return RootCommand builder object
     */
    public RootCommand setFailureToast(int resId) {
        this.failureToast = resId;
        return this;
    }

    /**
     * Tell RootShell whether or not it should try to open a new root shell if the last attempt
     * died.  To avoid "thrashing" it might be best to only try this in response to a user
     * request
     *
     * @param reopenShell true to attempt reopening a failed shell
     * @return RootCommand builder object
     */
    public RootCommand setReopenShell(boolean reopenShell) {
        this.reopenShell = reopenShell;
        return this;
    }

    /**
     * Capture the command output in this.res
     *
     * @param enableLog true to enable logging
     * @return RootCommand builder object
     */
    public RootCommand setLogging(boolean enableLog) {
        if (enableLog) {
            this.res = new StringBuilder();
        } else {
            this.res = null;
        }
        return this;
    }

    /**
     * Retry a failed command on a specific exit code
     *
     * @param retryExitCode code that indicates a transient failure
     * @return RootCommand builder object
     */
    public RootCommand setRetryExitCode(int retryExitCode) {
        this.retryExitCode = retryExitCode;
        return this;
    }

    /**
     * Run a series of commands as root; call cb.cbFunc() when complete
     *
     * @param ctx    Context object used to create toasts
     * @param script List of commands to run as root
     */
    public final void run(Context ctx, List<String> script) {
        RootShellService.runScriptAsRoot(ctx, script, this, false);
    }

    /**
     * Run a series of commands as root in thread mode; call cb.cbFunc() when complete
     *
     * @param ctx    Context object used to create toasts
     * @param script List of commands to run as root
     */
    public final void runThread(Context ctx, List<String> script) {
        RootShellService.runScriptAsRoot(ctx, script, this, true);
    }

    /**
     * Run a single command as root; call cb.cbFunc() when complete
     *
     * @param ctx Context object used to create toasts
     * @param cmd Command to run as root
     */
    public final void run(Context ctx, String cmd) {
        List<String> script = new ArrayList<String>();
        script.add(cmd);
        RootShellService.runScriptAsRoot(ctx, script, this, false);
    }
}
