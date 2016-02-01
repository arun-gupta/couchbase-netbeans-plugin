package org.netbeans.modules.couchbase.row;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.couchbase.value.LeafValueChildFactory;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;

public class DocumentNode extends BeanNode<CouchBaseRow> {

    @StaticResource
    private static final String ICON = "org/netbeans/modules/couchbase/collection.png";
    private final CouchBaseRow cbr;

    public DocumentNode(CouchBaseRow cbr) throws IntrospectionException {
        super(cbr, Children.create(new LeafValueChildFactory(cbr.getRow()), true));
        this.cbr = cbr;
        setDisplayName(String.valueOf(cbr.getI()));
        setIconBaseWithExtension(ICON);
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{new AbstractAction("Open Document...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String templateName = "Templates/Other/json.json";
                    String fileName = cbr.getBucketName() + "-" + getDisplayName();
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
                    jsonFile.getLookup().lookup(Openable.class).open();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }};
    }

    @Override
    public Action getPreferredAction() {
        return null;
    }

}
