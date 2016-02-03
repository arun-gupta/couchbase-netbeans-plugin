package org.netbeans.modules.couchbase.row;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.diff.Diff;
import org.netbeans.api.diff.DiffView;
import org.netbeans.api.diff.StreamSource;
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
import org.openide.windows.TopComponent;

@ActionID(
        category = "Document",
        id = "org.netbeans.modules.couchbase.row.BCompareDocumentAction"
)
@ActionRegistration(
        displayName = "#CTL_CompareDocumentAction"
)
@Messages("CTL_CompareDocumentAction=Compare Documents")
public final class CompareDocumentAction implements ActionListener {

    private final List<CouchBaseRow> context;

    public CompareDocumentAction(List<CouchBaseRow> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject obj1 = null;
        FileObject obj2 = null;
        CouchBaseRow cbr = context.get(0);
        CouchBaseRow cbr2 = context.get(1);
        try {
            String templateName = "Templates/Other/json.json";
            String fileName = cbr.getBucketName() + "-" + cbr.getI();
            String fileName2 = cbr.getBucketName() + "-" + cbr.getI();
            FileObject fo = FileUtil.getConfigRoot().getFileObject(templateName);
            FileObject fo2 = FileUtil.getConfigRoot().getFileObject(templateName);
            DataObject template = DataObject.find(fo);
            DataObject template2 = DataObject.find(fo2);
            FileSystem memFS = FileUtil.createMemoryFileSystem();
            FileObject root = memFS.getRoot();
            DataFolder dataFolder = DataFolder.findFolder(root);
            DataObject jsonFile1 = template.createFromTemplate(dataFolder, fileName);
            DataObject jsonFile2 = template2.createFromTemplate(dataFolder, fileName2);
            obj1 = jsonFile1.getPrimaryFile();
            obj2 = jsonFile2.getPrimaryFile();
            FileLock fileLock = null;
            OutputStreamWriter osw;
            try {
                fileLock = obj1.lock();
                OutputStream fout = obj1.getOutputStream(fileLock);
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
            FileLock fileLock2 = null;
            OutputStreamWriter osw2;
            try {
                fileLock2 = obj2.lock();
                OutputStream fout2 = obj2.getOutputStream(fileLock2);
                OutputStream bout2 = new BufferedOutputStream(fout2);
                osw2 = new OutputStreamWriter(bout2, "UTF-8");
                osw2.write(cbr2.getRow().value().toString());
                osw2.flush();
                osw2.close();
            } catch (IOException ex) {
            } finally {
                if (fileLock2 != null) {
                    fileLock2.releaseLock();
                }
            }
            formatDocument1(jsonFile1);
            formatDocument2(jsonFile2);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        StreamSource source1;
        try {
            source1 = StreamSource.createSource(
                    String.valueOf(cbr.getBucketName()+"-"+cbr.getI()), 
                    cbr.getBucketName()+"-"+String.valueOf(cbr.getI()), 
                    "text/x-json", 
                    new InputStreamReader(obj1.getInputStream()));
            StreamSource source2 = StreamSource.createSource(
                    String.valueOf(cbr.getBucketName()+"-"+cbr2.getI()), 
                    cbr2.getBucketName()+"-"+String.valueOf(cbr2.getI()), 
                    "text/x-json", 
                    new InputStreamReader(obj2.getInputStream()));
            DiffView view = Diff.getDefault().createDiff(source1, source2);
            TopComponent tc = new TopComponent();
            tc.setDisplayName("Couchbase Diff");
            tc.setLayout(new BorderLayout());
            tc.add(view.getComponent());
            tc.open();
            tc.requestActive();
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void formatDocument1(DataObject jsonFile1) throws IOException {
        EditorCookie ec = jsonFile1.getLookup().lookup(EditorCookie.class);
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
    }

    private void formatDocument2(DataObject jsonFile2) throws IOException {
        EditorCookie ec2 = jsonFile2.getLookup().lookup(EditorCookie.class);
        final StyledDocument doc2 = ec2.openDocument();
        final Reformat rf2 = Reformat.get(doc2);
        rf2.lock();
        try {
            NbDocument.runAtomicAsUser(doc2, new Runnable() {
                @Override
                public void run() {
                    try {
                        rf2.reformat(0, doc2.getLength());
                    } catch (BadLocationException ex) {
                    }
                }
            });
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            rf2.unlock();
        }
        ec2.saveDocument();
    }

}
