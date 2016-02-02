package org.netbeans.modules.couchbase.row;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.actions.Openable;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Document",
        id = "org.netbeans.modules.couchbase.row.AOpenDocumentAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenDocumentAction"
)
@Messages("CTL_OpenDocumentAction=Open Document")
public final class OpenDocumentAction implements ActionListener {

    private final List<CouchBaseRow> context;

    public OpenDocumentAction(List<CouchBaseRow> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        for (CouchBaseRow cbr : context) {
            try {
                String templateName = "Templates/Other/json.json";
                String fileName = cbr.getBucketName() + "-" + cbr.getI();
                FileObject fo = FileUtil.getConfigRoot().getFileObject(templateName);
                DataObject template = DataObject.find(fo);
                FileSystem memFS = FileUtil.createMemoryFileSystem();
                FileObject root = memFS.getRoot();
                DataFolder dataFolder = DataFolder.findFolder(root);
                DataObject jsonFile = template.createFromTemplate(dataFolder, fileName);
                FileObject obj = jsonFile.getPrimaryFile();
                FileLock fileLock = null;
                OutputStreamWriter osw;
                try {
                    fileLock = obj.lock();
                    OutputStream fout = obj.getOutputStream(fileLock);
                    OutputStream bout = new BufferedOutputStream(fout);
                    osw = new OutputStreamWriter(bout, "UTF-8");
                    osw.write(cbr.getRow().value().toString());
                    osw.flush();
                    osw.close();
                } catch (IOException ex) {
                } finally {
                    if (fileLock != null) {
                        fileLock.releaseLock();
                    }
                }
                EditorCookie ec = jsonFile.getLookup().lookup(EditorCookie.class);
                final StyledDocument doc = ec.openDocument();
                final Reformat rf = Reformat.get(doc);
                rf.lock();
                try {
                    NbDocument.runAtomicAsUser(doc, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                rf.reformat(0, doc.getLength());
                            } catch (BadLocationException ex) {
                            }
                        }
                    });
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    rf.unlock();
                }
                ec.saveDocument();
                jsonFile.getLookup().lookup(Openable.class).open();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
