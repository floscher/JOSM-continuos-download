package org.openstreetmap.josm.plugins.continuosDownload;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.concurrent.Future;

import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.io.OsmServerReader;

/*
 * This is a copy of the DownloadOsmTask that does not change the view after the area is downloaded.
 * It still displays modal windows and ugly dialog boxes :(
 */
public class DownloadOsmTask2 extends DownloadOsmTask {

    @Override
    public Future<?> download(OsmServerReader reader, boolean newLayer, Bounds downloadArea,
            ProgressMonitor progressMonitor) {
        return download(new DownloadTask2(newLayer, reader, progressMonitor), downloadArea);
    }

    @Override
    protected void rememberErrorMessage(String message) {
        // XXX: Remove error message that is not really an error to prevent ugly
        // popups
        if (!message.equals(tr("No data found in this area.")))
            super.rememberErrorMessage(message);
    }

    protected class DownloadTask2 extends DownloadTask {

        public DownloadTask2(boolean newLayer, OsmServerReader reader,
                ProgressMonitor progressMonitor) {
            super(newLayer, reader, progressMonitor);
        }

        @Override
        protected void computeBboxAndCenterScale(Bounds bounds) {
            BoundingXYVisitor v = new BoundingXYVisitor();
            if (bounds != null) {
                v.visit(bounds);
            } else {
                v.computeBoundingBox(dataSet.getNodes());
            }
            // Main.map.mapView.recalculateCenterScale(v);
        }
    }
}
