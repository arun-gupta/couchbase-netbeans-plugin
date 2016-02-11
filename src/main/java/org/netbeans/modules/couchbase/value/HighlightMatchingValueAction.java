package org.netbeans.modules.couchbase.value;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import org.netbeans.modules.couchbase.model.CouchBaseRow;
import org.netbeans.modules.couchbase.model.CouchbaseValue;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.LineCookie;
import org.openide.text.Line;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Value",
        id = "org.netbeans.modules.couchbase.value.HighlightMatchingValueAction"
)
@ActionRegistration(
        displayName = "#CTL_HighlightMatchingValueAction"
)
@Messages("CTL_HighlightMatchingValueAction=Highlight Matching Value")
public final class HighlightMatchingValueAction implements ActionListener {

    private final CouchbaseValue context;

    public HighlightMatchingValueAction(CouchbaseValue context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String name = context.getName();
        CouchBaseRow cbr = context.getCbr();
        String documentTabName = cbr.getBucketName() + "-" + cbr.getI();
        String dtName = documentTabName + ".json";
        DataObject dobj = findDataObject(dtName);
        LineCookie lc = dobj.getLookup().lookup(LineCookie.class);
        List<? extends Line> lines = lc.getLineSet().getLines();
        for (Line line : lines) {
            if (line.getText().contains(name)) {
                line.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FRONT);
            }
        }
    }

    private DataObject findDataObject(String displayName) {
        Set<TopComponent> opened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tc : opened) {
            if (tc != null && tc.getDisplayName() != null) {
                if (tc.getDisplayName().equals(displayName)) {
                    return tc.getLookup().lookup(DataObject.class);
                }
            }
        }
        return null;
    }

}
