package org.netbeans.modules.couchbase.document;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JButton;
import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.document.OnSaveTask;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.util.RequestProcessor;

public class JsonCouchbaseOnSaveTask implements OnSaveTask {

    private final Document document;
    private AtomicBoolean canceled = new AtomicBoolean();

    JsonCouchbaseOnSaveTask(Document doc) {
        this.document = doc;
    }

    @Override
    public void performTask() {
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                NotifyDescriptor nd = new NotifyDescriptor.Confirmation(new DocumentSavePanel(), "Save Document");
                JButton ok = new JButton();
                ok.setText("OK");
                JButton cancel = new JButton();
                cancel.setText("Cancel");
                ok.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        StatusDisplayer.getDefault().setStatusText("ok");
                    }
                });
                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        StatusDisplayer.getDefault().setStatusText("cancel");
                    }
                });
                nd.setOptions(new Object[]{ok, cancel});
                DialogDisplayer.getDefault().notifyLater(nd);
            }
        });
//        StatusDisplayer.getDefault().setStatusText("hello eben and arun!", );

//        final StyledDocument styledDoc = (StyledDocument) document;
//        GuardedSectionManager guards = GuardedSectionManager.getInstance(styledDoc);
//        EclipseFormatterUtilities u = new EclipseFormatterUtilities();
//        if (guards == null && this.formatter != null) {
//            u.reFormatWithEclipse(styledDoc, formatter, u.isJava(styledDoc));
//        } else {
//            if (EclipseFormatterUtilities.getGlobalPrefs().getBoolean("globalEclipseFormatterDebug", false) == true) {
//                NotificationDisplayer.getDefault().notify("NetBeans formatter", EclipseFormatterUtilities.icon, "(Files with guarded blocks are not supported by Eclipse formatters)", null);
//            }
//            u.reformatWithNetBeans(styledDoc);
//        }
    }

    @Override
    public void runLocked(Runnable run) {
        run.run();
    }

    @Override
    public boolean cancel() {
        canceled.set(true);
        return true;
    }

    @MimeRegistration(mimeType = "text/x-json", service = OnSaveTask.Factory.class, position = 1500)
    public static final class FactoryImpl implements OnSaveTask.Factory {

        @Override
        public OnSaveTask createTask(OnSaveTask.Context context) {
            return new JsonCouchbaseOnSaveTask(context.getDocument());
        }
    }

}
