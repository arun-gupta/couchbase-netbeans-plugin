package org.netbeans.modules.couchbase.row;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import org.netbeans.api.diff.Diff;
import org.netbeans.api.diff.DiffView;
import org.netbeans.api.diff.StreamSource;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Document",
        id = "org.netbeans.modules.couchbase.row.CompareDocumentAction"
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
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        StreamSource source1;
        try {
            source1 = StreamSource.createSource("name1", "title1", "text/x-json", new InputStreamReader(obj1.getInputStream()));
            StreamSource source2 = StreamSource.createSource("name2", "title2", "text/x-json", new InputStreamReader(obj2.getInputStream()));
            DiffView view = Diff.getDefault().createDiff(source1, source2);
            TopComponent tc = new TopComponent();
            tc.setDisplayName("Diff Window");
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

}
